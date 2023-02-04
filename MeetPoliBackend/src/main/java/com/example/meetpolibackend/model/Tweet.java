package com.example.meetpolibackend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
public class Tweet {

  private String id;
  private String createdAt;
  private String text;

  @JsonCreator
  public Tweet(
    @JsonProperty("id") String id,
    @JsonProperty("created_at") String createdAt,
    @JsonProperty("text") String text) {
    this.id = id;
    this.createdAt = createdAt;
    this.text = text;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return "Tweet{" +
      "id='" + id + '\'' +
      ", createdAt='" + createdAt + '\'' +
      ", text='" + text + '\'' +
      '}';
  }
}
