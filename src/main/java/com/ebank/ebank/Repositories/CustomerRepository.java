package com.ebank.ebank.Repositories;

import com.ebank.ebank.Entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("select c from Customer c where c.name like:mc")
    List<Customer> searchCustomers(@Param("mc") String keyword);
    //List<Customer> findByNameContains(String keyword);
}
