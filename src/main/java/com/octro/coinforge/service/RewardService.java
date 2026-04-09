package com.octro.coinforge.service;

import com.octro.coinforge.exception.GameException;
import com.octro.coinforge.model.Transaction;
import com.octro.coinforge.model.Wallet;
import com.octro.coinforge.repository.TransactionRepository;
import com.octro.coinforge.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.data.redis.core.RedisTemplate;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final WalletService walletService;
    private final TransactionRepository transactionRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final long DAILY_BONUS = 500;
    private static final long WIN_REWARD = 200;
    private static final long LOSS_PENALTY = 100;
    private static final String LEADERBOARD_KEY = "leaderboard";

    public Wallet claimDailyBonus(String playerId) {
        String bonusKey = "daily_bonus:" + playerId;
        Boolean alreadyClaimed = redisTemplate.hasKey(bonusKey);

        if (Boolean.TRUE.equals(alreadyClaimed))
            throw new GameException("Daily bonus already claimed. Come back tomorrow!");

        Wallet wallet = walletService.creditCoins(playerId, DAILY_BONUS, "Daily login bonus");

        redisTemplate.opsForValue().set(bonusKey, "claimed");
        redisTemplate.expireAt(bonusKey,
            java.util.Date.from(LocalDateTime.now()
                .plusDays(1)
                .toInstant(java.time.ZoneOffset.UTC)));

        return wallet;
    }

    public Wallet simulateWin(String playerId, long betAmount) {
        if (betAmount <= 0)
            throw new GameException("Bet amount must be greater than zero");

        walletService.debitCoins(playerId, betAmount, "Bet placed");
        long winAmount = betAmount + WIN_REWARD;
        return walletService.creditCoins(playerId, winAmount, "Win reward");
    }

    public Wallet simulateLoss(String playerId, long betAmount) {
        if (betAmount <= 0)
            throw new GameException("Bet amount must be greater than zero");

        return walletService.debitCoins(playerId, betAmount, "Game loss");
    }

    public List<Object[]> getLeaderboard() {
        Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple<String>> topPlayers =
            redisTemplate.opsForZSet().reverseRangeWithScores(LEADERBOARD_KEY, 0, 9);

        return topPlayers.stream()
            .map(entry -> new Object[]{entry.getValue(), entry.getScore()})
            .toList();
    }
}