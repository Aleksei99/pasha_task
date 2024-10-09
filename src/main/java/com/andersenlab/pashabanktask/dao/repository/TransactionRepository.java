package com.andersenlab.pashabanktask.dao.repository;

import com.andersenlab.pashabanktask.dao.entity.Transaction;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {}
