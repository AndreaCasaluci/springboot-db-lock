package org.andrea.springbootdblock.repository;

import jakarta.persistence.LockModeType;
import org.andrea.springbootdblock.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepositoryPessimisticLock extends JpaRepository<Product, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findByGuid(UUID guid);
}
