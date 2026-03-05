import org.hibernate.Session;
import org.hibernate.Transaction;

public final class UserDAO {

    public static void saveUser(User user) {
        Transaction transaction = null;
        try (Session session = ConnectionConfiguration.getSession()) {
            boolean exists = session.createQuery(
                "FROM User WHERE email = :email", User.class)
                .setParameter("email", user.getEmail())
                .uniqueResult() != null;

            if (exists) {
                System.out.println("Пользователь с E-mail " + user.getEmail() + " уже существует.");
                return;
            }

            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            System.out.println("Пользователь сохранен");

        } catch (Exception e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            System.out.println(e.getMessage());
        }
    }

    public static User getUserByEmail(String email) {
        try (Session session = ConnectionConfiguration.getSession()) {
            return session.createQuery(
                    "FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void updateUser(User user) {
        Transaction transaction = null;
        try (Session session = ConnectionConfiguration.getSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            System.out.println("Данные о пользователе обновлены");

        } catch (Exception e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            System.out.println(e.getMessage());
        }
    }

    public static void deleteUser(User user) {
        Transaction transaction = null;
        try (Session session = ConnectionConfiguration.getSession()) {
            transaction = session.beginTransaction();
            session.remove(user);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            System.out.println(e.getMessage());
        }
    }

    public static void clearTable() {
        try (Session session = ConnectionConfiguration.getSession()) {
            session.beginTransaction();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
