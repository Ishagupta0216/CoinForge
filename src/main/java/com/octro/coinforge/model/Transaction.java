package com.octro.coinforge.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;

    private String playerId;
    private String type;
    private long amount;
    private long balanceBefore;
    private long balanceAfter;
    private String description;
    private LocalDateTime timestamp = LocalDateTime.now();
}