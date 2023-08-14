package antifraud.controller;

import antifraud.entity.Stolencards;
import antifraud.entity.SuspiciousIP;
import antifraud.model.*;
import antifraud.service.StolencardsService;
import antifraud.service.SuspiciousIPService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/antifraud")
public class AntiFraudTransactionController {

    private final SuspiciousIPService suspiciousIPService;
    private final StolencardsService stolencardsService;

    public AntiFraudTransactionController(SuspiciousIPService suspiciousIPService, StolencardsService stolencardsService) {
        this.suspiciousIPService = suspiciousIPService;
        this.stolencardsService = stolencardsService;
    }

    @PostMapping("/transaction")
    public ResponseEntity<ValidationResponse> processTransaction(@Valid @RequestBody TransactionRequest request, BindingResult bindingResult) {
        ValidationResponse response = new ValidationResponse();

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // arraylist to store the transaction status and the info
        ArrayList<String> storeTransaction = new ArrayList<>();
        ArrayList<String> storeInfo = new ArrayList<>();

        Long amount = request.getAmount();

        // check if the ip address is in the blacklist
        boolean isInBlacklistIP = suspiciousIPService.checkIfExists(request.getIp());

        // check if the number is in the blacklist
        boolean isInBlacklistNumber = stolencardsService.checkIfExists(request.getNumber());

        if (amount <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (amount <= 200) {
            response.setResult("ALLOWED");
            storeTransaction.add("ALLOWED");
            storeInfo.add("none");
        } else if (amount <= 1500) {
            response.setResult("MANUAL_PROCESSING");
            storeTransaction.add("MANUAL_PROCESSING");
            storeInfo.add("amount");
        } else {
            response.setResult("PROHIBITED");
            storeTransaction.add("PROHIBITED");
            storeInfo.add("amount");
        }

        if (isInBlacklistIP && isInBlacklistNumber) {
            if (Objects.equals(response.getResult(), "MANUAL_PROCESSING")) {
                storeInfo.remove("amount");
            }

            storeTransaction.add("PROHIBITED");
            storeInfo.remove("none");
            storeInfo.add("ip");
            storeInfo.add("card-number");
        } else if (isInBlacklistIP) {
            if (Objects.equals(response.getResult(), "MANUAL_PROCESSING")) {
                storeInfo.remove("amount");
            }

            storeTransaction.add("PROHIBITED");
            storeInfo.remove("none");
            storeInfo.add("ip");
        } else if (isInBlacklistNumber) {
            if (Objects.equals(response.getResult(), "MANUAL_PROCESSING")) {
                storeInfo.remove("amount");
            }

            storeTransaction.add("PROHIBITED");
            storeInfo.remove("none");
            storeInfo.add("card-number");
        }

        // check length of the arraylist that stores the transaction
        int length = storeTransaction.size();

        String transaction = "";

        if (length == 1) {
            transaction = storeTransaction.get(0);
        } else {
            if (storeTransaction.contains("PROHIBITED")) {
                transaction = "PROHIBITED";
            }
        }

        // sort the data and store the info in a String
        Collections.sort(storeInfo);
        String info = String.join(", ", storeInfo);

        response.setResult(transaction);
        response.setInfo(info);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/suspicious-ip")
    public ResponseEntity<SuspiciousIP> addSuspiciousIP(@Valid @RequestBody IP ip, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            if (suspiciousIPService.checkIfExists(ip.getIp())) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<>(suspiciousIPService.addSusIP(ip),
                        HttpStatus.OK);
            }
        }
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public ResponseEntity<SuspiciousIPResponse> deleteSusIP(@PathVariable String ip) {
        String regex = "^((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9]?[0-9])$";

        // check if the ip address matches the regex string
        boolean matches = ip.matches(regex);

        if (!matches) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (suspiciousIPService.checkIfExists(ip)) {
            return new ResponseEntity<>(suspiciousIPService.deleteSuspiciousIP(ip), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/suspicious-ip")
    public ResponseEntity<List<SuspiciousIP>> getAllSusIP() {
        return new ResponseEntity<>(suspiciousIPService.getAllSusIP(),
                HttpStatus.OK);
    }

    @PostMapping("/stolencard")
    public ResponseEntity<Stolencards> addStolenCard(@RequestBody Stolencard stolencard) {
        if (!stolencardsService.checkLuhn(stolencard.getNumber())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (stolencardsService.checkIfExists(stolencard.getNumber())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(stolencardsService.addStolenCard(stolencard), HttpStatus.OK);
        }
    }

    @DeleteMapping("/stolencard/{number}")
    public ResponseEntity<SuspiciousIPResponse> deleteStolenCard(@PathVariable String number) {
        if (!stolencardsService.checkLuhn(number)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (stolencardsService.checkIfExists(number)) {
            return new ResponseEntity<>(stolencardsService.deleteStolencard(number), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/stolencard")
    public ResponseEntity<List<Stolencards>> getAllStolenCards() {
        return new ResponseEntity<>(stolencardsService.getAllStolenCards(),
                HttpStatus.OK);
    }
}