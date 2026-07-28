package com.sri.product_service.product.repository;

import com.sri.product_service.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product,Long> {

    /**
     * Atomically decrements stock only if enough is available, in a single UPDATE
     * so concurrent decrements can't both pass a separate read-then-write check.
     * Returns the number of rows updated: 0 means insufficient stock (or no such product).
     */
    @Modifying
    @Query("UPDATE Product p SET p.quantity = p.quantity - :quantity " +
            "WHERE p.id = :id AND p.quantity >= :quantity")
    int decrementStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    /**
     * Compensating action for a failed payment after stock was already reserved -
     * no availability guard needed since it's only ever adding back a quantity this
     * same order previously decremented.
     */
    @Modifying
    @Query("UPDATE Product p SET p.quantity = p.quantity + :quantity WHERE p.id = :id")
    int restoreStock(@Param("id") Long id, @Param("quantity") Integer quantity);
}
