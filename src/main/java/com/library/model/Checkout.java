package com.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Checkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;

    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    private double lateFee;

    // No-args constructor
    public Checkout() {
    }

    // All-args constructor
    public Checkout(Long id, User user, Book book, LocalDate checkoutDate,
                    LocalDate dueDate, LocalDate returnDate, double lateFee) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.lateFee = lateFee;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public double getLateFee() {
        return lateFee;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private User user;
        private Book book;
        private LocalDate checkoutDate;
        private LocalDate dueDate;
        private LocalDate returnDate;
        private double lateFee;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder book(Book book) {
            this.book = book;
            return this;
        }

        public Builder checkoutDate(LocalDate checkoutDate) {
            this.checkoutDate = checkoutDate;
            return this;
        }

        public Builder dueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder returnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
            return this;
        }

        public Builder lateFee(double lateFee) {
            this.lateFee = lateFee;
            return this;
        }

        public Checkout build() {
            return new Checkout(id, user, book, checkoutDate, dueDate, returnDate, lateFee);
        }
    }
}
