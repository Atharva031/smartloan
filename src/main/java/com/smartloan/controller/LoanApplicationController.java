package com.smartloan.controller;

import com.smartloan.model.LoanApplication;
import com.smartloan.model.LoanApplication.ApplicationStatus;
import com.smartloan.service.LoanApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    @PostMapping("/apply/{userId}")
    public ResponseEntity<LoanApplication> applyForLoan(
            @PathVariable Long userId,
            @Valid @RequestBody LoanApplication loanApplication) {
        LoanApplication result = loanApplicationService.applyForLoan(userId, loanApplication);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanApplication>> getUserLoans(@PathVariable Long userId) {
        return ResponseEntity.ok(loanApplicationService.getUserLoans(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<LoanApplication>> getLoansByStatus(@PathVariable ApplicationStatus status) {
        return ResponseEntity.ok(loanApplicationService.getLoansByStatus(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanApplication> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(loanApplicationService.getLoanById(id));
    }

    @GetMapping
    public ResponseEntity<List<LoanApplication>> getAllLoans() {
        return ResponseEntity.ok(loanApplicationService.getAllLoans());
    }
}