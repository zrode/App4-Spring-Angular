package com.ebank.ebank.Repositories;

import com.ebank.ebank.Entities.BankAccount;
import com.ebank.ebank.Entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
