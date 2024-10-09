package com.andersenlab.pashabanktask.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CbarRabbitMQConfig {
  private final String podName;
  private final String cbarForeignTransferQ;
  private final String cbarForeignTransferDlq;
  private final String cbarForeignTransferQKey;
  private final String cbarForeignTransferDlqKey;
  private final String cbarForeignTransferQExchange;
  private final String cbarForeignTransferDlqExchange;

  public CbarRabbitMQConfig(
      @Value("${kubernetes.pod.name}") String podName,
      @Value("${rabbitmq.cbar.foreign-transfer.q}") String cbarForeignTransferQ,
      @Value("${rabbitmq.cbar.foreign-transfer.dlq}") String cbarForeignTransferDlq) {
    this.podName = podName;
    this.cbarForeignTransferQ = cbarForeignTransferQ;
    this.cbarForeignTransferDlq = cbarForeignTransferDlq;
    this.cbarForeignTransferQKey = cbarForeignTransferQ + "_Key";
    this.cbarForeignTransferDlqKey = cbarForeignTransferDlq + "_Key";
    this.cbarForeignTransferQExchange = cbarForeignTransferQ + "_Exchange";
    this.cbarForeignTransferDlqExchange = cbarForeignTransferDlq + "_Exchange";
  }

  @Bean
  public Queue cbarForeignTransferQueue() {
    return QueueBuilder.durable(cbarForeignTransferQ)
        .withArgument("x-dead-letter-exchange", cbarForeignTransferDlqExchange)
        .withArgument("x-dead-letter-routing-key", cbarForeignTransferDlqKey)
        .build();
  }

  @Bean
  public Queue cbarForeignTransferDlq() {
    return QueueBuilder.durable(cbarForeignTransferDlq)
        .withArgument("x-project-key", "SPSP")
        .build();
  }

  @Bean
  public DirectExchange cbarForeignTransferExchange() {
    return new DirectExchange(cbarForeignTransferQExchange);
  }

  @Bean
  public DirectExchange cbarForeignTransferDlExchange() {
    return new DirectExchange(cbarForeignTransferDlqExchange);
  }

  @Bean
  public Binding cbarForeignTransferBinding() {
    return BindingBuilder.bind(cbarForeignTransferQueue())
        .to(cbarForeignTransferExchange())
        .with(cbarForeignTransferQKey);
  }

  @Bean
  public Binding cbarForeignTransferDlqBinding() {
    return BindingBuilder.bind(cbarForeignTransferDlq())
        .to(cbarForeignTransferDlExchange())
        .with(cbarForeignTransferDlqKey);
  }

  @Bean
  public MessageConverter messageConverter(){
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
      SimpleRabbitListenerContainerFactoryConfigurer configurer,
      ConnectionFactory connectionFactory) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setMessageConverter(messageConverter());
    configurer.configure(factory, connectionFactory);
    factory.setConsumerTagStrategy(s -> podName);
    return factory;
  }

  @Bean
  public SmartInitializingSingleton reconfigureCCF(CachingConnectionFactory ccf) {
    return () -> ccf.setConnectionNameStrategy(s -> podName);
  }
}
