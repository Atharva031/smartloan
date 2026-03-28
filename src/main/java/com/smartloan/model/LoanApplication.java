package com.smartloan.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "loan_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LoanApplication 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @NotNull(message = "Loan amount is required")
    @Min(value = 1000, message = "Minimum loan amount is 1000")
    @Column(nullable = false)
    private Double loanAmount;

    @NotNull(message = "Tenure is required")
    @Min(value = 1, message = "Minimum tenure is 1 month")
    @Max(value = 360, message = "Maximum tenure is 360 months")
    @Column(nullable = false)
    private Integer tenureMonths;

    @NotBlank(message = "Purpose is required")
    @Column(nullable = false)
    private String purpose;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @Column 
    private Integer creditScore;

    @Column
    private String remarks;

    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedAt;

    @Column
    private LocalDateTime reviewedAt;

    @PrePersist
    protected void onCreate()
    {
        appliedAt = LocalDateTime.now();
        status = ApplicationStatus.SUBMITTED;
    }

    public enum ApplicationStatus
    {
        SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED
    }
}
