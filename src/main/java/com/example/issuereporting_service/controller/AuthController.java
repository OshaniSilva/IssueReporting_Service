package com.example.issuereporting_service.controller;

import com.example.issuereporting_service.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class AuthController {
    public static String verifyToken(String token) throws IOException, InterruptedException, JSONException {
//        System.out.println("INSIDEEEE VALIDATE TOKEN  " +token);
        HttpClient client = HttpClient.newHttpClient();
//        System.out.println("--------------1-----------------");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://oauth2.googleapis.com/tokeninfo?id_token="+token))
                .build();
//        System.out.println("--------------2----------------- " + request);
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString()); //HANDLE EXCEPTION
//        System.out.println("--------------3-----------------");

        return response.body();
    }
}
