package org.example;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

class JSONPlaceholder2API {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final String USERS_ENDPOINT = "/users";

    public static void main(String[] args) {
        JSONPlaceholder2API api = new JSONPlaceholder2API(); // Ось тут виправлено

        try {
            // Створення нового користувача
            String newUserJson = "{\"name\":\"John Doe\",\"username\":\"johndoe\",\"email\":\"johndoe@example.com\"}";
            String createdUserJson = api.createUser(newUserJson);
            System.out.println("Created user: " + createdUserJson);

            // Оновлення користувача
            int userIdToUpdate = 1;
            String updatedUserJson = api.updateUser(userIdToUpdate, "{\"name\":\"Updated Name\",\"username\":\"updatedusername\",\"email\":\"updatedemail@example.com\"}");
            System.out.println("Updated user: " + updatedUserJson);

            // Видалення користувача
            int userIdToDelete = 1;
            int deleteStatusCode = api.deleteUser(userIdToDelete);
            System.out.println("Delete status code: " + deleteStatusCode);

            // Отримання інформації про всіх користувачів
            String allUsersJson = api.getAllUsers();
            System.out.println("All users: " + allUsersJson);

            // Отримання інформації про користувача за id
            int userId = 1;
            String userByIdJson = api.getUserById(userId);
            System.out.println("User by id: " + userByIdJson);

            // Отримання інформації про користувача за username
            String username = "Bret";
            String userByUsernameJson = api.getUserByUsername(username);
            System.out.println("User by username: " + userByUsernameJson);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String createUser(String userJson) throws IOException {
        return sendRequest(USERS_ENDPOINT, "POST", userJson);
    }

    public String updateUser(int userId, String updatedUserJson) throws IOException {
        String updateUserEndpoint = USERS_ENDPOINT + "/" + userId;
        return sendRequest(updateUserEndpoint, "PUT", updatedUserJson);
    }

    public int deleteUser(int userId) throws IOException {
        String deleteUserEndpoint = USERS_ENDPOINT + "/" + userId;
        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + deleteUserEndpoint).openConnection();
        connection.setRequestMethod("DELETE");
        return connection.getResponseCode();
    }

    public String getAllUsers() throws IOException {
        return sendRequest(USERS_ENDPOINT, "GET", null);
    }

    public String getUserById(int userId) throws IOException {
        String getUserByIdEndpoint = USERS_ENDPOINT + "/" + userId;
        return sendRequest(getUserByIdEndpoint, "GET", null);
    }

    public String getUserByUsername(String username) throws IOException {
        String getUserByUsernameEndpoint = USERS_ENDPOINT + "?username=" + username;
        return sendRequest(getUserByUsernameEndpoint, "GET", null);
    }

    private String sendRequest(String urlString, String method, String body) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + urlString).openConnection();
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

