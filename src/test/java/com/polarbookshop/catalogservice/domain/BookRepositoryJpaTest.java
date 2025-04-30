package com.polarbookshop.catalogservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import com.polarbookshop.catalogservice.config.DataConfig;

@DataJpaTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
public class BookRepositoryJpaTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void cleanDatabase() {
        bookRepository.deleteAll();
    }

    @Test
    void findAllBooks() {
        var book1 = new Book.BookBuilder()
                .isbn("1234567111")
                .title("Title")
                .author("Author")
                .price(12.90)
                .publisher("Polarsophia")
                .build();
        var book2 = new Book.BookBuilder()
                .isbn("1234567222")
                .title("Another Title")
                .author("Another Author")
                .price(13.90)
                .publisher("Polarsophia")
                .build();
        entityManager.persist(book1);
        entityManager.persist(book2);

        Iterable<Book> actualBooks = bookRepository.findAll();

        assertThat(actualBooks).hasSize(2);
        assertThat(actualBooks).contains(book1, book2);
    }

    @Test
    void findBookByIsbnWhenExisting() {
        var isbn = "1234567111";
        var book = new Book.BookBuilder()
                .isbn(isbn)
                .title("Title")
                .author("Author")
                .price(12.90)
                .publisher("Polarsophia")
                .build();
        entityManager.persist(book);

        Optional<Book> actualBook = bookRepository.findByIsbn(isbn);
        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().getIsbn()).isEqualTo(isbn);

    }

    @Test
    void findBookByIsbnWhenNotExisting() {
        Optional<Book> actualBook = bookRepository.findByIsbn("1234567890");
        assertThat(actualBook).isEmpty();
    }

    @Test
    void deleteByIsbn() {
        var isbn = "1234567111";
        var book = new Book.BookBuilder()
                .isbn(isbn)
                .title("Title")
                .author("Author")
                .price(12.90)
                .publisher("Polarsophia")
                .build();
        entityManager.persist(book);

        bookRepository.delete(book);

        Optional<Book> actualBook = bookRepository.findByIsbn(isbn);
        assertThat(actualBook).isEmpty();
    }
}
