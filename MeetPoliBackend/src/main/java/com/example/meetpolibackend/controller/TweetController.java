package com.example.meetpolibackend.controller;

import com.example.meetpolibackend.model.Author;
import com.example.meetpolibackend.model.Tweet;
import com.example.meetpolibackend.util.PropertiesCache;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TweetController {

  private final HttpClient client = HttpClient.newHttpClient();
  private final ObjectMapper om = new ObjectMapper();

  public Optional<String> getUserIdByUsername(String username)
    throws IOException, InterruptedException {
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
  public ResponseEntity<JsonNode> getMemberTimeline(@PathVariable String handle)
    throws IOException, InterruptedException {
    Optional<String> userId = getUserIdByUsername(handle);

    if (userId.isEmpty()) {
      return ResponseEntity.badRequest()
        .body(om.createObjectNode().put("error", String.format("Handle: %s is invalid", handle)));
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
        .body(om.createObjectNode()
          .put("error", String.format("Could not retrieve timeline for %s", handle)));
    }

    JsonNode root = om.readTree(res.body());
    JsonNode tweetsNode = root.path("data");
    JsonNode authorNode = root.path("includes").path("users").path(0);

    List<Tweet> tweets = om.readerForListOf(Tweet.class).readValue(tweetsNode);
    Author author = om.readerFor(Author.class).readValue(authorNode);

    ObjectNode result = om.createObjectNode();
    tweets.forEach(tweet -> result.withArray("tweets").addPOJO(tweet));
    result.putPOJO("author", author);

    return ResponseEntity.ok(result);
  }
}
