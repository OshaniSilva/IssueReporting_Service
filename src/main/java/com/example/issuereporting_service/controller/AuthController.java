package com.example.issuereporting_service.controller;

import org.json.JSONException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/* This class is used to validate the token the client sends in the Header
 *  If token is valid a success user object is sent from the oauth2.googleapis
 *  If invalid an error json is sent */
public class AuthController {

    public static String verifyToken(String token) throws IOException, NullPointerException, JSONException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://oauth2.googleapis.com/tokeninfo?id_token=" + token))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
