package com.nightyummy.dto;

import com.nightyummy.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

@Schema(name = "User", description = "User data")
public class UserDTO {

    @Schema(description = "User's name", example = "Ivan")
    @Pattern(regexp = "^\\p{L}+$", message = "Invalid name")
    private String name;

    @Schema(description = "User's email", example = "ivan@mail.ru")
    @Email(message = "Invalid E-mail")
    private String email;

    @Schema(description = "User's age", example = "25")
    @Min(value = 18, message = "Age must be more than 18")
    @Max(value = 100, message = "Age cannot exceed 100")
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
        return "Name: " + name +
                "; E-mail: " + email +
                "; age: " + age;
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
