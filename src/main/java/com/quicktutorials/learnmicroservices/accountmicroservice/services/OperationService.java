package com.quicktutorials.learnmicroservices.accountmicroservice.services;

import java.util.List;

import com.quicktutorials.learnmicroservices.accountmicroservice.entities.Account;
import com.quicktutorials.learnmicroservices.accountmicroservice.entities.Operation;

public interface OperationService {

    List<Operation> getAllOperationPerAccount(String accountId);

    List<Account> getAllAccountsPerUser(String userId);

    Operation saveOperation(Operation operation);
    
}
