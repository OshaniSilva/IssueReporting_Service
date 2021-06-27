package com.example.issuereporting_service.controller;

import com.example.issuereporting_service.model.Issue;
import com.example.issuereporting_service.model.IssueHistory;
import com.example.issuereporting_service.model.State;
import com.example.issuereporting_service.model.User;
import com.example.issuereporting_service.repository.IssueHistoryRepository;
import com.example.issuereporting_service.repository.IssueRepository;
//import com.example.issuereporting_service.repository.StateRepository;
import com.example.issuereporting_service.repository.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping ("/issue")
public class IssueController {

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    IssueHistoryRepository issueHistoryRepository;

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    StateRepository stateRepository;

    @PostMapping("/create")
    public Object cretaeIssue (@RequestHeader("Authorization") String token, @RequestBody Issue issue) throws IOException, InterruptedException, JSONException {

        System.out.println("ISSUEEE");
        System.out.println(issue);

        JSONObject jsonObject = new JSONObject(AuthController.verifyToken(token));

        if (!jsonObject.has("error")) {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            verifyUser(jsonObject);
            System.out.println("FIRST IFFF");

            User user = new User(jsonObject.getString("email"), jsonObject.getString("given_name"));

            Issue _newIssue = issueRepository
                .save(new Issue(issue.getIssueType(), issue.getIssueDescription(), "Open",
                        dtf.format(now), user));

            System.out.println("isuuuuuuuuu");
            System.out.println(_newIssue);

            issueHistoryRepository.save(new IssueHistory("Open",dtf.format(now), _newIssue ));

            return _newIssue;
//            for (User user: allUsers) {
//                if (user.getUserId() == jsonObject.getString("email")) {
//                    issueRepository.save(new Issue(issue.getIssueType(), issue.getIssueDescription(), issue.getIssueState(),
//                            null, new User("osh.silva@gmail.com", "Oshani ")));
//                } else {
////                    userRepository.save(new User(user.getUserId(), user.getUsername()));
//                    issueRepository.save(new Issue(1, issue.getIssueType(), issue.getIssueDescription(),
//                                    issue.getIssueState(), null, new User("osh.silva@gmail.com", "Oshani ")));
//                }
//            }
        } else {
            System.out.println("---Invalid token-----");
            return "Invalid token";
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Issue> updateIssue(@RequestHeader("Authorization") String token, @PathVariable("id") long id, @RequestBody Issue issue) throws JSONException, IOException, InterruptedException {

        System.out.println("edittttttttttttttttttttt");
        JSONObject jsonObject = new JSONObject(AuthController.verifyToken(token));

        verifyUser(jsonObject);

        if (!jsonObject.has("error")) {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            Optional<Issue> issueData = issueRepository.findById(id);

            if (issueData.isPresent()) {
                Issue _issue = issueData.get();

                System.out.println("--------_issue------------");
                System.out.println(_issue.getIssueState());
                System.out.println("========issue=============");
                System.out.println(issue.getIssueState());
                if (!_issue.getIssueState().equals(issue.getIssueState())) {
                    System.out.println("INSIDE IDD");
                    _issue.setIssueState(issue.getIssueState());
                    issueHistoryRepository.save(new IssueHistory(issue.getIssueState(),dtf.format(now), _issue ));
                    issueRepository.save(_issue);
                }
                return new ResponseEntity<>(_issue, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            System.out.println("invalid token");
            return null;
        }
    }


    @GetMapping("/getAllIssues")
    public List<Issue>  getAllIssues (@RequestHeader("Authorization") String token) throws IOException, InterruptedException, JSONException {

        System.out.println("ISSUEEE");
        System.out.println(token);

        JSONObject jsonObject = new JSONObject(AuthController.verifyToken(token));

        verifyUser(jsonObject);

        System.out.println("------1------");

        if (!jsonObject.has("error")) {
            System.out.println("------2------");
            List<Issue> allIssues = issueRepository.findAll();
            System.out.println("------3------ ");
            System.out.println(allIssues);
//            JSONArray allIssuesJson = new JSONArray(allIssues);
//            JSONObject obj = new JSONObject();

//            String json = new Gson().toJson(allIssues );
            return issueRepository.findAll();
        } else {
            System.out.println("---Invalid token-----");
            return null;
        }
    }

    @GetMapping("/getStatusPercentage")
    public List<State> getStateData (@RequestHeader("Authorization") String token) throws IOException, InterruptedException, JSONException {

        System.out.println("getStateData");
        System.out.println(token);
        int openCount = 0;
        int waitingCount = 0;
        int progressCount = 0;
        int resolvedCount = 0;
        double openState, waitingState, progressState, resolvedState = 0;

        JSONObject jsonObject = new JSONObject(AuthController.verifyToken(token));
        verifyUser(jsonObject);

        if (!jsonObject.has("error")) {

            List<Issue> allIssues = issueRepository.findAll();
            System.out.println(allIssues);

            for (Issue issue: allIssues) {
                if (issue.getIssueState().equals("Open")) {
                    ++openCount;
                } else if (issue.getIssueState().equals("Waiting")) {
                    ++waitingCount;
                } else if (issue.getIssueState().equals("Progress")) {
                    ++progressCount;
                } else if (issue.getIssueState().equals("Resolved")) {
                    ++resolvedCount;
                }
            }

            openState = (openCount*100)/allIssues.size();
            waitingState = (waitingCount*100)/allIssues.size();
            progressState = (progressCount*100)/allIssues.size();
            resolvedState = (resolvedCount*100)/allIssues.size();

            List<State> newStates = new ArrayList<>();
            newStates.add(new State(openState,"Open"));
            newStates.add(new State(waitingState,"Waiting"));
            newStates.add(new State(progressState, "Progress"));
            newStates.add(new State(resolvedState, "Resolved"));

//           stateRepository.save(new State(openState,waitingState,progressState,resolvedState));

//            JSONObject obj = new JSONObject();
//
//            obj.put("Open", openState);
//            obj.put("Waiting", waitingState);
//            obj.put("Progress", progressState);
//            obj.put("Resolved", resolvedState);
//
//            System.out.println("==================== "+obj);
//
//            System.out.println(openState);
//            System.out.println(waitingState);
//            System.out.println(progressState);
//            System.out.println(resolvedState);

            return newStates;
        } else {
            System.out.println("---Invalid token-----");
            return null;
        }

    }

    @GetMapping(value = "/getIssuesOfState/{issueState}")
    public List<Issue> getAllIssuesOfState(@RequestHeader("Authorization") String token, @PathVariable String issueState) throws JSONException, IOException, InterruptedException {

        JSONObject jsonObject = new JSONObject(AuthController.verifyToken(token));
        verifyUser(jsonObject);

        if (!jsonObject.has("error")) {
            List<Issue> issueData = issueRepository.findAll();
            List<Issue> issueDataByState = new ArrayList<Issue>();
            for(Issue issue : issueData) {
                if (issue.getIssueState().equals(issueState) ) {
                    issueDataByState.add(issue);
                }
            }
            return issueDataByState;
        } else {
            System.out.println("invalid token");
            return null;
        }
    }

    public void verifyUser(JSONObject tokenVerification) throws JSONException {

//        JSONObject jsonObject = new JSONObject(tokenVerification);
        List<User> allUsers = userRepository.findAll();
        Boolean haveUser = false;

        if (!tokenVerification.has("error")) {
            for (User user: allUsers) {
                System.out.println("-----");
                System.out.println(tokenVerification.getString("email"));
                System.out.println(user.getUserId());
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
