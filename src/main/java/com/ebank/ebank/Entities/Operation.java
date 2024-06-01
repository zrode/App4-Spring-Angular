package com.ebank.ebank.Entities;

import com.ebank.ebank.Enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Operation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date operationDate;
    private double amount;
    @Enumerated(EnumType.STRING)
    private OperationType type;
    // relation ManyToOne
    @ManyToOne
    private BankAccount bankAccount;
    private String description;
}
