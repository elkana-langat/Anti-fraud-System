package antifraud.model;

import jakarta.validation.constraints.NotBlank;

public class UserRoleRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String role;

    public UserRoleRequest() {
    }

    public UserRoleRequest(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
