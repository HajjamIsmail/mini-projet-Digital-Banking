package com.example.digitalbanking.services;

import com.example.digitalbanking.entities.BankAccount;
import com.example.digitalbanking.entities.CurrentAccount;
import com.example.digitalbanking.entities.SavingAccount;
import com.example.digitalbanking.repositories.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BankService {
    @Autowired
    public BankRepository bankRepository;
    public void consulter(){
        BankAccount bankAccount = bankRepository.findById("4d3d7785-2c7b-474a-a543-bdf9701676b2").orElse(null);
        if (bankAccount != null) {
            System.out.println("***************************");
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCreateAt());
            System.out.println(bankAccount.getCustomer().getName());
            System.out.println(bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CurrentAccount) {
                System.out.println("Over Draft =>" + ((CurrentAccount) bankAccount).getOverDraft());
            } else if (bankAccount instanceof SavingAccount) {
                System.out.println("Rate =>" + ((SavingAccount) bankAccount).getInterestRate());
            }
            bankAccount.getAccountOperations().forEach(op -> {
                System.out.println("===========================");
                System.out.println(op.getType());
                System.out.println(op.getOperationDate());
                System.out.println(op.getAmount());
            });
        }
    }
}
