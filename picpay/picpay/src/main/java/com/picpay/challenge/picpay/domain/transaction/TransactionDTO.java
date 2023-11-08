package com.picpay.challenge.picpay.domain.transaction;

import java.math.BigDecimal;

public record TransactionDTO(

        BigDecimal amount,
        Long payerId,
        Long payeeId
) {
}
