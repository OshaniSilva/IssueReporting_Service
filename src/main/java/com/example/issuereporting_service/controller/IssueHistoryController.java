package com.example.issuereporting_service.controller;

import com.example.issuereporting_service.model.IssueHistory;
import com.example.issuereporting_service.model.User;
import com.example.issuereporting_service.repository.IssueHistoryRepository;
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
@RequestMapping("/history")
public class IssueHistoryController {

    static final Logger log = LoggerFactory.getLogger(IssueHistoryController.class);

    @Autowired
    IssueHistoryRepository issueHistoryRepository;

    @Autowired
    UserRepository userRepository;

    /*  This endpoint is called when user gets history of an issue
     *  RequestHeader: Includes auth token for checking user validity
     *  PathVariable: Issue id is sent */
    @GetMapping(value = "/issue/{issueId}")
    public ResponseEntity<List<IssueHistory>> getHistoryOfIssue(@RequestHeader("Authorization") String token, @PathVariable long issueId) throws JSONException, IOException, InterruptedException {

        try {
            // Token is sent to AuthController to be verified
            JSONObject verifiedAuthObject = new JSONObject(AuthController.verifyToken(token));

            // If user is not in db new user is created in user table
            verifyUser(verifiedAuthObject);

            // If token is valid client is given access get the history of an issue
            if (!verifiedAuthObject.has("error")) {
                List<IssueHistory> issueHistoryData = issueHistoryRepository.findAll();
                List<IssueHistory> historyDataByIssue = new ArrayList<IssueHistory>();
                for (IssueHistory issueHistory : issueHistoryData) {
                    if (issueHistory.issue.getId() == issueId) {
                        historyDataByIssue.add(issueHistory);
                    }
                }
            //return historyDataByIssue;
                return new ResponseEntity<>(historyDataByIssue, HttpStatus.OK);
            } else {
                String errorMsg = "Invalid token";
                log.info(errorMsg);
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        } catch (IOException ioException) {
            String errorMsg = "IOException in getting all issues: " + ioException;
            log.info(errorMsg);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InterruptedException interruptedException) {
            String errorMsg = "InterruptedException in getting all issues: " + interruptedException;
            log.info(errorMsg);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JSONException jsonException) {
            String errorMsg = "Invalid json error in getting all issues: " + jsonException;
            log.info(errorMsg);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NullPointerException nullPointerException) {
            String errorMsg = "Null pointer : " + nullPointerException;
            log.info(errorMsg);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception exception) {
            String errorMsg = "Exception in get issues: " + exception;
            log.info(errorMsg);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // This method is used to register new users in the db
    public void verifyUser(JSONObject tokenVerification) throws JSONException {
        try {
            List<User> allUsers = userRepository.findAll();
            Boolean haveUser = false;

            // If token is valid new users are saved
            if (!tokenVerification.has("error")) {
                for (User user : allUsers) {
                    if (user.getUserId().equals(tokenVerification.getString("email"))) {
                        haveUser = true;
                    }
                }
                if (haveUser == false) {
                    userRepository.save(new User(tokenVerification.getString("email"), tokenVerification.getString("given_name")));
                }
            }
        } catch (JSONException jsonException) {
            String errorMsg = "Invalid json error in saving new user: " + jsonException;
            log.info(errorMsg);
        } catch (Exception exception) {
            String errorMsg = "Exception in saving new user: " + exception;
            log.info(errorMsg);
        }
    }
}
