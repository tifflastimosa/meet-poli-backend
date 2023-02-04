package com.example.meetpolibackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
public class Author {

  private String name;
  private String username;
  private String profileImageUrl;

  public Author(
    @JsonProperty("name") String name,
    @JsonProperty("username") String username,
    @JsonProperty("profile_image_url") String profileImageUrl) {
    this.name = name;
    this.username = username;
    this.profileImageUrl = profileImageUrl;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public void setProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  @Override
  public String toString() {
    return "Author{" +
      "name='" + name + '\'' +
      ", username='" + username + '\'' +
      ", profileImageUrl='" + profileImageUrl + '\'' +
      '}';
  }
}
