package com.nightyummy.dto;

import com.nightyummy.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public class UserDTO {

    @Pattern(regexp = "^\\p{L}+$", message = "Некорректное имя")
    private String name;

    @Email(message = "Некорректный E-mail")
    private String email;

    @Min(value = 18, message = "Возраст не может менее 18")
    @Max(value = 100, message = "Возраст не может более 100")
    private int age;

    public UserDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.age = user.getAge();
    }

    public UserDTO(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
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

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        UserDTO dto = (UserDTO) obj;
        return name.equals(dto.getName()) && email.equals(dto.getEmail()) && age == dto.getAge();
    }
}
