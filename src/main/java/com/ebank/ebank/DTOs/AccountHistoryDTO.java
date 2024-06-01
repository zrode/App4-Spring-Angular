package com.ebank.ebank.DTOs;

import lombok.Data;

import java.util.List;
@Data
public class AccountHistoryDTO {
    private String accountId;
    private double balance;
    private List<OperationDTO> accountOperationDTOS;
    // attributs pour la pagination
    private int currentPage;
    private int totalPages;
    private int size; // nombre d'éléments par page
}
