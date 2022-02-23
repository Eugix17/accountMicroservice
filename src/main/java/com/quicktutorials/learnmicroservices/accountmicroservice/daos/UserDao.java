package com.quicktutorials.learnmicroservices.accountmicroservice.daos;

import java.util.Optional;

import com.quicktutorials.learnmicroservices.accountmicroservice.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, String>{
    
    Optional<User> findById(String Id);
}
