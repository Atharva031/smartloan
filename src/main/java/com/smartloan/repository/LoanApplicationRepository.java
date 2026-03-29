package com.smartloan.repository;

import com.smartloan.model.LoanApplication;
import com.smartloan.model.LoanApplication.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long>
{
    List<LoanApplication> findByUserId(Long userId);
    List<LoanApplication> findByStatus(ApplicationStatus status);
    List<LoanApplication> findByUserIdOrderByAppliedAtDesc(Long userId);
}
