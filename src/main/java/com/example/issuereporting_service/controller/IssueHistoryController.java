package com.example.issuereporting_service.controller;

import com.example.issuereporting_service.model.IssueHistory;
import com.example.issuereporting_service.model.User;
import com.example.issuereporting_service.repository.IssueHistoryRepository;
import com.example.issuereporting_service.repository.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/history")
public class IssueHistoryController {

    @Autowired
    IssueHistoryRepository issueHistoryRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/issue/{issueId}")
    public List<IssueHistory> getHistoryOfIssue(@RequestHeader("Authorization") String token, @PathVariable long issueId) throws JSONException, IOException, InterruptedException {

        JSONObject jsonObject = new JSONObject(AuthController.verifyToken(token));
        verifyUser(jsonObject);

        if (!jsonObject.has("error")) {
            List<IssueHistory> issueHistoryData = issueHistoryRepository.findAll();
            List<IssueHistory> historyDataByIssue = new ArrayList<IssueHistory>();
            for(IssueHistory issueHistory : issueHistoryData) {
                if (issueHistory.issue.getId() == issueId ) {
                    historyDataByIssue.add(issueHistory);
                }
            }
            return historyDataByIssue;
        } else {
            System.out.println("invalid token");
            return null;
        }
    }

    public void verifyUser(JSONObject tokenVerification) throws JSONException {

        List<User> allUsers = userRepository.findAll();
        Boolean haveUser = false;

        if (!tokenVerification.has("error")) {
            for (User user: allUsers) {
                if (user.getUserId().equals(tokenVerification.getString("email"))) {
                    System.out.println("---user in db-----");
                    haveUser = true;
                }
            }
            if (haveUser == false) {
                System.out.println("---created new user-----");
                userRepository.save(new User(tokenVerification.getString("email"), tokenVerification.getString("given_name")));
            }
        }
    }


}
