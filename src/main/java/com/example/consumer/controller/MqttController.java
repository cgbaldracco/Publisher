package com.example.consumer.controller;

import com.example.consumer.config.MqttCajitaDeMensajes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MqttController {

  @Qualifier("mqttInputChannel")
  @Autowired
  private MessageChannel mqttInboundChannel;

  @Autowired
  private MqttCajitaDeMensajes mqttCajitaDeMensajes;

  @GetMapping("/publish")
  public String publish(@RequestParam String message) {
    mqttInboundChannel.send(MessageBuilder.withPayload(message).setHeader("mqtt_topic", "test/topic").build());
    return "Mensaje publicado: " + message;
  }

  @GetMapping("/ultimoMensaje")
  public String ultimoMensaje() {
    return "estoy entrando acá " + mqttCajitaDeMensajes.getLastMessage();
  }
}
