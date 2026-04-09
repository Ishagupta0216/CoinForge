package com.octro.coinforge.controller;

import com.octro.coinforge.dto.request.SimulateRequest;
import com.octro.coinforge.model.Wallet;
import com.octro.coinforge.service.RewardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RewardController {

    private final RewardService rewardService;

    @PostMapping("/daily-bonus/{playerId}")
    public ResponseEntity<Wallet> claimDailyBonus(@PathVariable String playerId) {
        return ResponseEntity.ok(rewardService.claimDailyBonus(playerId));
    }

    @PostMapping("/simulate/win")
    public ResponseEntity<Wallet> simulateWin(@Valid @RequestBody SimulateRequest request) {
        return ResponseEntity.ok(rewardService.simulateWin(request.getPlayerId(), request.getBetAmount()));
    }

    @PostMapping("/simulate/loss")
    public ResponseEntity<Wallet> simulateLoss(@Valid @RequestBody SimulateRequest request) {
        return ResponseEntity.ok(rewardService.simulateLoss(request.getPlayerId(), request.getBetAmount()));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<Object[]>> getLeaderboard() {
        return ResponseEntity.ok(rewardService.getLeaderboard());
    }
}