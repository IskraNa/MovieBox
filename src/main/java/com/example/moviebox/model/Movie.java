package com.example.moviebox.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private String description;

    @ElementCollection
    private List<String> genres;

    private String releaseYear;

    private String director;

    //private String image;


    public Movie(String director, String description, String genres, String name, String releaseYear) {
        this.name = name;
        this.genres.add(genres);
        this.description = description;
        this.releaseYear = releaseYear;
        this.director = director;


    }

    public Movie() {

    }
}
