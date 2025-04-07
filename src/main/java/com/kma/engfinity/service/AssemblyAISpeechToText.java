package com.kma.engfinity.service;



import org.springframework.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AssemblyAISpeechToText {

  private static final String API_KEY = "7d35c06c7b4d49e482bff9ac6d48481c";
  private static final String UPLOAD_URL = "https://api.assemblyai.com/v2/upload";
  private static final String TRANSCRIBE_URL = "https://api.assemblyai.com/v2/transcript";

  public static String uploadAudio(String fileUrl) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", API_KEY);
    headers.setContentType(MediaType.APPLICATION_JSON);

    Map<String, String> requestBody = new HashMap<>();
    requestBody.put("audio_url", fileUrl);

    HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

    ResponseEntity<Map> response = restTemplate.exchange(
        TRANSCRIBE_URL,
        HttpMethod.POST,
        requestEntity,
        Map.class
    );

    if (response.getStatusCode() == HttpStatus.OK) {
      return response.getBody().get("id").toString();
    } else {
      throw new RuntimeException("Failed to upload audio.");
    }
  }

  public static String getTranscription(String transcriptId) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", API_KEY);

    HttpEntity<String> requestEntity = new HttpEntity<>(headers);

    String url = TRANSCRIBE_URL + "/" + transcriptId;
    int maxRetries = 10;
    int delayMs = 3000;

    for (int i = 0; i < maxRetries; i++) {
      ResponseEntity<Map> response = restTemplate.exchange(
          url,
          HttpMethod.GET,
          requestEntity,
          Map.class
      );

      if (response.getStatusCode() == HttpStatus.OK) {
        Map<String, Object> body = response.getBody();
        String status = (String) body.get("status");

        if ("completed".equalsIgnoreCase(status)) {
          return body.get("text").toString();
        } else if ("error".equalsIgnoreCase(status)) {
          String error = (String) body.get("error");
          throw new RuntimeException("Transcription failed with error."+error);
        }
      }

      try {
        Thread.sleep(delayMs);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException("Thread interrupted while waiting for transcription");
      }
    }

    throw new RuntimeException("Transcription not completed in time.");
  }
  public static String submitAudio(String audioUrl) {
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", API_KEY);
    headers.setContentType(MediaType.APPLICATION_JSON);

    Map<String, Object> body = new HashMap<>();
    body.put("audio_url", audioUrl);

    HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

    ResponseEntity<Map> response = restTemplate.postForEntity(
        TRANSCRIBE_URL, request, Map.class
    );

    if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
      return response.getBody().get("id").toString();
    } else {
      throw new RuntimeException("Failed to submit audio for transcription.");
    }
  }
  public static String transcribeAudio(String audioUrl) {
    String transcriptId = submitAudio(audioUrl);
    return getTranscription(transcriptId);
  }

}
