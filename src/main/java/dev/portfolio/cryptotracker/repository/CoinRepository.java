package dev.portfolio.cryptotracker.repository;

import dev.portfolio.cryptotracker.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepository extends JpaRepository<Coin, String> {
}
