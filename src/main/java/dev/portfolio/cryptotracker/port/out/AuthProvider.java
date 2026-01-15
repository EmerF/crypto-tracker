package dev.portfolio.cryptotracker.port.out;

import dev.portfolio.cryptotracker.domain.model.Coin;

import java.util.Optional;

public interface AuthProvider {
    Optional<String> authenticate();
}
