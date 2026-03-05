public class Main {

    public static void main(String[] args) {
        UserDAO.clearTable();

        User user1 = new User("Oleg", "oleg@mail.ru", 22);
        User user2 = new User("Olga", "olga@mail.ru", 25);

        UserDAO.saveUser(user1);
        UserDAO.saveUser(user2);

        System.out.println(UserDAO.getUserByEmail("olga@mail.ru"));

        user2.setAge(27);
        UserDAO.updateUser(user2);

        System.out.println(UserDAO.getUserByEmail("olga@mail.ru"));

        UserDAO.deleteUser(user2);
    }
}
