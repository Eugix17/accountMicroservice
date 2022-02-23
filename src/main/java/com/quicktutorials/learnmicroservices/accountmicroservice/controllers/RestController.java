package com.quicktutorials.learnmicroservices.accountmicroservice.controllers;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.quicktutorials.learnmicroservices.accountmicroservice.entities.Operation;
import com.quicktutorials.learnmicroservices.accountmicroservice.entities.User;
import com.quicktutorials.learnmicroservices.accountmicroservice.services.LoginService;
import com.quicktutorials.learnmicroservices.accountmicroservice.services.OperationService;
import com.quicktutorials.learnmicroservices.accountmicroservice.utils.UserNotLoggedException;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    //private static final Logger log = LoggerFactory.getLogger(RestController.class);

    @Autowired
    LoginService loginService;

    @Autowired
    OperationService operationService;

    @RequestMapping("/hello")
    public String sayHello(){
        return "Hello everyone!";
    }

    @RequestMapping("/newuser1")
    public String addUser(User user){
        return "User added correctly:" + user.getId() + ", " + user.getUsername();
    }

    @RequestMapping("/newuser2")
    public String addUserValid(@Valid User user){
        return "User added correctly:" + user.getId() + ", " + user.getUsername();
    }

    @RequestMapping("/newuser3")
    public String addUserValidPlusBinding(@Valid User user, BindingResult result){
        if(result.hasErrors()){
            return result.toString();
        }
        return "User added correctly:" + user.getId() + ", " + user.getUsername();
    }

    @RequestMapping("/newuser4")
    public String addUserValidPlusBinding2(User user, BindingResult result){
        UserValidator userValidator = new UserValidator();
        userValidator.validate(user, result);
        if(result.hasErrors()){
            return result.toString();
        }
        return "User added correctly:" + user.getId() + ", " + user.getUsername();
    }

    private class UserValidator implements Validator {
        @Override
        public boolean supports(Class<?> clazz) {
            return User.class.equals(clazz);
        }

        @Override
        public void validate(Object obj, Errors errors) {
            User user = (User) obj;
            if (user.getPassword().length() < 8) {
                errors.rejectValue("password", "the password must be at least 8 chars long!");
            }
        }
    }

    @AllArgsConstructor
    public class JsonResponseBody{
        @Getter @Setter
        private int server;
        @Getter @Setter
        private Object response;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<JsonResponseBody> loginUser(@RequestParam(value = "id") String id, @RequestParam(value="password") String pwd){
        try{
            Optional<User> userr = loginService.getUserFromDbAndVerifyPassword(id, pwd);
            if(userr.isPresent()){
                User user = userr.get();
                String jwt = loginService.createJwt(user.getId(), user.getUsername(), user.getPermission(), new Date());
                return ResponseEntity.status(HttpStatus.OK).header("jwt", jwt).body(new JsonResponseBody(HttpStatus.OK.value(), "Success! User logged in!"));
            }
        }catch (UserNotLoggedException e1){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "Login failed! Wrong credentials " + e1.toString()));
        } catch (UnsupportedEncodingException e2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "Token error!" + e2.toString()));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "No corrispondence in the database of user"));
    }

    @RequestMapping("/operations/account/{account}")
    public ResponseEntity<JsonResponseBody> fetchAllOperationPerAccount(HttpServletRequest request, @PathVariable(name = "account") String account){
        try {
            loginService.verifyJwtAndGetData(request);
            return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseBody(HttpStatus.OK.value(), operationService.getAllOperationPerAccount(account)));
        } catch (UnsupportedEncodingException e1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "Unsupported Encoding: " + e1.toString()));
        } catch (UserNotLoggedException e2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "User not correctly logged: " + e2.toString()));
        }catch (ExpiredJwtException e3) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(new JsonResponseBody(HttpStatus.GATEWAY_TIMEOUT.value(), "Session expired!: " + e3.toString()));
        }
    }

    @RequestMapping(value = "/accounts/user", method = RequestMethod.POST)
    public ResponseEntity<JsonResponseBody> fetchAllAccountsPerUser(HttpServletRequest request){
        try {
            Map<String, Object> userData = loginService.verifyJwtAndGetData(request);
            return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseBody(HttpStatus.OK.value(), operationService.getAllAccountsPerUser((String)userData.get("subject"))));
        } catch (UnsupportedEncodingException e1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseBody(HttpStatus.BAD_REQUEST.value(), "Bad Request: " + e1.toString()));
        } catch (UserNotLoggedException e2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "User not logged! Login first!: " + e2.toString()));
        } catch (ExpiredJwtException e3) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(new JsonResponseBody(HttpStatus.OK.value(), "Session expired!: " + e3.toString()));
    }
}

    @RequestMapping(value = "/operations/add", method = RequestMethod.POST)
    public ResponseEntity<JsonResponseBody> addOperation(HttpServletRequest request, @Valid Operation operation, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "Error! Invalid format of data."));
        }
        try {
            loginService.verifyJwtAndGetData(request);
            return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseBody((HttpStatus.OK.value()), operationService.saveOperation(operation)));
        } catch (UnsupportedEncodingException e1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseBody(HttpStatus.BAD_REQUEST.value(), "Bad Request: " + e1.toString()));
        } catch (UserNotLoggedException e2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "User not logged! Login first!: " + e2.toString()));
        } catch (ExpiredJwtException e3) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(new JsonResponseBody(HttpStatus.OK.value(), "Session expired!: " + e3.toString()));
        }   
    }
}
