package com.polarbookshop.catalogservice.domain;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;

public class BookValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds() {
        var book = Book.builder()
                .isbn("1234567890")
                .title("Title")
                .author("Author")
                .price(9.90)
                .publisher("Polarshphia")
                .build();
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenIsbnNotDefinedThenValidationFails() {
        var book = Book.builder()
                .isbn("")
                .title("Title")
                .author("Author")
                .price(9.90)
                .publisher("Polarshphia")
                .build();
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(2);
        List<String> constraintViolationMessages = violations.stream()
                .map(ConstraintViolation::getMessage).toList();
        assertThat(constraintViolationMessages)
                .contains("The book ISBN must be defined.")
                .contains("The ISBN format must be valid.");
    }

    @Test
    void whenIsbnDefinedButIncorrectThenValidationFails() {
        var book = Book.builder()
                .isbn("a234567890")
                .title("Title")
                .author("Author")
                .price(9.90)
                .publisher("Polarshphia")
                .build();
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The ISBN format must be valid.");
    }

}
