package com.library.controller;

import com.library.model.Book;
import com.library.model.Checkout;
import com.library.model.User;
import com.library.repository.BookRepository;
import com.library.repository.CheckoutRepository;
import com.library.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CheckoutRepository checkoutRepository;

    // Constructor for dependency injection (replaces @RequiredArgsConstructor)
    public BookController(BookRepository bookRepository,
                          UserRepository userRepository,
                          CheckoutRepository checkoutRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.checkoutRepository = checkoutRepository;
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam(required = false) String title) {
        if (title != null) {
            return bookRepository.findByTitleContainingIgnoreCase(title);
        }
        return bookRepository.findAll();
    }

    @PostMapping("/checkout/{bookId}")
    @Transactional
    public ResponseEntity<?> checkout(@PathVariable Long bookId, @AuthenticationPrincipal UserDetails userDetails) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        if (!book.isAvailable()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Book is not available"));
        }

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        book.setAvailable(false);
        Checkout checkout = new Checkout();
        checkout.setBook(book);
        checkout.setUser(user);
        checkout.setCheckoutDate(LocalDate.now());
        checkout.setDueDate(LocalDate.now().plusDays(14));

        bookRepository.save(book);
        checkoutRepository.save(checkout);

        return ResponseEntity.ok(Map.of("message", "Book checked out successfully"));
    }

    @PostMapping("/checkin/{bookId}")
    @Transactional
    public ResponseEntity<?> checkin(@PathVariable Long bookId, @AuthenticationPrincipal UserDetails userDetails) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        Checkout checkout = checkoutRepository.findByBookAndUserAndReturnDateIsNull(book, user)
                .orElseThrow(() -> new RuntimeException("No active checkout found for user"));

        book.setAvailable(true);
        checkout.setReturnDate(LocalDate.now());

        long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(checkout.getDueDate(), LocalDate.now());
        if (overdueDays > 0) {
            checkout.setLateFee(overdueDays * 0.5); // $0.50 per day
        }

        bookRepository.save(book);
        checkoutRepository.save(checkout);

        return ResponseEntity.ok(Map.of(
                "message", "Book checked in",
                "lateFee", checkout.getLateFee()
        ));
    }

    @PostMapping("/admin/add")
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        book.setAvailable(true);
        bookRepository.save(book);
        return ResponseEntity.ok(Map.of("message", "Book added"));
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Book deleted"));
    }

    @GetMapping("/admin/report")
    public ResponseEntity<?> feeReport() {
        List<Checkout> all = checkoutRepository.findAll();
        double total = all.stream()
                .mapToDouble(Checkout::getLateFee)
                .sum();
        return ResponseEntity.ok(Map.of("totalLateFees", total));
    }
}
