package antifraud.repository;

import antifraud.entity.Stolencards;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StolencardsRepository extends JpaRepository<Stolencards, Long> {
    Optional<Stolencards> findById(Long id);

    boolean existsByNumber(String number);

    Optional<Stolencards> findByNumber(String number);
}
