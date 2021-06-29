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

    @PostMapping("/create")
    public ResponseEntity<User> loginUser(@RequestHeader("Authorization") String token, @RequestBody User user) throws IOException, InterruptedException, JSONException {

        try {
            // Token is sent to AuthController to be verified
            JSONObject verifiedAuthObject = new JSONObject(AuthController.verifyToken(token));

            if (!verifiedAuthObject.has("error")) {
                System.out.println("INSISEEE USER");
                User _user = userRepository
                        .save(new User(user.getUserId(), user.getUsername()));
                return new ResponseEntity<>(_user, HttpStatus.CREATED);
            } else {
                String errorMsg = "Invalid token";
                log.info(errorMsg);
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception exception) {
            String errorMsg = "Exception in get issues: " + exception;
            log.info(errorMsg);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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