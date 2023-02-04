package com.example.meetpolibackend.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("members")
public class MemberController {

  private final HttpClient client = HttpClient.newHttpClient();

  // First API call to get members of a specific chamber and state
  @GetMapping(value = "/{chamber}/{state}/current", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Optional<JsonNode> getMemberByChamberAndState(@PathVariable("chamber") String chamber,
      @PathVariable("state") String state) throws IOException, InterruptedException {
    ObjectMapper mapper = new ObjectMapper();
    String url =
        "https://api.propublica.org/congress/v1/members/" + chamber + "/" + state + "/current.json";
    HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .setHeader("X-API-Key", "mkXnRMzkbiLZ9vc2ocNUqYGd0FuHzroQI1vWxNvr")
        .setHeader("Accept", "application/json")
        .build();
    HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

    if (res.statusCode() != HttpServletResponse.SC_OK) {
      return Optional.empty();
    }
    JsonNode jsonNode = mapper.readTree(res.body());
    JsonNode array = jsonNode.get("results");
    return Optional.ofNullable(mapper.createObjectNode().set("members", array));
  }

  // Second API call to get a specific member
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Optional<JsonNode> getMemberById(@PathVariable("id") String id)
      throws IOException, InterruptedException {
    ObjectMapper mapper = new ObjectMapper();
    String url = "https://api.propublica.org/congress/v1/members/" + id + ".json";
    HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .setHeader("X-API-Key", "mkXnRMzkbiLZ9vc2ocNUqYGd0FuHzroQI1vWxNvr")
        .setHeader("Accept", "application/json")
        .build();
    HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

    if (res.statusCode() != HttpServletResponse.SC_OK) {
      return Optional.empty();
    }

    JsonNode jsonNode = mapper.readTree(res.body());
    JsonNode array = jsonNode.get("results");
    return Optional.ofNullable(mapper.createObjectNode().set("member", array));
  }

  // Third API call to get list of bills voted on
  @GetMapping(value = "/{member-id}/votes", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Optional<JsonNode> getMembersVote(@PathVariable("member-id") String memberID)
      throws IOException, InterruptedException {
    ObjectMapper mapper = new ObjectMapper();
    String url = "https://api.propublica.org/congress/v1/members/" + memberID + "/votes.json";
    HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .setHeader("X-API-Key", "mkXnRMzkbiLZ9vc2ocNUqYGd0FuHzroQI1vWxNvr")
        .setHeader("Accept", "application/json")
        .build();
    HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

    if (res.statusCode() != HttpServletResponse.SC_OK) {
      return Optional.empty();
    }

    JsonNode jsonNode = mapper.readTree(res.body());
    JsonNode array = jsonNode.get("results").get(0).get("votes");
    return Optional.ofNullable(mapper.createObjectNode().set("votes", array));
  }
}
