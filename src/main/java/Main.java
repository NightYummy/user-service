import app.ConnectionConfiguration;
import app.UserService;
import dao.UserDAOImpl;

public class Main {

    public static void main(String[] args) {
        ConnectionConfiguration connection = new ConnectionConfiguration();
        UserDAOImpl userDAO = new UserDAOImpl(connection);
        UserService service = new UserService(userDAO);

        boolean running = true;
        while (running) {
            running = service.mainMenu();
        }

        connection.shutdown();
    }
}
