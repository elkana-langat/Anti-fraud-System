package antifraud.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class SuspiciousIP {

    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String ip;

    public SuspiciousIP() {
    }

    public SuspiciousIP(Long id, String ip) {
        this.id = id;
        this.ip = ip;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
