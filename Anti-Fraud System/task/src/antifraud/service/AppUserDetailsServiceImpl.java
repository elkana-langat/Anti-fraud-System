package antifraud.service;

import antifraud.dto.AppUserDTO;
import antifraud.entity.AppUser;
import antifraud.model.*;
import antifraud.repository.AppUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AppUserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AppUserDetailsServiceImpl(AppUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
                .findAppUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));
    }

    // check if a user exists with username
    public boolean userExists(String username) {
        return repository
                .existsByUsernameIgnoreCase(username);
    }

    // get all users in the database
    public List<AppUserDTO> getAllUser() {
        List<AppUserDTO> appUserDTOS = new ArrayList<>();
        List<AppUser> appUsers = repository.findAll();

        for (AppUser user : appUsers) {
            appUserDTOS.add(convertAppUser(user));
        }


        return appUserDTOS;
    }

    // convert UserRequest to AppUser
    @Transactional
    public AppUserDTO registerUser(UserRequest request) {
        long countAll = repository.count();
        AppUser user = new AppUser();

        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (countAll == 0) {
            user.setRole(UserRoles.ADMINISTRATOR);
            user.setAccountLocked(true);
        } else {
            user.setRole(UserRoles.MERCHANT);
            user.setAccountLocked(false);
        }

        return convertAppUser(repository.save(user));
    }

    // delete user by username
    @Transactional
    public DeleteResponse deleteUser(String username) {
        Optional<AppUser> user = repository.findAppUserByUsernameIgnoreCase(username);
        String usernameToDelete = user.get().getUsername();

        DeleteResponse response = new DeleteResponse();
        response.setUsername(usernameToDelete);
        response.setStatus("Deleted successfully!");

        repository.deleteByUsername(usernameToDelete);

        return response;
    }

    // convert AppUser to AppUserDto
    public AppUserDTO convertAppUser(AppUser user) {
        AppUserDTO appUserDTO = new AppUserDTO();

        appUserDTO.setId(user.getId());
        appUserDTO.setName(user.getName());
        appUserDTO.setUsername(user.getUsername());
        appUserDTO.setRole(user.getRole());

        return appUserDTO;
    }

    // change a user role
    public ResponseEntity<AppUserDTO> changeUserRole(UserRoleRequest userRoleRequest) {
        Optional<AppUser> user =
                repository.findAppUserByUsernameIgnoreCase(userRoleRequest.getUsername());

        String userRoleFromDB = String.valueOf(user.get().getRole());
        String newUserRoleRequest = userRoleRequest.getRole();

        if (Objects.equals(userRoleFromDB, "ADMINISTRATOR")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            if (Objects.equals(userRoleFromDB, newUserRoleRequest)) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } else {
                if (Objects.equals(newUserRoleRequest, "SUPPORT")) {
                    user.get().setRole(UserRoles.SUPPORT);
                } else if (Objects.equals(newUserRoleRequest, "MERCHANT")) {
                    user.get().setRole(UserRoles.MERCHANT);
                }
            }

            return new ResponseEntity<>(convertAppUser(repository.save(user.get())
            ), HttpStatus.OK);
        }
    }

    // change a user access roles
    public ResponseEntity<AccessResponse> changeUserAccessStatus(UserAccessRequest userAccessRequest) {
        Optional<AppUser> user =
                repository.findAppUserByUsernameIgnoreCase(userAccessRequest.getUsername());

        String userRole = String.valueOf(user.get().getRole());

        if (Objects.equals(userRole, "ADMINISTRATOR")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            String operation = userAccessRequest.getOperation();
            AccessResponse accessResponse = new AccessResponse();

            if (Objects.equals(operation, "LOCK")) {
                user.get().setAccountLocked(false);
                repository.save(user.get());
                String status = String.format("User %s locked!",
                        userAccessRequest.getUsername());
                accessResponse.setStatus(status);
                return new ResponseEntity<>(accessResponse, HttpStatus.OK);
            } else if (Objects.equals(operation, "UNLOCK")) {
                user.get().setAccountLocked(true);
                repository.save(user.get());
                String status = String.format("User %s unlocked!",
                        userAccessRequest.getUsername());
                accessResponse.setStatus(status);
                return new ResponseEntity<>(accessResponse, HttpStatus.OK);
            }
        }
        return null;
    }

}