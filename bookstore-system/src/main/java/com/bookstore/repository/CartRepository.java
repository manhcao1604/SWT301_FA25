package com.bookstore.repository;

import com.bookstore.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findBySessionId(String sessionId);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.updatedAt < :expiryDate")
    void deleteExpiredCarts(@Param("expiryDate") LocalDateTime expiryDate);

    @Query("SELECT COUNT(c) FROM Cart c WHERE c.updatedAt > :since")
    Long countActiveCarts(@Param("since") LocalDateTime since);
}