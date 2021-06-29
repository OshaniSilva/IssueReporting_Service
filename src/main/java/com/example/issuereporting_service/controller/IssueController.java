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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    IssueHistoryRepository issueHistoryRepository;

    @Autowired
    UserRepository userRepository;

    /* This endpoint is called when issue creation is done by user
    *  RequestHeader: Includes auth token for checking user validity
    *  RequestBody: Issue details are included  */
    @PostMapping("/create")
    public ResponseEntity<Issue> cretaeIssue (@RequestHeader("Authorization") String token, @RequestBody Issue issue) throws IOException, InterruptedException, JSONException {
        try {

            // Token is sent to AuthController to be verified
            JSONObject verifiedAuthObject = new JSONObject(AuthController.verifyToken(token));

            // If token is valid client is given access for issue creation
            if (!verifiedAuthObject.has("error")) {

                // Issue creation time
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime presentTime = LocalDateTime.now();

                // If user is not in db new user is created in user table
                findUserInDB(verifiedAuthObject);

                // User object is created with current user information for mapping issue to user table
                User user = new User(verifiedAuthObject.getString("email"), verifiedAuthObject.getString("given_name"));

                // New issue is saved in db
                Issue _newIssue = issueRepository
                        .save(new Issue(issue.getIssueType(), issue.getIssueDescription(), "Open", dtf.format(presentTime), user));
                issueHistoryRepository.save(new IssueHistory("Open",dtf.format(presentTime), _newIssue ));

                return new ResponseEntity<>(_newIssue, HttpStatus.OK);
                //return _newIssue;
            } else {
                String errorMsg = "Invalid token";
                log.info(errorMsg);
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        } catch (IOException ioException) {
            String errorMsg = "IOException in issue creation: " + ioException;
            log.info(errorMsg);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InterruptedException interruptedException) {
            String errorMsg = "InterruptedException in issue creation: " + interruptedException;
            log.info(errorMsg);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JSONException jsonException) {
            String errorMsg = "Invalid json error: " + jsonException;
            log.info(errorMsg);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NullPointerException nullPointerException) {
            String errorMsg = "Null pointer: " + nullPointerException;
            log.info(errorMsg);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception exception) {
            String errorMsg = "Exception in issue creation: " + exception;
            log.info(errorMsg);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*  This endpoint is called when user edits an issue
     *  RequestHeader: Includes auth token for checking user validity
     *  PathVariable: Issue id is sent
     *  RequestBody: Updated issue details are included  */
    @PutMapping("/edit/{id}")
    public ResponseEntity<Issue> updateIssue(@RequestHeader("Authorization") String token, @PathVariable("id") long id, @RequestBody Issue issue) throws JSONException, IOException, InterruptedException {

        try {

            // Token is sent to AuthController to be verified
            JSONObject verifiedAuthObject = new JSONObject(AuthController.verifyToken(token));

            // If user is not in db new user is created in user table
            findUserInDB(verifiedAuthObject);

            // If token is valid client is given access to update issue
            if (!verifiedAuthObject.has("error")) {

                // Issue updated time
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime presentTime = LocalDateTime.now();

                // Relevant issue is queried for in the db
                Optional<Issue> issueData = issueRepository.findById(id);

                // If issue is available user update is done
                if (issueData.isPresent()) {

                    // If user updated state is not the same as previous state in db the issue state is updated
                    Issue _issue = issueData.get();

                    // If token is valid client is given access to update issue
                    if (!_issue.getIssueState().equals(issue.getIssueState())) {

                        _issue.setIssueState(issue.getIssueState());

                        // A new record is inserted into the issueHistory table with the updated time
                        issueHistoryRepository.save(new IssueHistory(issue.getIssueState(),dtf.format(presentTime), _issue ));

                        // The state is updated in relavant issue
                        issueRepository.save(_issue);
                    }
                    return new ResponseEntity<>(_issue, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                String errorMsg = "Invalid token";
                log.info(errorMsg);
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        } catch (IOException ioException) {
            String errorMsg = "IOException in issue update: " + ioException;
            log.info(errorMsg);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InterruptedException interruptedException) {
            String errorMsg = "InterruptedException in issue update: " + interruptedException;
            log.info(errorMsg);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JSONException jsonException) {
            String errorMsg = "Invalid json error in issue update: " + jsonException;
            log.info(errorMsg);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NullPointerException nullPointerException) {
            String errorMsg = "Null pointer : " + nullPointerException;
            log.info(errorMsg);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception exception) {
            String errorMsg = "Exception in issue update: " + exception;
            log.info(errorMsg);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /*  This endpoint is called for getting all issue
     *  RequestHeader: Includes auth token for checking user validity  */
    @GetMapping("/getAllIssues")
    public ResponseEntity<List<Issue>>  getAllIssues (@RequestHeader("Authorization") String token) throws IOException, InterruptedException, JSONException {
        try {
            System.out.println("hereee");

            // Token is sent to AuthController to be verified
            JSONObject verifiedAuthObject = new JSONObject(AuthController.verifyToken(token));

            // If user is not in db new user is created in user table
            findUserInDB(verifiedAuthObject);

            System.out.println("get all isuesss");

            // If token is valid client is given access to get all issues
            if (!verifiedAuthObject.has("error")) {
                return new ResponseEntity<>(issueRepository.findAll(), HttpStatus.OK);
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

    /*  This endpoint is called to get issue percentages
     *  RequestHeader: Includes auth token for checking user validity */
    @GetMapping("/getStatusPercentage")
    public ResponseEntity<List<State>> getStateData (@RequestHeader("Authorization") String token) throws IOException, InterruptedException, JSONException {

        // Issue state percentages are calculated here
        try {

            int openCount = 0;
            int waitingCount = 0;
            int progressCount = 0;
            int resolvedCount = 0;
            double openState, waitingState, progressState, resolvedState = 0;

            // Token is sent to AuthController to be verified
            JSONObject verifiedAuthObject = new JSONObject(AuthController.verifyToken(token));

            // If user is not in db new user is created in user table
            findUserInDB(verifiedAuthObject);

            // If token is valid client is given access to get issue percentages
            if (!verifiedAuthObject.has("error")) {

                List<Issue> allIssues = issueRepository.findAll();

                // Count of issue state are taken
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

                // Percentages calculated
                openState = (openCount*100)/allIssues.size();
                waitingState = (waitingCount*100)/allIssues.size();
                progressState = (progressCount*100)/allIssues.size();
                resolvedState = (resolvedCount*100)/allIssues.size();

                List<State> newStates = new ArrayList<>();
                newStates.add(new State(openState,"Open"));
                newStates.add(new State(waitingState,"Waiting"));
                newStates.add(new State(progressState, "Progress"));
                newStates.add(new State(resolvedState, "Resolved"));

                //  return newStates;
                return new ResponseEntity<>(newStates, HttpStatus.OK);
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

    /*  This endpoint is called to get issue list of a particular state
     *  RequestHeader: Includes auth token for checking user validity
     *  PathVariable: state is sent here  */
    @GetMapping(value = "/getIssuesOfState/{issueState}")
    public ResponseEntity<List<Issue>> getAllIssuesOfState(@RequestHeader("Authorization") String token, @PathVariable String issueState) throws JSONException, IOException, InterruptedException {

        try {

            // Token is sent to AuthController to be verified
            JSONObject verifiedAuthObject = new JSONObject(AuthController.verifyToken(token));

            // If user is not in db new user is created in user table
            findUserInDB(verifiedAuthObject);

            // If token is valid client is given access to get issue list by state
            if (!verifiedAuthObject.has("error")) {
                List<Issue> issueData = issueRepository.findAll();
                List<Issue> issueDataByState = new ArrayList<Issue>();
                for(Issue issue : issueData) {
                    if (issue.getIssueState().equals(issueState) ) {
                        issueDataByState.add(issue);
                    }
                }
                //return issueDataByState;
                return new ResponseEntity<>(issueDataByState, HttpStatus.OK);
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
    public void findUserInDB(JSONObject tokenVerification) throws JSONException {

        try {
            List<User> allUsers = userRepository.findAll();
            Boolean haveUser = false;

            // If token is valid new users are saved
            if (!tokenVerification.has("error")) {
                for (User user: allUsers) {
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
