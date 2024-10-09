package com.andersenlab.pashabanktask.service;

import com.andersenlab.pashabanktask.controller.request.TransactionRequestDto;

public interface TransactionService {
    void createTransaction(TransactionRequestDto transactionRequestDto);
}
