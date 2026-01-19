package dev.portfolio.cryptotracker.adapter.common;

import dev.portfolio.cryptotracker.domain.model.Coin;
import dev.portfolio.cryptotracker.port.out.AuthProvider;
import dev.portfolio.cryptotracker.port.out.CryptoDataFetcher;

import java.util.Collections;
import java.util.List;

public abstract class AbstractCryptoDataFetcher implements CryptoDataFetcher {
    private final AuthProvider authProvider;
    protected AbstractCryptoDataFetcher(AuthProvider authProvider) { this.authProvider = authProvider; }
    @Override
    public List<Coin> fetchData() {
        String token = authProvider != null ? authProvider.authenticate().orElse(null) : null;
        return fetchWithAuth(token);
    }
    protected abstract List<Coin> fetchWithAuth(String authToken);
}
