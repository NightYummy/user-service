import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

    @Column(unique = true)
    private String email;

    @Column
    private int age;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    public User() {}

    public User(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = ZonedDateTime.now();
    }

    public long getId() { return this.id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Пользователь: " + name +
                "; E-mail: " + email +
                "; возраст: " + age +
                "; создан: " + createdAt;
    }
}
