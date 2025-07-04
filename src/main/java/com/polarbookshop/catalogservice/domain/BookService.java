package com.polarbookshop.catalogservice.domain;

import java.util.Optional;

public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Iterable<Book> viewBookList() {
        return bookRepository.findAll();
    }

    public Book viewBookDetails(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        return book.orElseThrow(() -> new BookNotFoundException(isbn));
    }

    public Book addBookToCatalog(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new BookAlreadyExistsException(book.getIsbn());
        }
        return bookRepository.save(book);
    }

    public void removeBookFromCatalog(String isbn) {
        bookRepository.deleteByIsbn(isbn);
    }

    public Book editBookDetails(String isbn, Book book) {
        return bookRepository.findByIsbn(isbn)
                .map(
                        existingBook -> {
                            existingBook.setTitle(book.getTitle());
                            existingBook.setAuthor(book.getAuthor());
                            existingBook.setPrice(book.getPrice());
                            existingBook.setPublisher(book.getPublisher());
                            return bookRepository.save(existingBook);
                        })
                .orElseGet(
                        () -> addBookToCatalog(book));

    }

}
