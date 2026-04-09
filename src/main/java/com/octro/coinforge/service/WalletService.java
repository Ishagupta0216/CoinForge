package com.octro.coinforge.service;

import com.octro.coinforge.exception.GameException;
import com.octro.coinforge.model.Transaction;
import com.octro.coinforge.model.Wallet;
import com.octro.coinforge.repository.TransactionRepository;
import com.octro.coinforge.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String LEADERBOARD_KEY = "leaderboard";

    public Wallet getWallet(String playerId) {
        return walletRepository.findByPlayerId(playerId)
            .orElseThrow(() -> new GameException("Wallet not found for player"));
    }

    public Wallet creditCoins(String playerId, long amount, String description) {
        if (amount <= 0)
            throw new GameException("Amount must be greater than zero");

        Wallet wallet = getWallet(playerId);
        long balanceBefore = wallet.getCoinBalance();
        wallet.setCoinBalance(balanceBefore + amount);
        wallet.setLastUpdated(LocalDateTime.now());
        walletRepository.save(wallet);

        saveTransaction(playerId, "CREDIT", amount, balanceBefore, wallet.getCoinBalance(), description);
        updateLeaderboard(playerId, wallet.getCoinBalance());

        return wallet;
    }

    public Wallet debitCoins(String playerId, long amount, String description) {
        if (amount <= 0)
            throw new GameException("Amount must be greater than zero");

        Wallet wallet = getWallet(playerId);

        if (wallet.getCoinBalance() < amount)
            throw new GameException("Insufficient coins. Current balance: " + wallet.getCoinBalance());

        if (amount > 10000)
            throw new GameException("Transaction limit exceeded. Max 10,000 coins per transaction");

        long balanceBefore = wallet.getCoinBalance();
        wallet.setCoinBalance(balanceBefore - amount);
        wallet.setLastUpdated(LocalDateTime.now());
        walletRepository.save(wallet);

        saveTransaction(playerId, "DEBIT", amount, balanceBefore, wallet.getCoinBalance(), description);
        updateLeaderboard(playerId, wallet.getCoinBalance());

        return wallet;
    }

    public List<Transaction> getTransactionHistory(String playerId) {
        return transactionRepository.findByPlayerIdOrderByTimestampDesc(playerId);
    }

    public List<Transaction> getRecentTransactions(String playerId) {
        return transactionRepository.findTop10ByPlayerIdOrderByTimestampDesc(playerId);
    }

    private void saveTransaction(String playerId, String type, long amount,
                                  long balanceBefore, long balanceAfter, String description) {
        Transaction transaction = new Transaction();
        transaction.setPlayerId(playerId);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setDescription(description);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    private void updateLeaderboard(String playerId, long coinBalance) {
        redisTemplate.opsForZSet().add(LEADERBOARD_KEY, playerId, coinBalance);
    }
}