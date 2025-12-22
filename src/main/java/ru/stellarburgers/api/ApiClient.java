package ru.stellarburgers.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {

    private static final String BASE_URL = "https://stellarburgers.education-services.ru/api";
    private static final String AUTH_TOKEN_KEY = "accessToken";

    private final HttpClient httpClient;
    private final Gson gson;

    public ApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    /**
     * Создает нового пользователя через API
     */
    public String registerUser(String email, String password, String name) throws IOException, InterruptedException {
        String url = BASE_URL + "/auth/register";
        
        JsonObject body = new JsonObject();
        body.addProperty("email", email);
        body.addProperty("password", password);
        body.addProperty("name", name);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
            return jsonResponse.get(AUTH_TOKEN_KEY).getAsString();
        } else {
            throw new RuntimeException("Не удалось создать пользователя: " + response.body());
        }
    }

    /**
     * Удаляет пользователя через API
     */
    public void deleteUser(String token) throws IOException, InterruptedException {
        String url = BASE_URL + "/auth/user";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", token)
                .DELETE()
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Вход пользователя через API
     */
    public String loginUser(String email, String password) throws IOException, InterruptedException {
        String url = BASE_URL + "/auth/login";

        JsonObject body = new JsonObject();
        body.addProperty("email", email);
        body.addProperty("password", password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
            return jsonResponse.get(AUTH_TOKEN_KEY).getAsString();
        } else {
            throw new RuntimeException("Не удалось войти: " + response.body());
        }
    }
}





