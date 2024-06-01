package com.ebank.ebank;

import com.ebank.ebank.DTOs.BankAccountDTO;
import com.ebank.ebank.DTOs.CurrentAccountDTO;
import com.ebank.ebank.DTOs.CustomerDTO;
import com.ebank.ebank.DTOs.SavingAccountDTO;
import com.ebank.ebank.Entities.*;
import com.ebank.ebank.Enums.AccountStatus;
import com.ebank.ebank.Enums.OperationType;
import com.ebank.ebank.Exceptions.BalanceNotSufficientException;
import com.ebank.ebank.Exceptions.BankAccountNotFoundException;
import com.ebank.ebank.Exceptions.CustomerNotFoundException;
import com.ebank.ebank.Repositories.BankAccountRepository;
import com.ebank.ebank.Repositories.CustomerRepository;
import com.ebank.ebank.Repositories.OperationRepository;
import com.ebank.ebank.Services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankApplication.class, args);
	}

	// On va créer un jeu de données pour l'exemple
	@Bean
	CommandLineRunner start(BankAccountService bankAccountService) { // on injecte le service
		return args -> {
			// On crée 3 clients
			Stream.of("Client_A", "Client_B", "Client_C").forEach(name->{
				CustomerDTO customer = new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				bankAccountService.saveCustomer(customer);
			});

			// On ajoute des comptes à chaque client créé
			bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*70000, 5000, customer.getId());
					bankAccountService.saveSavingBankAccount(Math.random()*70000,4.5, customer.getId());

                } catch (CustomerNotFoundException 	e) {
                    e.printStackTrace();
                }
            });
			// J'ajoute 7 opérations par compte
			List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
			for (BankAccountDTO bankAccount: bankAccounts) {
				for (int i=0 ; i<7 ; i++) {
					String accountId;
					if (bankAccount instanceof SavingAccountDTO) {
						accountId = ((SavingAccountDTO) bankAccount).getId();
					} else {
						accountId = ((CurrentAccountDTO) bankAccount).getId();
					}
					bankAccountService.credit(accountId, 10000+Math.random()*100000, "CREDIT");
					bankAccountService.debit(accountId, 2000+Math.random()*5000, "DEBIT");
				}
			}
		};
	}

}
