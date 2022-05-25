package com.example.digitalbanking;

import com.example.digitalbanking.dtos.BankAccountDTO;
import com.example.digitalbanking.dtos.CurrentBankAccountDTO;
import com.example.digitalbanking.dtos.CustomerDTO;
import com.example.digitalbanking.dtos.SavingBankAccountDTO;
import com.example.digitalbanking.entities.*;
import com.example.digitalbanking.enums.AccountStatus;
import com.example.digitalbanking.enums.OperationType;
import com.example.digitalbanking.exception.BalanceNotSufficientException;
import com.example.digitalbanking.exception.BankAccountNotFoundException;
import com.example.digitalbanking.exception.CustomerNotFoundException;
import com.example.digitalbanking.repositories.AccountOperationRepository;
import com.example.digitalbanking.repositories.BankRepository;
import com.example.digitalbanking.repositories.CustomerRepository;
import com.example.digitalbanking.services.BankAccountService;
import com.example.digitalbanking.services.BankAccountServiceImpl;
import com.example.digitalbanking.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingApplication {
    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            //bankService.consulter();
            Stream.of("AAAA","BBBB","CCCC").forEach(name->{
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                bankAccountService.saveCustomer(customer);
            });
            bankAccountService.listCustomer().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*90000,9000,customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*120000,5.5, customer.getId());

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }

            });
            List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
            for(BankAccountDTO bankAccount:bankAccounts){
                for (int i = 0; i < 10; i++) {
                    String accountId;
                    if(bankAccount instanceof SavingBankAccountDTO){
                        accountId = ((SavingBankAccountDTO) bankAccount).getId();
                    }else{
                        accountId=((CurrentBankAccountDTO) bankAccount).getId();
                    }
                    try {
                        bankAccountService.credit(accountId,10000+Math.random()*120000,"Credit");
                    } catch (BalanceNotSufficientException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        bankAccountService.debit(accountId,1000+Math.random()*9000,"Debit");
                    } catch (BalanceNotSufficientException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
    }

    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankRepository bankRepository,
                            AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("AAAA", "BBBB", "CCCC").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random() * 90000);
                currentAccount.setCreateAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random() * 90000);
                savingAccount.setCreateAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankRepository.save(savingAccount);
            });
            bankRepository.findAll().forEach(acc -> {
                for (int i = 0; i < 10; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random() * 12000);
                    accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }

            });
        };
    }
}
