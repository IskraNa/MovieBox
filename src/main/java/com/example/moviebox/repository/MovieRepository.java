package com.example.moviebox.repository;

import com.example.moviebox.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MovieRepository extends JpaRepository<Movie, Long> {

     Movie findMovieByName (String name);
}
