package com.zeus.utils.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeus.utils.log.Logger;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class APIHandler {
    private static final String api = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    private static final String translateAPI = "https://api.mymemory.translated.net/get?";

    public static MediaPlayer getAudio(String wordTarget) {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api + wordTarget.toLowerCase()))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());
            if (jsonNode.isEmpty() || jsonNode.get(0) == null || jsonNode.get(0).get("phonetics").isEmpty() || jsonNode.get(0).get("phonetics").get(0) == null)
                throw new Exception("There is no audio for this word, change the word or add new data.");
            String audioURL = jsonNode.get(0).get("phonetics").get(0).get("audio").asText();
            MediaPlayer mediaPlayer = new MediaPlayer(new Media(audioURL));
            mediaPlayer.setOnEndOfMedia(mediaPlayer::play);
            mediaPlayer.setOnPaused(mediaPlayer::play);
            return mediaPlayer;
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(translate("hello, my name is Hung, i'm 10 years old"));
    }

    public static List<String> translate(String english) {
        List<String> translates = new ArrayList<>();
        String url = translateAPI +
                "q=" +
                URLEncoder.encode(english, StandardCharsets.UTF_8) +
                "&langpair=en" +
                URLEncoder.encode("|", StandardCharsets.UTF_8) +
                "vi";
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());
            for (JsonNode node : jsonNode.get("matches")) {
                translates.add(node.get("translation").toString().replace("\\n", "\n").replace("\"", ""));
            }
            return translates;
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
        return null;
    }
}