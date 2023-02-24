package com.rom4ik.model;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author rom4ik
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Name can not be empty")
    @Size(min = 2, max = 25, message = "Size must be between 2 and 25 characters")
    private String name;

    @Column(name = "author")
    @NotEmpty(message = "Author can not be empty")
    @Size(min = 2, max = 25, message = "Size must be between 2 and 25 characters")
    private String author;

    @Column(name = "publication_year")
    @Max(value = 2023, message = "Maximum year should be 2023")
    private String publicationYear;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Person owner;

    @Builder
    public Book(String name, String author, String publicationYear) {
        this.name = name;
        this.author = author;
        this.publicationYear = publicationYear;
    }
}
