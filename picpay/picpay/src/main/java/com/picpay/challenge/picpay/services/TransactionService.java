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

import static com.picpay.challenge.picpay.constants.Constants.*;

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

    public Transaction valideTransaction(TransactionDTO transactionDTO) throws Exception {
        Transaction transaction = new Transaction();

        var payer = this.userService.findUserById(transactionDTO.payerId());
        var payee = this.userService.findUserById(transactionDTO.payeeId());

        userService.validateUser(payer, transactionDTO.amount());

        boolean isAuthorize = authorizeTransaction();

        if (!isAuthorize) {
            throw new Exception(TRANSACAO_NAO_AUTORIZADA);
        }

        return transaction;
    }

    public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception {

        var transaction = valideTransaction(transactionDTO);

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transactionDTO.amount());
        newTransaction.setPayer(transaction.getPayer());
        newTransaction.setPayee(transaction.getPayee());
        newTransaction.setTransactionTime(LocalDateTime.now());

        transaction.getPayer().setBalance(transaction.getPayer().getBalance().subtract(transactionDTO.amount()));
        transaction.getPayee().setBalance(transaction.getPayee().getBalance().add(transactionDTO.amount()));

        this.transactionRepository.save(newTransaction);
        this.userService.saveUser(transaction.getPayer());
        this.userService.saveUser(transaction.getPayee());

        notificationService.sendNotification(transaction.getPayer(), TRANSACAO_REALIZADA_COM_SUCESSO);
        notificationService.sendNotification(transaction.getPayee(), TRANSACAO_RECEBIDA_COM_SUCESSO);

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
