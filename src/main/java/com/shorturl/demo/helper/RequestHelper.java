package com.shorturl.demo.helper;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class RequestHelper {

  public static JsonNode reqReurl(String key, String json, String url) {
    try {
      HttpResponse<JsonNode> response = Unirest.post(url).header("Content-Type", "application/json").header("reurl-api-key", key).body(json).asJson();
      return 200 != response.getStatus() ? null : (JsonNode)response.getBody();
    } catch (UnirestException var4) {
      throw new RuntimeException(var4);
    }
  }
}
