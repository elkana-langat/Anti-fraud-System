package antifraud.controller;

import antifraud.dto.AppUserDTO;
import antifraud.model.DeleteResponse;
import antifraud.model.UserRequest;
import antifraud.service.AppUserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
