package com.octro.coinforge.service;

import com.octro.coinforge.dto.request.PlayerRequest;
import com.octro.coinforge.exception.GameException;
import com.octro.coinforge.model.Player;
import com.octro.coinforge.model.Wallet;
import com.octro.coinforge.repository.PlayerRepository;
import com.octro.coinforge.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final WalletRepository walletRepository;

    public Player registerPlayer(PlayerRequest request) {
        if (playerRepository.existsByUsername(request.getUsername()))
            throw new GameException("Username already taken");
        if (playerRepository.existsByEmail(request.getEmail()))
            throw new GameException("Email already registered");

        Player player = new Player();
        player.setUsername(request.getUsername());
        player.setEmail(request.getEmail());
        Player saved = playerRepository.save(player);

        Wallet wallet = new Wallet();
        wallet.setPlayerId(saved.getId());
        walletRepository.save(wallet);

        return saved;
    }

    public Player getPlayer(String id) {
        return playerRepository.findById(id)
            .orElseThrow(() -> new GameException("Player not found"));
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }
}