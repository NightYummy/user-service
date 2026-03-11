public class UserDTO {

    private String name;
    private String email;
    private int age;

    public UserDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.age = user.getAge();
    }

    public UserDTO() {
        this(new User());
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Имя: " + name +
                "; E-mail: " + email +
                "; возраст: " + age;
    }
}
