package com.jpmc.midascore.services;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final IncentiveService incentiveService;

    public TransactionService(UserRepository userRepository,
                              TransactionRecordRepository transactionRecordRepository,
                              IncentiveService incentiveService) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.incentiveService = incentiveService;
    }

    @Transactional
    public void saveTransactionToDatabase(Transaction transaction) {
        UserRecord sender = this.userRepository.findById(transaction.getSenderId());
        UserRecord recipient = this.userRepository.findById(transaction.getRecipientId());
        long senderId = transaction.getSenderId();
        long recipientId = transaction.getRecipientId();
        float amount = transaction.getAmount();

        if (!isValidTransaction(senderId, recipientId, amount, sender)) {
            return;
        }

        // Call incentives API and get amount
        float incentiveAmount = incentiveService.fetchIncentiveAmount(transaction);

        // Now save changes and update balances

        transactionRecordRepository.save(new TransactionRecord(sender, recipient, amount, incentiveAmount));

        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount + incentiveAmount);

        userRepository.save(sender);
        userRepository.save(recipient);

    }

    private boolean isValidTransaction(long senderId, long recipientId, float transactionAmount, UserRecord sender) {
        if (senderId <= 0) {return false;}
        if (recipientId <= 0) {return false;}
        if (sender.getBalance() < transactionAmount) {return false;}
        return true;
    }
}
