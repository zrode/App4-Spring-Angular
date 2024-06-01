package com.ebank.ebank.Services;

import com.ebank.ebank.DTOs.*;
import com.ebank.ebank.Entities.BankAccount;
import com.ebank.ebank.Entities.CurrentAccount;
import com.ebank.ebank.Entities.Customer;
import com.ebank.ebank.Entities.SavingAccount;
import com.ebank.ebank.Exceptions.BalanceNotSufficientException;
import com.ebank.ebank.Exceptions.BankAccountNotFoundException;
import com.ebank.ebank.Exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer (CustomerDTO customerDTO);
    CurrentAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccountDTO> bankAccountList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    AccountHistoryDTO accountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);
}
