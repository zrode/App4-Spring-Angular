package com.ebank.ebank.Services;

import com.ebank.ebank.DTOs.*;
import com.ebank.ebank.Entities.*;
import com.ebank.ebank.Enums.OperationType;
import com.ebank.ebank.Exceptions.BalanceNotSufficientException;
import com.ebank.ebank.Exceptions.BankAccountNotFoundException;
import com.ebank.ebank.Exceptions.CustomerNotFoundException;
import com.ebank.ebank.Mappers.BankAccountMapperImpl;
import com.ebank.ebank.Repositories.BankAccountRepository;
import com.ebank.ebank.Repositories.CustomerRepository;
import com.ebank.ebank.Repositories.OperationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private OperationRepository operationRepository;
    private BankAccountMapperImpl mapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer");
        // On suppose qu'il n'y a aucune règle métier et que l'on sauvegarde directement le client
        Customer customer = mapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return mapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null); // s'il ne trouve pas, il attribuera la valeur null
        if(customer==null)
            throw new CustomerNotFoundException("Customer not found");
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        CurrentAccount savedCurrentAccount = bankAccountRepository.save(currentAccount);
        return mapper.fromCurrentAccount(currentAccount);
    }

    public SavingAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null); // s'il ne trouve pas, il attribuera la valeur null
        if(customer==null)
            throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setRate(interestRate);
        SavingAccount savedSavingAccount = bankAccountRepository.save(savingAccount);
        return mapper.fromSavingAccount(savedSavingAccount);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        /* Voici la façon impérative :
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for (Customer customer:customers) {
            CustomerDTO customerDTO = mapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        }
        return customerDTOS;
        */
        // la façon fonctionnelle grâce au Stream
        List<CustomerDTO> collect = customers.stream().map(customer -> mapper.fromCustomer(customer)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("BankAccount not found!!!")); // s'il n'existe pas on met null
        if (bankAccount instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return mapper.fromSavingAccount(savingAccount);
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return mapper.fromCurrentAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("BankAccount not found!!!"));
        if (bankAccount.getBalance() < amount)
            throw new BalanceNotSufficientException("Balance not sufficient!!");
        Operation operation = new Operation();
        operation.setType(OperationType.DEBIT);
        operation.setAmount(amount);
        operation.setOperationDate(new Date());
        operation.setDescription(description);
        operation.setBankAccount(bankAccount);
        operationRepository.save(operation);
        // Après avoir créé l'opération, je met à jour le solde du compte et j'enregistre
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("BankAccount not found!!!"));
        Operation operation = new Operation();
        operation.setType(OperationType.CREDIT);
        operation.setAmount(amount);
        operation.setOperationDate(new Date());
        operation.setDescription(description);
        operation.setBankAccount(bankAccount);
        operationRepository.save(operation);
        // Après avoir créé l'opération, je met à jour le solde du compte et j'enregistre
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource, amount, "Transfert to "+accountIdDestination);
        credit(accountIdDestination, amount, "Transfert from "+accountIdSource);
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return mapper.fromSavingAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return mapper.fromCurrentAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(()->new CustomerNotFoundException("Customer not found"));
        return mapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer");
        // On suppose qu'il n'y a aucune règle métier et que l'on sauvegarde directement le client
        Customer customer = mapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return mapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public AccountHistoryDTO accountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount==null) throw new BankAccountNotFoundException("Account not found");

        Page<Operation> operations = operationRepository.findByBankAccountId(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<OperationDTO> operationDTOS = operations.getContent().stream().map(operation -> mapper.fromOperation(operation)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(operationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setSize(size);
        accountHistoryDTO.setTotalPages(operations.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers = customerRepository.searchCustomers(keyword);
        List<CustomerDTO> customerDTOS = customers.stream().map(cust -> mapper.fromCustomer(cust)).collect(Collectors.toList());
        return customerDTOS;
    }
}
