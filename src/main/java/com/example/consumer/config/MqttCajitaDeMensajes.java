package com.example.consumer.config;

import org.springframework.stereotype.Component;

@Component

public class MqttCajitaDeMensajes {

  private String lastMessage;

  public String getLastMessage() {
    return lastMessage;
  }

  public void setLastMessage(String lastMessage) {
    this.lastMessage = lastMessage;
  }
}
