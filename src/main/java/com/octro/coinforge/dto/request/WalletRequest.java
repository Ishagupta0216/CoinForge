package com.octro.coinforge.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WalletRequest {

    @NotBlank(message = "Player ID is required")
    private String playerId;

    @Min(value = 1, message = "Amount must be at least 1")
    private long amount;

    private String description;
}