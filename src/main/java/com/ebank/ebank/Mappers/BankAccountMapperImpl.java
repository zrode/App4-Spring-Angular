package com.ebank.ebank.Mappers;

import com.ebank.ebank.DTOs.CurrentAccountDTO;
import com.ebank.ebank.DTOs.CustomerDTO;
import com.ebank.ebank.DTOs.OperationDTO;
import com.ebank.ebank.DTOs.SavingAccountDTO;
import com.ebank.ebank.Entities.CurrentAccount;
import com.ebank.ebank.Entities.Customer;
import com.ebank.ebank.Entities.Operation;
import com.ebank.ebank.Entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {

    // De Customer vers CustomerDTO
    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        //customerDTO.setId(customer.getId());
        //customerDTO.setName(customer.getName());
        //customerDTO.setEmail(customer.getEmail());
        // Au lieu de procéder de manière statique, on peut simplifier en utilisant la façon suivante :
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    // De CustomerDTO vers Customer
    public Customer fromCustomerDTO (CustomerDTO customerDTO) {
        Customer customer = new Customer();
        //customer.setId(customerDTO.getId());
        //customer.setName(customerDTO.getName());
        //customer.setEmail(customerDTO.getEmail());
        // Idem, on simplifie comme suit :
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    /*
    ---------------------------------------------------------------------
     */

    // De SavingAccount vers SavingAccountDTO
    public SavingAccountDTO fromSavingAccount(SavingAccount savingAccount) {
        SavingAccountDTO savingAccountDTO = new SavingAccountDTO();
        // Il ne va transmettre que les attributs. Il faut donc ajouter une ligne pour ajouter Customer
        BeanUtils.copyProperties(savingAccount, savingAccountDTO);
        // Ici, on prend Customer de savingAccount qu'on transfert vers CustomerDTO via la méthode fromCustomer() que l'on affecte à savingAccountDTO via le setter
        savingAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        savingAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingAccountDTO;
    }

    // De SavingAccountDTO vers SavingAccount
    public SavingAccount fromSavingAccountDTO(SavingAccountDTO savingAccountDTO) {
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingAccountDTO, savingAccount);
        savingAccount.setCustomer(fromCustomerDTO(savingAccountDTO.getCustomerDTO()));
        return savingAccount;
    }

    /*
    ---------------------------------------------------------------------
     */

    // De CurrentAccount vers CurrentAccountDTO
    public CurrentAccountDTO fromCurrentAccount(CurrentAccount currentAccount) {
        CurrentAccountDTO currentAccountDTO = new CurrentAccountDTO();
        BeanUtils.copyProperties(currentAccount, currentAccountDTO);
        currentAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        currentAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentAccountDTO;
    }

    // De CurrentAccountDTO vers CurrentAccount
    public CurrentAccount fromCurrentAccountDTO(CurrentAccountDTO currentAccountDTO) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentAccountDTO, currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(currentAccountDTO.getCustomerDTO()));
        return currentAccount;
    }

    /*
    ---------------------------------------------------------------------
     */

    public OperationDTO fromOperation(Operation operation) {
        OperationDTO operationDTO = new OperationDTO();
        BeanUtils.copyProperties(operation, operationDTO);
        return operationDTO;
    }
}
