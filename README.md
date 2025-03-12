# Spring Boot DB Locking (_Optimistic_ & _Pessimistic_)
This project demonstrates the implementation of both **_Optimistic Locking_** and 
**_Pessimistic Locking_** strategies using Spring Boot, Hibernate, and PostgreSQL. 
The project also includes Flyway for database migrations, database triggers for history 
tracking, and Docker Compose for easy setup of the PostgreSQL database.

## Features
- **Optimistic Locking:** Ensures that no two transactions can modify the same data 
  simultaneously by using versioning.
- **Pessimistic Locking:** Prevents conflicts by locking the rows in the database during a transaction.
- **PostgreSQL History Tracking:** Uses triggers to copy updated rows into a history table 
  for auditing purposes.

## Technologies Used
- **Spring Boot** (Java 17+)
- **Hibernate JPA**
- **PostgreSQL**
- **Flyway** for _Database Migration_

## Getting Started
### Prerequisites
Ensure the following are installed:
- **Docker** and **Docker Compose**
- **Java 17** or higher
- **Maven**
### Setup with Docker
1. Clone the Repository:
    ```shell
    git clone https://github.com/your-username/springboot-db-lock.git
    cd springboot-db-lock
    ```
2. **Run PostgreSQL with Docker Compose**: The project includes a `docker-compose.yml` file to easily set up a 
   PostgreSQL database:
    ```shell
    docker-compose up
    ```
   This command will start PostgreSQL in a container and expose it on `localhost:5432`. 
   The database credentials are as follows:
   - **Username**: `postgres`
   - **Password**: `postgres`
   - **Database**: `postgres`
   The database data will be persisted using a Docker volume (`postgres_data`), so it will remain even
   if the container is restarted.
3. **Run the Application**: With the PostgreSQL database running, you can start the Spring Boot application
   ```shell
    mvn spring-boot:run
    ```
   The application will automatically apply the database migrations using Flyway and will be ready to use.
4. **Stopping Docker Compose**: After you're done, you can stop the PostgreSQL container with the following
    command:
    ```shell
    docker-compose down
    ```

## Database Migrations with Flyway
The project uses **Flyway** to handle _database migrations_. When the Spring Boot application starts, Flyway will 
automatically run SQL scripts in the `src/main/resources/db/migration/` folder. These migrations will:
- Create the necessary tables (`products` and `products_history`)
- Add the appropriate indexes and constraints
- Ensure database schema consistency across different environments

## Locking Strategies
### Optimistic Locking
Optimistic locking is implemented using the `@Version` annotation on the `Product` entity. 
The `version` column is automatically updated by Hibernate whenever the entity is updated. 
If two users try to update the same `Product` entity simultaneously, one of them will get 
a `OptimisticLockFailureException`, preventing data corruption.

### Example Product Entity with Optimistic Locking:
```java
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private UUID guid = UUID.randomUUID();

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime  createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true)
    private ZonedDateTime  updatedAt;

    @Version()
    private Integer version;
}
```
### Pessimistic Locking
Pessimistic locking is achieved using `@Lock` annotations in Spring Data repositories. 
This approach forces the database to lock rows while a transaction is being processed, 
preventing other users from accessing the same row at the same time.
### Example of Pessimistic Locking in a Spring Data JPA repository:
```java
@Repository
public interface ProductRepositoryPessimisticLock extends JpaRepository<Product, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findByGuid(UUID guid);
}
```
## Database History Tracking with Triggers
The project includes a database trigger that copies the old record from the `products` table to 
the `products_history` table whenever a product is updated. 
This ensures that every update is tracked and allows you to maintain an audit trail.
### SQL Trigger for History Tracking:
```sql
CREATE OR REPLACE FUNCTION track_product_updates()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO products_history (product_id, guid, name, quantity, version, updated_at)
    VALUES (OLD.id, OLD.guid, OLD.name, OLD.quantity, OLD.version, NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER product_update_trigger
    BEFORE UPDATE ON products
    FOR EACH ROW
    WHEN (OLD.version <> NEW.version)
    EXECUTE FUNCTION track_product_updates();
```
This trigger ensures that every update in the `products` table is saved to the 
`products_history` table before the update occurs.
## PostgreSQL Configuration and Indexes
- The `products` table has the following constraints and indexes:
  - **Unique Constraints**: The `guid` column and the `guid` + `version` pair are unique.
  - **Indexes**: The `guid` and `version` columns are indexed to improve performance on lookup and version-based operations.
### SQL for Table Creation:
```sql
CREATE TABLE products (
                         id SERIAL PRIMARY KEY,
                         guid UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
                         name VARCHAR(100) NOT NULL,
                         quantity INT NOT NULL,
                         version INT NOT NULL DEFAULT 0
);

CREATE TABLE products_history (
                                 id SERIAL PRIMARY KEY,
                                 product_id BIGINT NOT NULL,
                                 guid UUID NOT NULL,
                                 name VARCHAR(100) NOT NULL,
                                 quantity INT NOT NULL,
                                 version INT NOT NULL,
                                 updated_at TIMESTAMP NOT NULL,
                                 CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE UNIQUE INDEX idx_products_guid_version ON products(guid, version);
CREATE UNIQUE INDEX idx_products_history_guid_version ON products_history(guid, version);
```

### License
This project is licensed under the MIT License