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
    private long isbn;
    private String title;
    private String author;
    private String summary;
    private String language;
    private int pageCount;
    private int publishYear;
    private String imageUrl;
}
