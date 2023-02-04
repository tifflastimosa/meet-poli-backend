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
  private final ObjectMapper mapper = new ObjectMapper();
  private final String apiKey = "X-API-Key";
  private final String apiKeyValue = "mkXnRMzkbiLZ9vc2ocNUqYGd0FuHzroQI1vWxNvr";

  // First API call to get members of a specific chamber and state
  @GetMapping(value = "/{chamber}/{state}/current", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Optional<JsonNode> getMemberByChamberAndState(@PathVariable("chamber") String chamber,
      @PathVariable("state") String state) throws IOException, InterruptedException {
    String url =
        String.format("https://api.propublica.org/congress/v1/members/%s/%s/current.json", chamber,
            state);
    Optional<JsonNode> result = this.apiCallHelper(url);

    if (result.isEmpty()) return result;

    return Optional.ofNullable(mapper.createObjectNode().set("members", result.get()));
  }

  // Second API call to get a specific member
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Optional<JsonNode> getMemberById(@PathVariable("id") String id)
      throws IOException, InterruptedException {
    String url = String.format("https://api.propublica.org/congress/v1/members/%s.json", id);
    Optional<JsonNode> result = this.apiCallHelper(url);

    if (result.isEmpty()) return result;

    return Optional.ofNullable(mapper.createObjectNode().set("member", result.get()));
  }

  // Third API call to get list of bills voted on
  @GetMapping(value = "/{member-id}/votes", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Optional<JsonNode> getMembersVote(@PathVariable("member-id") String memberID)
      throws IOException, InterruptedException {
    String url = String.format("https://api.propublica.org/congress/v1/members/%s/votes.json",
        memberID);
    Optional<JsonNode> result = this.apiCallHelper(url);

    if (result.isEmpty()) return result;

    return Optional.ofNullable(
        mapper.createObjectNode().set("votes", result.get().get(0).get("votes")));
  }

  private Optional<JsonNode> apiCallHelper(String url) throws IOException, InterruptedException {
    HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .setHeader(apiKey, apiKeyValue)
        .setHeader("Accept", "application/json")
        .build();
    HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

    if (res.statusCode() != HttpServletResponse.SC_OK) {
      return Optional.empty();
    }

    JsonNode jsonNode = mapper.readTree(res.body()).get("results");
    return Optional.ofNullable((jsonNode));
  }
}
