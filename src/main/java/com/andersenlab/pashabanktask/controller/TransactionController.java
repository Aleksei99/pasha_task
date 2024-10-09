package com.andersenlab.pashabanktask.controller;

import com.andersenlab.pashabanktask.controller.request.TransactionRequestDto;
import com.andersenlab.pashabanktask.rabbitmq.RabbitMqMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

  private final RabbitMqMessageProducer rabbitMqMessageProducer;

  @PostMapping
  public ResponseEntity<String> createTransaction(
      @RequestBody TransactionRequestDto transactionRequestDto) {
    rabbitMqMessageProducer.publish(
        transactionRequestDto, "CBAR_FOREIGN_TRANSFER_Exchange", "CBAR_FOREIGN_TRANSFER_Key");
    return ResponseEntity.ok("Ok");
  }
}
