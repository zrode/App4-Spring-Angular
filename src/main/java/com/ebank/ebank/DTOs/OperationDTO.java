package com.ebank.ebank.DTOs;

import com.ebank.ebank.Entities.BankAccount;
import com.ebank.ebank.Enums.OperationType;
import lombok.Data;
import java.util.Date;

@Data
public class OperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
}
