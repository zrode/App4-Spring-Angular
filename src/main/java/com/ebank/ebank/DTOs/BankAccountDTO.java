package com.ebank.ebank.DTOs;

import com.ebank.ebank.Enums.AccountStatus;
import lombok.Data;

import java.util.Date;
@Data
public class BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private String type;
}
