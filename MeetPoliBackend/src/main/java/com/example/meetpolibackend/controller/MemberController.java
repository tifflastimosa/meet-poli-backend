package com.example.meetpolibackend.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class MemberController {

  private final HttpClient client = HttpClient.newHttpClient();

  // First API call to get members of a specific chamber and state
  @GetMapping(value = "/{chamber}/{state}/current", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public String getMemberByChamberAndState(@PathVariable("chamber") String chamber,
      @PathVariable("state") String state) throws IOException, InterruptedException {
    String url =
        "https://api.propublica.org/congress/v1/members/" + chamber + "/" + state + "/current.json";
    HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .setHeader("X-API-Key", "mkXnRMzkbiLZ9vc2ocNUqYGd0FuHzroQI1vWxNvr")
        .setHeader("Accept", "application/json")
        .build();
    HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

    if (res.statusCode() != HttpServletResponse.SC_OK) {
      return "error";
    }
    return res.body();
  }

  // Second API call to get a specific member
  @GetMapping("/members/{id}")
  public ResponseEntity<Integer> getMemberById(@PathVariable String id) {
    try {
      return new ResponseEntity<>(Integer.parseInt(id), HttpStatus.OK);
    } catch (NumberFormatException nfe) {
      System.out.println(id + " is not a valid member id");
      return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
  }

  // Third API call to get list of bills voted on
  @GetMapping("/{member-id}/votes")
  @ResponseBody
  public String getMembersVote(@PathVariable("member-id") String memberID) {
    return "hello world";
  }
}
