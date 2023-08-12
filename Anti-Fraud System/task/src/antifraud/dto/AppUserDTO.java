package antifraud.dto;

import antifraud.model.UserRoles;

public class AppUserDTO {

    private Long id;
    private String name;
    private String username;
    private UserRoles role;

    public AppUserDTO() {
    }

    public AppUserDTO(Long id, String name, String username, UserRoles role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }
}


