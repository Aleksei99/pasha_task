package com.andersenlab.pashabanktask.controller.request;

import java.math.BigDecimal;

public record TransactionRequestDto(String name, BigDecimal value) {}
