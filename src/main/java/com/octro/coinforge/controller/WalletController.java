package com.octro.coinforge.controller;

import com.octro.coinforge.dto.request.WalletRequest;
import com.octro.coinforge.model.Transaction;
import com.octro.coinforge.model.Wallet;
import com.octro.coinforge.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/{playerId}")
    public ResponseEntity<Wallet> getWallet(@PathVariable String playerId) {
        return ResponseEntity.ok(walletService.getWallet(playerId));
    }

    @PostMapping("/credit")
    public ResponseEntity<Wallet> credit(@Valid @RequestBody WalletRequest request) {
        return ResponseEntity.ok(
            walletService.creditCoins(request.getPlayerId(), request.getAmount(), request.getDescription())
        );
    }

    @PostMapping("/debit")
    public ResponseEntity<Wallet> debit(@Valid @RequestBody WalletRequest request) {
        return ResponseEntity.ok(
            walletService.debitCoins(request.getPlayerId(), request.getAmount(), request.getDescription())
        );
    }

    @GetMapping("/transactions/{playerId}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable String playerId) {
        return ResponseEntity.ok(walletService.getTransactionHistory(playerId));
    }

    @GetMapping("/transactions/{playerId}/recent")
    public ResponseEntity<List<Transaction>> getRecentTransactions(@PathVariable String playerId) {
        return ResponseEntity.ok(walletService.getRecentTransactions(playerId));
    }
}