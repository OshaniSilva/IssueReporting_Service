package com.example.issuereporting_service.controller;

import com.example.issuereporting_service.model.User;
import com.example.issuereporting_service.repository.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/user")
public class UserController {

    static final Logger log = LoggerFactory.getLogger(IssueHistoryController.class);

    @Autowired
    UserRepository userRepository;

    /*  This endpoint is called to create new users
     *  RequestHeader: Includes auth token for checking user validity
     *  RequestBody: User object is sent here  */
    @PostMapping("/create")
    public ResponseEntity<Object> loginUser(@RequestHeader("Authorization") String token, @RequestBody User user) {

        try {
            // Token is sent to AuthController to be verified
            JSONObject verifiedAuthObject = new JSONObject(AuthController.verifyToken(token));

            // If token is valid client is given access create a new user
            if (!verifiedAuthObject.has("error")) {
                User _user = userRepository
                        .save(new User(user.getUserId(), user.getUsername()));
                return new ResponseEntity<>(_user, HttpStatus.CREATED);
            } else {
                String errorMsg = "Invalid token";
                log.info(errorMsg);
                return new ResponseEntity<>(errorMsg, HttpStatus.UNAUTHORIZED);
            }
        } catch (IOException ioException) {
            String errorMsg = "IOException in creating user: " + ioException;
            log.info(errorMsg);
            return new ResponseEntity<>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InterruptedException interruptedException) {
            String errorMsg = "InterruptedException in creating user: " + interruptedException;
            log.info(errorMsg);
            return new ResponseEntity<>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JSONException jsonException) {
            String errorMsg = "Invalid json error in creating user: " + jsonException;
            log.info(errorMsg);
            return new ResponseEntity<>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NullPointerException nullPointerException) {
            String errorMsg = "Null pointer in creating user: " + nullPointerException;
            log.info(errorMsg);
            return new ResponseEntity<>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception exception) {
            String errorMsg = "Exception in creating user: " + exception;
            log.info(errorMsg);
            return new ResponseEntity<>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //  This endpoint is called to get all users
    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = new ArrayList<User>();
            userRepository.findAll().forEach(users::add);
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}