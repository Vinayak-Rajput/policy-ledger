# Policy Ledger

## How to run

Requires Java 17 or later. The default `dev` profile uses an in-memory H2 database; Flyway applies the SQL migrations at startup.

```bash
./mvnw spring-boot:run
```

Swagger UI: <http://localhost:8080/swagger-ui/index.html>  
OpenAPI JSON: <http://localhost:8080/v3/api-docs>

To run with PostgreSQL, create a database named `hdfclife` and start the `prod` profile. Override the connection with `DB_URL`, `DB_USER`, and `DB_PASSWORD` if needed:

```bash
DB_URL=jdbc:postgresql://localhost:5432/<db_name> \
DB_USER=<User-name> DB_PASSWORD=<password> \
./mvnw -Dspring-boot.run.profiles=prod spring-boot:run
```

## Endpoint table

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/policies` | List policies; optionally filter by `status`, `type`, or `customer` |
| `GET` | `/api/policies/search?minPremium={amount}` | Find policies with at least the given base premium |
| `GET` | `/api/policies/{policyNo}` | Get a policy by policy number |
| `POST` | `/api/policies` | Create a policy |
| `DELETE` | `/api/policies/{policyNo}` | Delete a policy |
| `GET` | `/api/policies/{policyNo}/claims` | List claims for a policy |
| `POST` | `/api/claims` | Create a claim |

## `policy_riders` ownership

`Policy` owns the `policy_riders` join table: its `riders` collection declares `@ManyToMany` with `@JoinTable`. `Rider.policies` is the inverse side because it declares `mappedBy = "riders"`. Update the `Policy.riders` collection for association changes to be persisted; changing only `Rider.policies` does not write the join table.

## Why return DTOs with open-in-view disabled?

With open-in-view disabled, the persistence session closes after the service transaction ends.  
If the controller returns a `Policy` entity, its lazy `riders` collection may still be unloaded.  
JSON serialization can then trigger a `LazyInitializationException` because no session is available.  
If loaded, serializing entities can expose internal fields or recurse through the bidirectional `Policy`/`Rider` relationship.  
DTOs let the service load required data within the transaction and return a stable response shape.
