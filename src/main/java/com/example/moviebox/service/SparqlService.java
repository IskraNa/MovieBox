package com.example.moviebox.service;

import com.example.moviebox.model.Movie;

import java.util.List;

public interface SparqlService {

    List<Movie> queryMovies();
}
