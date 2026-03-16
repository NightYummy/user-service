public class Main {

    public static void main(String[] args) {
        ConnectionConfiguration connection = new ConnectionConfiguration();
        UserDAO userDAO = new UserDAO(connection);
        UserService service = new UserService(userDAO);

        boolean running = true;
        while (running) {
            running = service.mainMenu();
        }

        connection.shutdown();
    }
}
