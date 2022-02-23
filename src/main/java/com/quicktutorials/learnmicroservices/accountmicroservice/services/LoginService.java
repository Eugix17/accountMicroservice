package com.quicktutorials.learnmicroservices.accountmicroservice.services;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.quicktutorials.learnmicroservices.accountmicroservice.entities.User;
import com.quicktutorials.learnmicroservices.accountmicroservice.utils.UserNotLoggedException;

public interface LoginService {
    Optional<User> getUserFromDbAndVerifyPassword(String id, String password) throws UserNotLoggedException;

    String createJwt(String subject, String name, String permission, Date date) throws UnsupportedEncodingException;

    Map<String, Object> verifyJwtAndGetData(HttpServletRequest request) throws UserNotLoggedException, UnsupportedEncodingException;
}
