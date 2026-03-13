import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserDAO implements IUserDAO, UserDTOMapper {

    private final ConnectionConfiguration connection;

    public UserDAO(ConnectionConfiguration connection) {
        this.connection = connection;
    }

    public UserDAO() {
        this(new ConnectionConfiguration());
    }

    @Override
    public void saveUser(UserDTO user) {
        Transaction transaction = null;
        try (Session session = connection.getSession()) {
            transaction = session.beginTransaction();
            session.persist(mapToUser(user));
            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = getUserEntityByEmail(email);
        return mapToDTO(user);
    }

    private User getUserEntityByEmail(String email) {
        try (Session session = connection.getSession()) {
            return session.createQuery(
                "FROM User WHERE email = :email", User.class)
                .setParameter("email", email)
                .uniqueResult();

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void updateUser(UserDTO user) {
        Transaction transaction = null;
        try (Session session = connection.getSession()) {
            User existing = getUserEntityByEmail(user.getEmail());

            if (existing == null) return;

            existing.setName(user.getName());
            existing.setAge(user.getAge());

            transaction = session.beginTransaction();
            session.merge(existing);
            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            System.out.println(e.getMessage());
        }
    }

    public void updateUserEmail(UserDTO user, String newEmail) {
        Transaction transaction = null;
        try (Session session = connection.getSession()) {
            User existing = getUserEntityByEmail(user.getEmail());

            if (userExists(newEmail)) return;

            existing.setEmail(newEmail);

            transaction = session.beginTransaction();
            session.merge(existing);
            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteUser(UserDTO user) {
        Transaction transaction = null;
        try (Session session = connection.getSession()) {
            transaction = session.beginTransaction();
            session.remove(getUserEntityByEmail(user.getEmail()));
            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            System.out.println(e.getMessage());
        }
    }

    public void clearTable() {
        try (Session session = connection.getSession()) {
            session.beginTransaction();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean userExists(UserDTO user) {
        return getUserEntityByEmail(user.getEmail()) != null;
    }

    public boolean userExists(String email) {
        return getUserEntityByEmail(email) != null;
    }

    @Override
    public UserDTO mapToDTO(User user) {
        if (user == null) return null;
        return new UserDTO(user);
    }

    @Override
    public User mapToUser(UserDTO dto) {
        if (dto == null) return null;
        return new User(dto.getName(), dto.getEmail(), dto.getAge());
    }
}
