package com.example.issuereporting_service.controller;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthController {

    static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public static String verifyToken(String token) throws IOException, InterruptedException, JSONException {
        try {
            System.out.println("-----token---- "+token);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://oauth2.googleapis.com/tokeninfo?id_token="+token))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); //HANDLE EXCEPTION
            System.out.println("===========1===================");
            return response.body();
        } catch (IOException ioException) {
            String errorMsg = "IOException OAuth2: " + ioException;
            log.info(errorMsg);
            System.out.println("=======================");
            System.out.println(ioException);
            return null;
        } catch (InterruptedException interruptedException) {
            String errorMsg = "InterruptedException OAuth2: " + interruptedException;
            log.info(errorMsg);
            return null;
        } catch (NullPointerException nullPointerException) {
            String errorMsg = "Null pointer OAuth2: " + nullPointerException;
            log.info(errorMsg);
            return null;
        } catch (Exception exception) {
            String errorMsg = "Exception OAuth2: " + exception;
            log.info(errorMsg);
            return null;
        }


//        catch (Exception e) {
//            String errorMsg = "OAuth2 validation error.";
//            log.info(errorMsg);
//            return null;
//        }
    }
}
