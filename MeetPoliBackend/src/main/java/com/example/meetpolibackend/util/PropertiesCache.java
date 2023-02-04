package com.example.meetpolibackend.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class PropertiesCache {
  private final Properties properties = new Properties();

  private PropertiesCache() {
    InputStream in = this.getClass().getClassLoader().getResourceAsStream("application.properties");
    try {
      properties.load(in);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static class Loader {
    private static final PropertiesCache INSTANCE = new PropertiesCache();
  }

  public static PropertiesCache getInstance() {
    return Loader.INSTANCE;
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  public Set<String> getAllPropertyNames() {
    return properties.stringPropertyNames();
  }

  public boolean containsKey(String key) {
    return properties.containsKey(key);
  }
}
