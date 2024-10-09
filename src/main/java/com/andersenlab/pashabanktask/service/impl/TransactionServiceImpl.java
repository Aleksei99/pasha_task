package com.andersenlab.pashabanktask.service.impl;

import com.andersenlab.pashabanktask.controller.request.TransactionRequestDto;
import com.andersenlab.pashabanktask.dao.entity.Transaction;
import com.andersenlab.pashabanktask.dao.repository.TransactionRepository;
import com.andersenlab.pashabanktask.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;

  @Override
  @Transactional
  public void createTransaction(TransactionRequestDto transactionRequestDto) {
    Transaction transaction =
        new Transaction(transactionRequestDto.name(), transactionRequestDto.value());
    transactionRepository.save(transaction);
    log.info("Saved transaction to db");
  }
}
