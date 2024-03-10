package org.example;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.OutputStream;

class JSONPlaceholder3API {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final String USER_TODOS_ENDPOINT = "/users/%d/todos";

    public static void main(String[] args) {
        JSONPlaceholder3API api = new JSONPlaceholder3API(); // Use JSONPlaceholder3API instead of JSONPlaceholderAPI

        try {
            // Отримуємо всі відкриті задачі для користувача з ідентифікатором 1
            int userId = 1;
            String userTodosUrl = String.format(BASE_URL + USER_TODOS_ENDPOINT, userId);
            api.printOpenTodos(userTodosUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void printOpenTodos(String userTodosUrl) throws IOException {
        String response = sendRequest(userTodosUrl, "GET", null);
        try (Scanner scanner = new Scanner(response)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("\"completed\": false")) {
                    System.out.println(line);
                }
            }
        }
    }

    private String sendRequest(String urlString, String method, String body) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        if (body != null) {
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }

        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(connection.getInputStream())) {
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        }

        return response.toString();
    }
}

