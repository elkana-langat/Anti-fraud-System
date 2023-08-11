package antifraud.service;

import antifraud.dto.AppUserDTO;
import antifraud.entity.AppUser;
import antifraud.model.DeleteResponse;
import antifraud.model.UserRequest;
import antifraud.repository.AppUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    public AppUserDTO registerUser(UserRequest request) {
        AppUser user = new AppUser();

        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

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

        return appUserDTO;
    }

}