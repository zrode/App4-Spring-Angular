package com.ebank.ebank.DTOs;

import com.ebank.ebank.Enums.AccountStatus;
import lombok.Data;

import java.util.Date;

@Data
public class CurrentAccountDTO extends BankAccountDTO{

    private double overDraft;
}
