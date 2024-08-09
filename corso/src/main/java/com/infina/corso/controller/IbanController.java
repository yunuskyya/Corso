package com.infina.corso.controller;

import com.infina.corso.model.Iban;
import com.infina.corso.service.IbanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/iban")
public class IbanController {

    private final IbanService ibanService;

    @Autowired
    public IbanController(IbanService ibanService) {
        this.ibanService = ibanService;
    }

    @PostMapping
    public ResponseEntity<Iban> createIban(@RequestBody Iban iban) {
        Iban savedIban = ibanService.saveIban(iban);
        return ResponseEntity.ok(savedIban);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Iban> getIbanById(@PathVariable Long id) {
        Optional<Iban> iban = ibanService.getIbanById(id);
        return iban.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Iban>> getAllIbans() {
        List<Iban> ibans = ibanService.getAllIbans();
        return ResponseEntity.ok(ibans);
    }

    /*
    @PutMapping("/{id}")
    public ResponseEntity<Iban> updateIban(@PathVariable Long id, @RequestBody Iban iban) {
        Iban updatedIban = ibanService.updateIban(id, iban);
        return ResponseEntity.ok(updatedIban);
    }

     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIban(@PathVariable Long id) {
        ibanService.deleteIban(id);
        return ResponseEntity.noContent().build();
    }
}
