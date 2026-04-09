package com.octro.coinforge.repository;

import com.octro.coinforge.model.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface WalletRepository extends MongoRepository<Wallet, String> {
    Optional<Wallet> findByPlayerId(String playerId);
}