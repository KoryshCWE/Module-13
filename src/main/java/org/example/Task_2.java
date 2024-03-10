package org.example;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.OutputStream;



class JSONPlaceholderAPI {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final String USER_POSTS_ENDPOINT = "/users/%d/posts";
    private static final String POST_COMMENTS_ENDPOINT = "/posts/%d/comments";

    public static void main(String[] args) {
        JSONPlaceholderAPI api = new JSONPlaceholderAPI();

        try {
            // Отримуємо всі коментарі до останнього посту користувача з id = 1
            int userId = 1;
            String userPostsUrl = String.format(BASE_URL + USER_POSTS_ENDPOINT, userId);
            String lastPostId = api.getLastPostId(userPostsUrl);
            String postCommentsUrl = String.format(BASE_URL + POST_COMMENTS_ENDPOINT, Integer.parseInt(lastPostId));
            String commentsJson = api.getComments(postCommentsUrl);

            // Записуємо коментарі у файл
            String fileName = String.format("user-%d-post-%s-comments.json", userId, lastPostId);
            api.writeToFile(fileName, commentsJson);
            System.out.println("Comments saved to file: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLastPostId(String userPostsUrl) throws IOException {
        String response = sendRequest(userPostsUrl, "GET", null);
        JSONArray postsArray = new JSONArray(response);
        JSONObject lastPost = postsArray.getJSONObject(postsArray.length() - 1);
        return String.valueOf(lastPost.getInt("id"));
    }


    public String getComments(String postCommentsUrl) throws IOException {
        return sendRequest(postCommentsUrl, "GET", null);
    }

    public void writeToFile(String fileName, String content) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
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


