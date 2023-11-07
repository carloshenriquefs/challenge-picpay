package com.picpay.challenge.picpay.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(unique = true)
    private String document;

    @Column(unique = true)
    private String email;
    private String password;
    private UserType userType;
    private BigDecimal balance;

    public User(UserDTO userDTO) {
        this.name = userDTO.name();
        this.document = userDTO.document();
        this.email = userDTO.email();
        this.password = userDTO.password();
        this.userType = userDTO.userType();
        this.balance = userDTO.balance();
    }
}
