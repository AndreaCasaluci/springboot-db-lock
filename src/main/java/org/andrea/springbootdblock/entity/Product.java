package org.andrea.springbootdblock.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@ToString
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
