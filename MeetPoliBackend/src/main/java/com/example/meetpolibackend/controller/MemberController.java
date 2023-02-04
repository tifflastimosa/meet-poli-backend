package com.example.meetpolibackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MemberController {

  @GetMapping("/members")
  public ResponseEntity<String> getMembersList() {
    return new ResponseEntity<>("hello world", HttpStatus.OK);
  }

  @GetMapping("/members/{id}")
  public ResponseEntity<Integer> getMemberById(@PathVariable String id) {
    try {
      return new ResponseEntity<>(Integer.parseInt(id), HttpStatus.OK);
    } catch (NumberFormatException nfe) {
      System.out.println(id + " is not a valid member id");
      return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
  }
}
