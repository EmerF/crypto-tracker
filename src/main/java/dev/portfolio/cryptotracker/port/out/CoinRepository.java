package dev.portfolio.cryptotracker.port.out;

import dev.portfolio.cryptotracker.domain.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepository extends JpaRepository<Coin, String> {
}
