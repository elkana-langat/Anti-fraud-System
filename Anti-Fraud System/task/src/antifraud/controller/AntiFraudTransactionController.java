package antifraud.controller;

import antifraud.model.TransactionRequest;
import antifraud.model.TransactionStatus;
import antifraud.model.ValidationResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/antifraud")
public class AntiFraudTransactionController {

    @PostMapping("/transaction")
    public ResponseEntity<ValidationResponse> processTransaction(@Valid @RequestBody TransactionRequest request, BindingResult bindingResult) {
        Long amount = request.getAmount();

        ValidationResponse response = new ValidationResponse();

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (amount <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (amount <= 200) {
            response.setResult(TransactionStatus.ALLOWED);
        } else if (amount <= 1500) {
            response.setResult(TransactionStatus.MANUAL_PROCESSING);
        } else {
            response.setResult(TransactionStatus.PROHIBITED);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}