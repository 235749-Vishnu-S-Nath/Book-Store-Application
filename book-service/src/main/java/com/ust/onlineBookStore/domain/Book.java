package com.ust.onlineBookStore.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long bookId;

    @Column(unique = true)
    private String isbn;

    private String title;
    private String seriesName;
    private String author;
    private Integer lexile;
    private Integer pageCount;
    private Integer minAge;
    private Integer maxAge;

    @ElementCollection
    @OrderColumn(name = "category_order")
    private String[] categories;

    @Column(length = 3000)
    private String summary;
    private String coverArtUrl;
    private String authorFirstName;
    private String authorLastName;
    private Integer copyright;
    private String publishedWorkId;
    private String binding;
    private String language;
    private double rating;

}
