package com.portfolio.inventoryservice.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "books")
public class Book {

    @Id
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genre;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private int publicationYear;

    @Column(nullable = false, length = 5)
    private String language;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookFormat format;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int availableCopies;

    protected Book() {}

    public Book(String isbn, String title, String author, Genre genre,
                String publisher, int publicationYear, String language,
                BookFormat format, BigDecimal price, int availableCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.language = language;
        this.format = format;
        this.price = price;
        this.availableCopies = availableCopies;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public Genre getGenre() { return genre; }
    public String getPublisher() { return publisher; }
    public int getPublicationYear() { return publicationYear; }
    public String getLanguage() { return language; }
    public BookFormat getFormat() { return format; }
    public BigDecimal getPrice() { return price; }
    public int getAvailableCopies() { return availableCopies; }

    public void deduct(int quantity) {
        this.availableCopies -= quantity;
    }
}