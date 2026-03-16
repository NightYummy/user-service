import java.util.Scanner;

public final class UserService {

    private static final Scanner scanner = new Scanner(System.in);
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserService() {
        this(new UserDAO());
    }

    public boolean mainMenu() {
        System.out.println("\n========== ГЛАВНОЕ МЕНЮ ==========");
        System.out.println("1 - Создать пользователя");
        System.out.println("2 - Найти пользователя по E-mail");
        System.out.println("3 - !!! Удалить всех пользователей !!!");
        System.out.println("0 - Выход из программы\n");
        System.out.print("Выберите действие: ");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1: createUser(); return true;
            case 2: findUser(); return true;
            case 3: deleteAllUsersMenu(); return true;
            case 0: scanner.close(); break;
            default:
                System.out.println("\nНеверный ввод! Пожалуйста, выберите пункт из меню\n");
                return true;
        }
        return false;
    }

    private void userMenu(UserDTO user) {
        System.out.println("\n========== МЕНЮ ПОЛЬЗОВАТЕЛЯ ==========");
        System.out.println(user);
        System.out.println("1 - Редактировать данные пользователя");
        System.out.println("2 - Удалить пользователя");
        System.out.println("0 - Вернуться в главное меню\n");
        System.out.print("Выберите действие: ");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1: updateUserMenu(user); break;
            case 2: deleteUserMenu(user); break;
            case 0: return;
            default:
                System.out.println("Неверный ввод! Пожалуйста, выберите пункт из меню");
        }
    }

    private void deleteUserMenu(UserDTO user) {
        scanner.nextLine();
        System.out.println("Пользователь будет удален");
        System.out.print("Вы уверены? да/нет ('нет' по умолчанию): ");

        String choice = scanner.nextLine().trim().toLowerCase();
        switch (choice) {
            case "да", "д", "yes", "y": userDAO.deleteUser(user); break;
            case "нет", "н", "no", "n", "": userMenu(user);
            default:
                System.out.println("Неверный ввод! Пожалуйста, выберите пункт из меню");
        }
        System.out.println("\nПользователь был удален");
    }

    private void deleteAllUsersMenu() {
        scanner.nextLine();
        System.out.println("\n!!! Внимание !!!");
        System.out.println("Все пользователи будут удалены");
        System.out.print("Вы уверены? да/нет ('нет' по уполчанию): ");

        String choice = scanner.nextLine().trim().toLowerCase();
        switch (choice) {
            case "да", "д", "yes", "y": userDAO.clearTable(); break;
            case "нет", "н", "no", "n", "": System.out.println(); return;
            default:
                System.out.println("\nНеверный ввод! Пожалуйста, выберите пункт из меню\n");
        }
        System.out.println("\nВсе пользователи были удалены\n");
    }

    private void updateUserMenu(UserDTO user) {
        System.out.println("\n========== РЕДАКТИРОВАНИЕ ПОЛЬЗОВАТЕЛЯ ==========");
        System.out.println(user);
        System.out.println("1 - Редактировать имя");
        System.out.println("2 - Редактировать E-mail");
        System.out.println("3 - Редактировать возраст");
        System.out.println("0 - Вернуться в меню пользователя\n");
        System.out.print("Выберите действие: ");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                scanner.nextLine();
                while (true) {
                    System.out.print("Введите новое имя пользователя: ");
                    String name = scanner.nextLine().trim();
                    if (name.matches("^\\p{L}+$")) {
                        user.setName(name);
                        break;
                    }
                    else System.out.print("\nИмя указано неверно\n");
                }
                break;
            case 2:
                scanner.nextLine();
                while (true) {
                    System.out.print("Введите новый E-mail пользователя: ");
                    String email = scanner.nextLine().trim();
                    if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                        System.out.print("\nE-mail указан неверно");
                        continue;
                    }
                    if (userDAO.getUserByEmail(email) != null) {
                        System.out.print("Пользователь с таким E-mail уже существует\n");
                        continue;
                    }
                    userDAO.updateUserEmail(user, email);
                    break;
                }
                break;
            case 3:
                scanner.nextLine();
                while (true) {
                    System.out.print("Введите возраст пользователя: ");
                    int age = scanner.nextInt();
                    if (age < 18) {
                        System.out.println("Вам нет 18 лет");
                        break;
                    }
                    if (age > 100) System.out.println("Возраст указан некорректно");
                    else {
                        user.setAge(age);
                        break;
                    }
                }
                break;
            case 0: userMenu(user);
            default:
                System.out.println("Неверный ввод! Пожалуйста, выберите пункт из меню");
        }
        userDAO.updateUser(user);
    }

    private void createUser() {
        scanner.nextLine();
        UserDTO user = new UserDTO();

        while (user.getName().isEmpty()) {
            System.out.print("\nВведите имя пользователя: ");
            String input = scanner.nextLine();
            if (input.matches("^\\p{L}+$")) user.setName(input);
            else System.out.print("\nИмя указано неверно");
        }

        while (user.getEmail().isEmpty()) {
            System.out.print("Введите E-mail пользователя: ");
            String input = scanner.nextLine().trim();
            if (!input.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                System.out.print("\nE-mail указан неверно\n");
                continue;
            }
            user.setEmail(input);
            if (userDAO.userExists(user)) {
                System.out.print("Пользователь с таким E-mail уже существует\n");
                user.setEmail("");
            }
        }

        while (user.getAge() < 18 || user.getAge() > 100) {
            System.out.print("Введите возраст пользователя: ");
            int input = scanner.nextInt();
            if (input < 18) {
                System.out.println("\nВам нет 18 лет");
                continue;
            }
            if (input > 100) System.out.println("Возраст указан некорректно");
            else user.setAge(input);
        }
        userDAO.saveUser(user);
        System.out.println("Пользователь сохранен\n");
    }

    private void findUser() {
        scanner.nextLine();
        System.out.print("Введите E-mail пользователя: ");
        String input = scanner.nextLine();

        if (!input.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            System.out.println("\nE-mail указан неверно\n");
            return;
        }

        UserDTO user = userDAO.getUserByEmail(input);
        if (user == null) {
            System.out.println("\nПользователь с таким E-mail не найден\n");
            return;
        }
        userMenu(user);
    }
}
