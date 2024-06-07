package com.example.consumer.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import java.util.UUID;

@Configuration
public class MqttConfig {
  @Value("${spring.mqtt.url}")
  private String mqttUrl;

  @Value("${spring.mqtt.username}")
  private String username;

  @Value("${spring.mqtt.password}")
  private String password;

  @Bean
  public MqttPahoClientFactory mqttClientFactory() {
    DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
    MqttConnectOptions options = new MqttConnectOptions();
    options.setServerURIs(new String[]{mqttUrl});
    options.setUserName(username);
    options.setPassword(password.toCharArray());
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    factory.setConnectionOptions(options);
    return factory;
  }

  @Bean
  public MessageChannel mqttInputChannel() {
    return new DirectChannel();
  }

  @Bean
  public MqttPahoMessageDrivenChannelAdapter inbound(MqttPahoClientFactory mqttClientFactory) {
    String clientId = UUID.randomUUID().toString();
    MqttPahoMessageDrivenChannelAdapter adapter =
        new MqttPahoMessageDrivenChannelAdapter(clientId, mqttClientFactory, "test/topic");
    adapter.setCompletionTimeout(5000);
    adapter.setConverter(new DefaultPahoMessageConverter());
    adapter.setQos(1);
    adapter.setOutputChannel(mqttInputChannel());
    return adapter;
  }

  @Bean
  @ServiceActivator(inputChannel = "mqttInputChannel")
  public MessageHandler handler() {
    return message -> {
      System.out.println("Mensaje recibido: " + message.getPayload());
    };
  }

  @Bean
  @ServiceActivator(inputChannel = "mqttFakeInputChannel")
  public MessageHandler handlerCajita(MqttCajitaDeMensajes cajitaDeMensajes) {
    return message -> {
      String recibido = message.getPayload().toString();
      cajitaDeMensajes.setLastMessage(recibido);
      System.out.println("Mensaje recibido: " + recibido);
    };
  }

}
