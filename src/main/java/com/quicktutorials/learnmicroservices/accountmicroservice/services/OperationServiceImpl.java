package com.quicktutorials.learnmicroservices.accountmicroservice.services;

import java.util.Date;
import java.util.List;

import com.quicktutorials.learnmicroservices.accountmicroservice.daos.AccountDao;
import com.quicktutorials.learnmicroservices.accountmicroservice.daos.OperationDao;
import com.quicktutorials.learnmicroservices.accountmicroservice.entities.Account;
import com.quicktutorials.learnmicroservices.accountmicroservice.entities.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class OperationServiceImpl implements OperationService {

    @Autowired
    AccountDao accountDao;

    @Autowired
    OperationDao operationDao;

    @Override
    public List<Operation> getAllOperationPerAccount(String accountId) {
        return operationDao.findAllOperationByAccount(accountId);
    }

    @Override
    public List<Account> getAllAccountsPerUser(String userId) {
        return accountDao.getAllAccountsPerUser(userId);
    }

    @Override
    public Operation saveOperation(Operation operation) {
        if(operation.getDate() == null){
            operation.setDate(new Date());
        }
        return operationDao.save(operation);
    }
    
}
