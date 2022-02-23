package com.quicktutorials.learnmicroservices.accountmicroservice.utils;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EncryptionUtils {
    
    @Autowired
    BasicTextEncryptor textEncryptor;

    public String encrypt(String data){
        return textEncryptor.encrypt(data);
    }

    public String decrypt(String encryptedData){
        return textEncryptor.decrypt(encryptedData);
    }
}
