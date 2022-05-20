package tech.quban.solidsnake.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.quban.solidsnake.entity.Balance;
import tech.quban.solidsnake.entity.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllBySender(Balance senderBalance);
    List<Transaction> findAllByReceiver(Balance receiverBalance);
    Optional<Transaction> findByHash(String hash);
}
