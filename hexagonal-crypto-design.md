Hexagonal Architecture — Crypto Fetcher Design
=============================================

This document collects the ideas we discussed about designing an extendable system to fetch coin data from many exchanges using a hexagonal (Ports & Adapters) architecture. It includes package recommendations, interface/abstract-class roles, wiring strategies, examples, and how to add new exchanges and authentication.

Table of contents
- Goals
- Key concepts
- Recommended package layout
- Interfaces and base classes
- Auth provider strategy
- Example concrete adapter (Binance)
- Controller / service wiring
- Adding a new exchange
- Testing and edge cases
- Short checklist for implementation


Goals
- Design an extendable system where multiple exchange clients (adapters) implement a common port and can be discovered/injected by Spring automatically.
- Keep domain and application logic independent from exchange-specific details.
- Provide a clear place to add per-exchange authentication and HTTP client config while keeping testability.

Key concepts
- Port: a Java interface that describes what the application needs from the outside. Example: `CryptoDataFetcher` or `CoinRepository`.
- Adapter: a concrete implementation that talks to an external system (exchange API). Example: `BinanceFetcher`.
- Use Hexagonal/Ports-and-Adapters naming: keep interfaces in `port` packages and adapters in `adapter.out` (or `in` for incoming adapters).
- Use Spring to auto-detect concrete adapters annotated with `@Component`. Inject a `List<Port>` or `Map<String, Port>` to get all adapters.

Recommended package layout (consistent with your project)
- dev.portfolio.cryptotracker.port.out
  - interfaces: `CryptoDataFetcher`, `AuthProvider` (if needed), `CoinRepository` (already present)
- dev.portfolio.cryptotracker.adapter.out.common
  - shared adapter utilities, e.g. `AbstractCryptoDataFetcher`
- dev.portfolio.cryptotracker.adapter.out.<exchange>
  - concrete adapters like `binance`, `coinbase`, etc.

Naming examples (fully-qualified)
- Port interface: `dev.portfolio.cryptotracker.port.out.CryptoDataFetcher`
- Auth provider: `dev.portfolio.cryptotracker.port.out.AuthProvider`
- Abstract adapter: `dev.portfolio.cryptotracker.adapter.out.common.AbstractCryptoDataFetcher`
- Concrete adapter: `dev.portfolio.cryptotracker.adapter.out.binance.BinanceFetcher`
- Controller: `dev.portfolio.cryptotracker.web.CoinController`
- Processor/service: `dev.portfolio.cryptotracker.service.ApiFetcherProcessor`

Why `AbstractCryptoDataFetcher`?
- It provides a template method pattern: orchestration that is common for all adapters (e.g., run authentication, retry logic, map responses to domain model) while delegating exchange-specific behavior to subclasses.
- Keeps duplicated code (auth orchestration, error handling, logging) in one place.
- It should live in the adapter layer (e.g. `adapter.out.common`) because it's an adapter-level implementation detail, not application-level port.

Design contract (tiny)
Inputs: none (the fetcher retrieves external data)
Outputs: domain model objects (`Coin`, or lists of `Coin`)
Error modes: network errors, auth failures, parsing errors — the abstract base should catch/log and either return null/optional or rethrow domain-friendly exceptions.

Interfaces and base classes (example signatures)

- Port: CryptoDataFetcher
  - String getExchangeId();
  - Coin fetchData();

- Port: AuthProvider
  - Optional<String> authenticate();

- Adapter base: AbstractCryptoDataFetcher (template)
  - final Coin fetchData() {
      String token = authProvider != null ? authProvider.authenticate() : null;
      try { return fetchWithAuth(token); } catch(...) { handle/log; throw new AdapterException(...); }
    }
  - protected abstract Coin fetchWithAuth(String authToken);

Auth provider strategy
- Put `AuthProvider` interface in `port.out` so the application depends on the abstraction.
- Implement per-exchange auth provider in adapter packages, e.g. `adapter.out.binance.BinanceAuthProvider`.
- Inject the auth provider into the corresponding fetcher via constructor injection.
- For exchanges that need signing or HMAC, encapsulate that logic in an `ExchangeClient` or `AuthProvider` implementation.

Example concrete adapter (BinanceFetcher) — structure
- Package: `dev.portfolio.cryptotracker.adapter.out.binance`
- Class: `@Component public class BinanceFetcher extends AbstractCryptoDataFetcher`
- Injected dependencies: `BinanceAuthProvider`, a configured `RestTemplate` or `WebClient`, optional `BinanceClient` that wraps endpoints.
- Implement `fetchWithAuth(String authToken)` to call Binance endpoints and map response to `Coin`.

Controller / service wiring example
- A processor collects all fetchers from Spring:
  - public class ApiFetcherProcessor {
      private final List<CryptoDataFetcher> fetchers;
      public ApiFetcherProcessor(List<CryptoDataFetcher> fetchers) { this.fetchers = fetchers; }
      public List<Coin> fetchAll() { return fetchers.stream().map(CryptoDataFetcher::fetchData).collect(Collectors.toList()); }
    }
- Controller injects `ApiFetcherProcessor` and exposes an endpoint:
  - @RestController public class CoinController { private final ApiFetcherProcessor fetcher; public CoinController(ApiFetcherProcessor fetcher) { this.fetcher = fetcher; } @GetMapping("/coins") public List<Coin> all() { return fetcher.fetchAll(); } }

Will Spring inject implementations automatically?
- Yes: annotating concrete classes with `@Component` (or `@Service` / `@Repository`) registers them as beans. Injecting `List<CryptoDataFetcher>` will collect all beans that implement that interface.
- Only concrete classes should be annotated; abstract classes shouldn't be `@Component`.
- To add another exchange, create a new class under `adapter.out.<exchange>`, implement the fetcher (extend the abstract base), add `@Component`, and provide any exchange-specific `AuthProvider` or client beans. Spring will pick it up.

Handling exchanges with different auth and clients
- Each exchange can provide its own `AuthProvider` and possibly a small client wrapper (e.g., `BinanceClient`) that encapsulates HTTP configuration, base URLs, timeouts, signing, etc.
- Inject the exchange client and/or auth provider into the fetcher. This keeps the fetcher code focused on mapping the API response to domain objects.
- If an exchange uses OAuth, API keys, or HMAC signing, keep that code in the adapter; use `@ConfigurationProperties` to wire secrets (and avoid committing them).

Example bean lookup strategies
- List<CryptoDataFetcher> collects all
- Map<String, CryptoDataFetcher> can map bean name -> instance
- Build an explicit Map by calling fetchers.stream().collect(toMap(CryptoDataFetcher::getExchangeId, f->f)) so you can look up by exchange id at runtime.

Testing and edge cases
- For unit tests, create test doubles that implement `CryptoDataFetcher` and register them via `@TestConfiguration` or directly instantiate `ApiFetcherProcessor` with a List.
- Edge cases: duplicate exchange ids, missing auth provider, failed network calls. Use `@Profile` or `@ConditionalOnProperty` to disable adapters in some envs.
- Decide a failure policy: failing one exchange shouldn't break all results — return partial results and capture errors per-exchange.

Short checklist for implementation
- [ ] Create `CryptoDataFetcher` port in `port.out` (if not present)
- [ ] Create `AuthProvider` port in `port.out`
- [ ] Implement `AbstractCryptoDataFetcher` in `adapter.out.common`
- [ ] Implement concrete adapters in `adapter.out.<exchange>` with `@Component`
- [ ] Add tests for `ApiFetcherProcessor` and one adapter (mock HTTP)

Appendix: example snippets (short)

CryptoDataFetcher (port)

```java
public interface CryptoDataFetcher {
    String getExchangeId();
    Coin fetchData();
}
```

AuthProvider (port)

```java
public interface AuthProvider {
    Optional<String> authenticate();
}
```

AbstractCryptoDataFetcher (adapter/base)

```java
public abstract class AbstractCryptoDataFetcher implements CryptoDataFetcher {
    private final AuthProvider authProvider;
    protected AbstractCryptoDataFetcher(AuthProvider authProvider) { this.authProvider = authProvider; }
    @Override
    public final Coin fetchData() {
        String token = authProvider != null ? authProvider.authenticate().orElse(null) : null;
        return fetchWithAuth(token);
    }
    protected abstract Coin fetchWithAuth(String authToken);
}
```

BinanceFetcher (adapter)

```java
@Component
public class BinanceFetcher extends AbstractCryptoDataFetcher {
    private final RestTemplate rest;
    public BinanceFetcher(BinanceAuthProvider authProvider, RestTemplate rest) {
        super(authProvider);
        this.rest = rest;
    }
    @Override
    protected Coin fetchWithAuth(String authToken) {
        // call Binance API and map to Coin
    }
}
```

ApiFetcherProcessor

```java
@Component
public class ApiFetcherProcessor {
    private final List<CryptoDataFetcher> fetchers;
    public ApiFetcherProcessor(List<CryptoDataFetcher> fetchers) { this.fetchers = fetchers; }
    public List<Coin> fetchAll() { return fetchers.stream().map(CryptoDataFetcher::fetchData).collect(Collectors.toList()); }
}
```

Final notes
- Use `adapter.out` instead of a generic `infrastructure.fetcher` package for clearer hexagonal separation.
- Keep secrets/configs in `@ConfigurationProperties` and use Spring `@Profile` when you need to enable/disable adapters.

-- End of document

