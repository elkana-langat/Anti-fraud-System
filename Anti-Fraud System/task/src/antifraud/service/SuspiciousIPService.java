package antifraud.service;

import antifraud.entity.SuspiciousIP;
import antifraud.model.IP;
import antifraud.model.SuspiciousIPResponse;
import antifraud.repository.SuspiciousIPRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SuspiciousIPService {

    private final SuspiciousIPRepository suspiciousIPRepository;

    public SuspiciousIPService(SuspiciousIPRepository suspiciousIPRepository) {
        this.suspiciousIPRepository = suspiciousIPRepository;
    }

    // add suspicious IP to database
    public SuspiciousIP addSusIP(IP ip) {
        SuspiciousIP suspiciousIP = new SuspiciousIP();
        suspiciousIP.setIp(ip.getIp());
        return suspiciousIPRepository.save(suspiciousIP);
    }

    // delete SuspiciousIp from DB
    public SuspiciousIPResponse deleteSuspiciousIP(String ip) {
        SuspiciousIPResponse response = new SuspiciousIPResponse();
        Optional<SuspiciousIP> suspiciousIP =
                suspiciousIPRepository.findByIp(ip);

        String reply = String.format("IP %s successfully removed!", ip);

        response.setStatus(reply);

        suspiciousIPRepository.deleteById(suspiciousIP.get().getId());

        return response;
    }

    // get all suspicious IP from the database
    public List<SuspiciousIP> getAllSusIP() {
        return suspiciousIPRepository.findAll();
    }

    // check if IP exists
    public boolean checkIfExists(String ip) {
        return suspiciousIPRepository.existsByIp(ip);
    }
}
