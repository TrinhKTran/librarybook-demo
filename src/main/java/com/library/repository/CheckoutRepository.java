package com.library.repository;

import com.library.model.Book;
import com.library.model.Checkout;
import com.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {
    Optional<Checkout> findByBookAndUserAndReturnDateIsNull(Book book, User user);
}