package tech.quban.solidsnake.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.quban.solidsnake.entity.Balance;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, String> {
}
