package antifraud.controller;

import antifraud.dto.AppUserDTO;
import antifraud.model.*;
import antifraud.service.AppUserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserDetailsServiceImpl appUserDetailsService;

    public AuthController(AppUserDetailsServiceImpl appUserDetailsService) {
        this.appUserDetailsService = appUserDetailsService;
    }

    @PostMapping("/user")
    public ResponseEntity<AppUserDTO> registerUser(
            @Valid @RequestBody UserRequest userRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            if (appUserDetailsService.userExists(userRequest.getUsername())) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<>(appUserDetailsService.registerUser(userRequest),
                        HttpStatus.CREATED);
            }
        }
    }

    @GetMapping("/list")
    public List<AppUserDTO> getAllUsers() {
        return appUserDetailsService.getAllUser();
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<DeleteResponse> deleteUser(@PathVariable String username) {
        if (appUserDetailsService.userExists(username)) {
            return new ResponseEntity<>(appUserDetailsService.deleteUser(username), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/role")
    public ResponseEntity<AppUserDTO> changeUserRole(@Valid @RequestBody UserRoleRequest userRoleRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            if (appUserDetailsService.userExists(userRoleRequest.getUsername())) {
                if (!Objects.equals(userRoleRequest.getRole(), "MERCHANT") && !Objects.equals(userRoleRequest.getRole(), "SUPPORT")) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                } else {
                    return appUserDetailsService.changeUserRole(userRoleRequest); // Return the response from changeUserRole
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }


    @PutMapping("/access")
    public ResponseEntity<AccessResponse> changeUserAccess(@Valid @RequestBody UserAccessRequest userAccessRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            if (appUserDetailsService.userExists(userAccessRequest.getUsername())) {
                return appUserDetailsService.changeUserAccessStatus(userAccessRequest);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }
}
