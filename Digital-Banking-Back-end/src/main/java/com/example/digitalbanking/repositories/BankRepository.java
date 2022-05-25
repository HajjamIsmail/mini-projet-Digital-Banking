package com.example.digitalbanking.repositories;

import com.example.digitalbanking.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<BankAccount,String> {
}
