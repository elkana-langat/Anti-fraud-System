package antifraud.repository;

import antifraud.entity.AppUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findAppUserByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);

    @Transactional
    void deleteByUsername(String usernameToDelete);

    long count();
}
