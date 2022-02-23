package com.quicktutorials.learnmicroservices.accountmicroservice.services;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.quicktutorials.learnmicroservices.accountmicroservice.daos.UserDao;
import com.quicktutorials.learnmicroservices.accountmicroservice.entities.User;
import com.quicktutorials.learnmicroservices.accountmicroservice.utils.EncryptionUtils;
import com.quicktutorials.learnmicroservices.accountmicroservice.utils.JwtUtils;
import com.quicktutorials.learnmicroservices.accountmicroservice.utils.UserNotLoggedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    UserDao userDao;

    @Autowired
    EncryptionUtils encryptionUtils;

    @Override
    public Optional<User> getUserFromDbAndVerifyPassword(String id, String password) throws UserNotLoggedException{
        Optional<User> userr = userDao.findById(id);
        if(userr.isPresent()){
            User user = userr.get();
            if(encryptionUtils.decrypt(user.getPassword()).equals(password)){
                log.info("Username and Password verified");
            } else{
                log.info("Username verified. Password not");
                throw new UserNotLoggedException("User not correctly logged in");
            }
        }
        return userr;
    }

    @Override
    public String createJwt(String subject, String name, String permission, Date dateNow) throws UnsupportedEncodingException{
        Date expDate = dateNow;
        expDate.setTime(dateNow.getTime() + (300*1000));
        log.info("JWT Creation. Expiration time: " + expDate.getTime());
        String token = JwtUtils.generateJwt(subject, expDate, name, permission);
        return token;
    }

    @Override
    public Map<String, Object> verifyJwtAndGetData(HttpServletRequest request) throws UserNotLoggedException, UnsupportedEncodingException {
        String jwt = JwtUtils.getJwtFromHttpRequest(request);
        if (jwt == null){
            throw new UserNotLoggedException("Authentication token not found in the request");
        } else {
            Map<String, Object> userData = JwtUtils.jwt2Map(jwt);
            return userData;
        }
    }
}
