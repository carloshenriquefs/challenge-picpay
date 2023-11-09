package com.picpay.challenge.picpay.dtos;

import com.picpay.challenge.picpay.domain.user.UserType;

import java.math.BigDecimal;

public record UserDTO(

        String name,

        String lastName,
        String document,
        BigDecimal balance,
        String email,
        String password,
        UserType userType
) {
}
