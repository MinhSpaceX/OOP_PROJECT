package com.zeus.utils.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeus.App.App;
import com.zeus.utils.log.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import org.apache.http.HttpConnection;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class APIHandler {
    private static final String api = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    public static MediaPlayer getAudio(String wordTarget) {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api+wordTarget.toLowerCase()))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());
            if (jsonNode.isEmpty() || jsonNode.get(0) == null || jsonNode.get(0).get("phonetics").isEmpty() || jsonNode.get(0).get("phonetics").get(0) == null) throw new Exception("There is no audio for this word, change the word or add new data.");
            String audioURL = jsonNode.get(0).get("phonetics").get(0).get("audio").asText();
            MediaPlayer mediaPlayer = new MediaPlayer(new Media(audioURL));
            mediaPlayer.setOnEndOfMedia(mediaPlayer::play);
            mediaPlayer.setOnPaused(mediaPlayer::play);
            return mediaPlayer;
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
        return null;
    }
}
