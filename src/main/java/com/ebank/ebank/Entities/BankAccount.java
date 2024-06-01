package com.ebank.ebank.Entities;

import com.ebank.ebank.Enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4)
@Data @NoArgsConstructor @AllArgsConstructor
public abstract class BankAccount {
    @Id
    private String id;
    private double balance;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    // relaton ManyToOne : plusieurs comptes peuvent appartenir à 1 seul client
    @ManyToOne
    private Customer customer;
    // relation OneToMany : un compte peut effectuer plusieurs opérations
    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.EAGER)
    private List<Operation> operationList;
}
