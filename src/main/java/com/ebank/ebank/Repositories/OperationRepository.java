package com.ebank.ebank.Repositories;

import com.ebank.ebank.Entities.Customer;
import com.ebank.ebank.Entities.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    //List<Operation> findByBankAccountId(String accountId);
    Page<Operation> findByBankAccountId(String accountId, Pageable pageable);
}
