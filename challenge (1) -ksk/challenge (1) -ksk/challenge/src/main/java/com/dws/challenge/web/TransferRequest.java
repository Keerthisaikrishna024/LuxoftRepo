package com.dws.challenge.web;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {

    @NotNull(message = "Account from ID must not be null")
    private String accountFromId;

    @NotNull(message = "Account to ID must not be null")
    private String accountToId;

    @NotNull(message = "Amount must not be null")
    @Min(value = 1, message = "Transfer amount must be greater than zero")
    private BigDecimal amount;
}
