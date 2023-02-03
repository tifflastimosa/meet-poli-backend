package com.example.meetpolibackend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MemberController {

  @GetMapping("/members")
  @ResponseBody
  public String getMembersList() {
    return "hello world";
  }

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
}
