package com.polarbookshop.catalogservice.demo;

import java.util.List;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;

@Component
@Profile("testdata")
public class BookDataLoader {

    private final BookRepository bookRepository;

    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData() {
        bookRepository.deleteAll();

        var book1 = Book.builder()
                .isbn("1234567891")
                .title("Northen Lights")
                .author("Lyra Silverstar")
                .price(9.90)
                .publisher("Polarsophia")
                .build();

        var book2 = Book.builder()
                .isbn("1234567892")
                .title("Polar Journey")
                .author("Iorek Polarson")
                .price(12.90)
                .publisher("Polarsophia")
                .build();

        bookRepository.saveAll(List.of(book1, book2));
    }

}
