package antifraud.service;

import antifraud.entity.Stolencards;
import antifraud.model.Stolencard;
import antifraud.model.SuspiciousIPResponse;
import antifraud.repository.StolencardsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StolencardsService {

    private final StolencardsRepository repository;

    public StolencardsService(StolencardsRepository repository) {
        this.repository = repository;
    }

    // Returns true if given
    // card number is valid
    public boolean checkLuhn(String cardNo) {
        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {

            int d = cardNo.charAt(i) - '0';

            if (isSecond)
                d = d * 2;

            // We add two digits to handle
            // cases that make two digits
            // after doubling
            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

    // add stolencard to the db
    public Stolencards addStolenCard(Stolencard stolencard) {
        Stolencards stolencards = new Stolencards();
        stolencards.setNumber(stolencard.getNumber());

        return repository.save(stolencards);
    }

    // check if stolencard exists
    public boolean checkIfExists(String number) {
        return repository.existsByNumber(number);
    }

    // delete the stolencard from the db
    public SuspiciousIPResponse deleteStolencard(String number) {
        SuspiciousIPResponse response = new SuspiciousIPResponse();
        Optional<Stolencards> stolencards = repository.findByNumber(number);

        String reply = String.format("Card %s successfully removed!", number);

        response.setStatus(reply);

        repository.deleteById(stolencards.get().getId());

        return response;
    }

    // get all stored stolenCards from the db
    public List<Stolencards> getAllStolenCards() {
        return repository.findAll();
    }
}
