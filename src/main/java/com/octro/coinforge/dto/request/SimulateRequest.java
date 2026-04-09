package com.octro.coinforge.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SimulateRequest {

    @NotBlank(message = "Player ID is required")
    private String playerId;

    private long betAmount = 100;
}