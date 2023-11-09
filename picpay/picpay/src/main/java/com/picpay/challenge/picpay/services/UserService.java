package com.picpay.challenge.picpay.services;

import com.picpay.challenge.picpay.domain.user.User;
import com.picpay.challenge.picpay.dtos.UserDTO;
import com.picpay.challenge.picpay.domain.user.UserType;
import com.picpay.challenge.picpay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.picpay.challenge.picpay.constants.Constants.*;

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
        return this.userRepository.findById(id).orElseThrow(() -> new Exception(USUARIO_NAO_ENCONTRADO));
    }

    public boolean validateUser(User payer, BigDecimal amount) throws Exception {

        if (payer.getUserType() == UserType.MERCHANT) {
            throw new Exception(LOJISTA_NAO_PODE_REALIZAR_TRANSACAO);
        }

        if (payer.getBalance().compareTo(amount) < 0) {
            throw new Exception(SALDO_INSUFICIENTE);
        }

        return true;
    }
}
