package dev.portfolio.cryptotracker.adapter.out.binance;

import dev.portfolio.cryptotracker.port.out.AuthProvider;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BinanceAuthProvider implements AuthProvider {
    @Override
    public Optional<String> authenticate() {
        return Optional.empty();
    }
}
