package com.example.meetpolibackend.model;

public class Member {

  private String name;
  private String id;
  private String state;
  private String party;
  private String twitterHandle;

  public Member(String name, String id, String state, String party, String twitterHandle) {
    this.name = name;
    this.id = id;
    this.state = state;
    this.party = party;
    this.twitterHandle = twitterHandle;
  }

  public String getName() {
    return this.name;
  }

  public String getId() {
    return this.id;
  }

  public String getState() {
    return this.state;
  }

  public String getParty() {
    return this.party;
  }

  public String getTwitterHandle() {
    return this.twitterHandle;
  }
}
