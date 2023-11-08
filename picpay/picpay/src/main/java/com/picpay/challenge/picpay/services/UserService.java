package com.picpay.challenge.picpay.services;

import com.picpay.challenge.picpay.domain.user.User;
import com.picpay.challenge.picpay.domain.user.UserDTO;
import com.picpay.challenge.picpay.domain.user.UserType;
import com.picpay.challenge.picpay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        this.userRepository.save(user);
    }

    public User createUser(UserDTO userDTO) {
        User newUser = new User(userDTO);
        this.saveUser(newUser);
        return newUser;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User findUserById(Long id) throws Exception {
        return this.userRepository.findById(id).orElseThrow(() -> new Exception("Usuário não encontrado."));
    }

    public boolean validateUser(User payer, BigDecimal amount) throws Exception {

        if (payer.getUserType() == UserType.MERCHANT) {
            throw new Exception("Um usuário Lojista não pode realizar transações.");
        }

        if (payer.getBalance().compareTo(amount) < 0) {
            throw new Exception("Saldo insuficiente.");
        }

        return true;
    }
}
