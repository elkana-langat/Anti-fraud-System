package antifraud.model;

import jakarta.validation.constraints.NotBlank;

public class UserRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public UserRequest() {
    }

    public UserRequest(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword() {
        this.password = password;
    }
}
