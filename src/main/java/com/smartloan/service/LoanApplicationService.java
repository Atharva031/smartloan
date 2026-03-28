package com.smartloan.service;

import com.smartloan.model.LoanApplication;
import com.smartloan.model.LoanApplication.ApplicationStatus;
import com.smartloan.model.User;
import com.smartloan.repository.LoanApplicationRepository;
import com.smartloan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final UserRepository userRepository;

    public LoanApplication applyForLoan(Long userId, LoanApplication loanApplication) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        loanApplication.setUser(user);

        int score = calculateCreditScore(user, loanApplication);
        loanApplication.setCreditScore(score);

        if (score >= 70) {
            loanApplication.setStatus(ApplicationStatus.APPROVED);
            loanApplication.setRemarks("Auto-approved based on credit score: " + score);
        } else if (score >= 50) {
            loanApplication.setStatus(ApplicationStatus.UNDER_REVIEW);
            loanApplication.setRemarks("Manual review required. Credit score: " + score);
        } else {
            loanApplication.setStatus(ApplicationStatus.REJECTED);
            loanApplication.setRemarks("Auto-rejected based on credit score: " + score);
        }

        loanApplication.setReviewedAt(LocalDateTime.now());
        return loanApplicationRepository.save(loanApplication);
    }

    private int calculateCreditScore(User user, LoanApplication application) {
        int score = 0;

        // Employment type score (max 30 points)
        switch (user.getEmploymentType()) {
            case "SALARIED"      -> score += 30;
            case "SELF_EMPLOYED" -> score += 20;
            case "UNEMPLOYED"    -> score += 0;
        }

        // Income to loan ratio score (max 40 points)
        double monthlyEmi = application.getLoanAmount() / application.getTenureMonths();
        double emiToIncomeRatio = monthlyEmi / user.getMonthlyIncome();

        if (emiToIncomeRatio <= 0.20)      score += 40;
        else if (emiToIncomeRatio <= 0.30) score += 30;
        else if (emiToIncomeRatio <= 0.40) score += 20;
        else if (emiToIncomeRatio <= 0.50) score += 10;
        else                               score += 0;

        // Tenure score (max 30 points)
        if (application.getTenureMonths() <= 12)       score += 30;
        else if (application.getTenureMonths() <= 36)  score += 20;
        else if (application.getTenureMonths() <= 60)  score += 10;
        else                                           score += 0;

        return score;
    }

    public List<LoanApplication> getUserLoans(Long userId) {
        return loanApplicationRepository.findByUserIdOrderByAppliedAtDesc(userId);
    }

    public List<LoanApplication> getLoansByStatus(ApplicationStatus status) {
        return loanApplicationRepository.findByStatus(status);
    }

    public LoanApplication getLoanById(Long id) {
        return loanApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan application not found with id: " + id));
    }

    public List<LoanApplication> getAllLoans() {
        return loanApplicationRepository.findAll();
    }
}