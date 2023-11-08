package com.picpay.challenge.picpay.services;

import com.picpay.challenge.picpay.domain.transaction.Transaction;
import com.picpay.challenge.picpay.domain.transaction.TransactionDTO;
import com.picpay.challenge.picpay.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception {

        var payer = this.userService.findUserById(transactionDTO.payerId());
        var payee = this.userService.findUserById(transactionDTO.payeeId());

        userService.validateUser(payer, transactionDTO.amount());

        boolean isAuthorize = authorizeTransaction();

        if (!isAuthorize) {
            throw new Exception("Transação não autorizada.");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transactionDTO.amount());
        newTransaction.setPayer(payer);
        newTransaction.setPayee(payee);
        newTransaction.setTransactionTime(LocalDateTime.now());

        payer.setBalance(payer.getBalance().subtract(transactionDTO.amount()));
        payee.setBalance(payee.getBalance().add(transactionDTO.amount()));

        this.transactionRepository.save(newTransaction);
        this.userService.saveUser(payer);
        this.userService.saveUser(payee);

        notificationService.sendNotification(payer, "Transação realizada com sucesso.");
        notificationService.sendNotification(payee, "Transação recebida com sucesso.");

        return newTransaction;
    }

    public boolean authorizeTransaction() {
        var response = restTemplate.getForEntity("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String message = (String) response.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);
        } else return false;
    }
}
