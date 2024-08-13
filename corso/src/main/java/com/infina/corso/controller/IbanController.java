package com.infina.corso.controller;

import com.infina.corso.dto.request.IbanRegisterRequest;
import com.infina.corso.dto.response.IbanResponse;
import com.infina.corso.model.Iban;
import com.infina.corso.service.IbanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/iban")
public class IbanController {

    private final IbanService ibanService;

    @Autowired
    public IbanController(IbanService ibanService) {
        this.ibanService = ibanService;
    }

    @PostMapping
    public ResponseEntity<?> createIban(@RequestBody IbanRegisterRequest ibanRegisterRequest) {
        ibanService.saveIban(ibanRegisterRequest);
        return ResponseEntity.ok("Iban başarılı şekilde kaydedildi.");
    }

    @GetMapping("/customer/{id}")
    public List<IbanResponse> getIbanByCustomerId(@PathVariable Long id) {
        return ibanService.getIbansByCustomerId(id);
    }

    @GetMapping
    public ResponseEntity<List<Iban>> getAllIbans() {
        List<Iban> ibans = ibanService.getAllIbans();
        return ResponseEntity.ok(ibans);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIban(@PathVariable Long id) {
        ibanService.deleteIban(id);
        return ResponseEntity.noContent().build();
    }
}
