package com.andersenlab.pashabanktask.rabbitmq;

import com.andersenlab.pashabanktask.controller.request.TransactionRequestDto;
import com.andersenlab.pashabanktask.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMqMessageConsumer {

    private final TransactionService transactionService;

    @RabbitListener(queues = "${rabbitmq.cbar.foreign-transfer.q}")
    public void consume(TransactionRequestDto transactionRequestDto){
        log.info("Consumed from queue {}", transactionRequestDto);
        transactionService.createTransaction(transactionRequestDto);
    }

}
