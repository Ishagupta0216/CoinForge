package com.octro.coinforge.repository;

import com.octro.coinforge.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByPlayerIdOrderByTimestampDesc(String playerId);
    List<Transaction> findTop10ByPlayerIdOrderByTimestampDesc(String playerId);
}