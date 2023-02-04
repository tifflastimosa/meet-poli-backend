package com.example.meetpolibackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MemberController {

  // First API call to get members of a specific chamber and state
  @GetMapping("/{chamber}/{state}/current")
  @ResponseBody
  public String getMemberByChamberAndState(@PathVariable("chamber") String chamber,
      @PathVariable("state") String state) {
    return "hello world";
  }

  // Second API call to get a specific member
  @GetMapping("/members/{id}")
  @ResponseBody
  public Integer getMemberById(@PathVariable String id) {
    try {
      return Integer.parseInt(id);
    } catch (NumberFormatException nfe) {
      System.out.println(id + " is not a valid member id");
      throw nfe;
    }
  }

  // Third API call to get list of bills voted on
  @GetMapping("/{member-id}/votes")
  @ResponseBody
  public String getMembersVote(@PathVariable("member-id") String memberID) {
    return "hello world";
  }
}
