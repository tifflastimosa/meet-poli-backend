package com.example.meetpolibackend.controller;

import com.example.meetpolibackend.util.PropertiesCache;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TweetController {

  private final HttpClient client = HttpClient.newHttpClient();

  public Optional<String> getUserIdByUsername(String username)
    throws IOException, InterruptedException {
    ObjectMapper om = new ObjectMapper();
    HttpRequest req = HttpRequest.newBuilder()
      .uri(URI.create(String.format("https://api.twitter.com/2/users/by/username/%s", username)))
      .setHeader("Authorization",
        String.format("Bearer %s", PropertiesCache.getInstance().getProperty("TWITTER_API_TOKEN")))
      .setHeader("Accept", "application/json")
      .build();

    HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

    if (res.statusCode() != HttpServletResponse.SC_OK) {
      return Optional.empty();
    }

    JsonNode json = om.readTree(res.body());
    return Optional.of(json.get("data").get("id").asText());
  }

  @GetMapping(value = "/timeline/{handle}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getMemberTimeline(@PathVariable String handle)
    throws IOException, InterruptedException {
    Optional<String> userId = getUserIdByUsername(handle);

    if (userId.isEmpty()) {
      return ResponseEntity.badRequest().body(String.format("Handle: %s is invalid", handle));
    }

    HttpRequest req = HttpRequest.newBuilder()
      .uri(URI.create(
        String.format(
          "https://api.twitter.com/2/users/%s/tweets?max_results=100&expansions=author_id&tweet.fields=id,text,created_at&user.fields=name,username,profile_image_url",
          userId.get())))
      .setHeader("Authorization",
        String.format("Bearer %s", PropertiesCache.getInstance().getProperty("TWITTER_API_TOKEN")))
      .setHeader("Accept", "application/json")
      .build();

    HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

    if (res.statusCode() != HttpServletResponse.SC_OK) {
      return ResponseEntity.badRequest()
        .body(String.format("Could not retrieve timeline for %s", handle));
    }

    ObjectMapper om = new ObjectMapper();
    JsonNode json = om.readTree(res.body());

    return ResponseEntity.ok(json.toPrettyString());
  }
}
