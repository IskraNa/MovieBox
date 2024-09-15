package com.example.moviebox.service;

import com.example.moviebox.model.Movie;

import java.util.List;

public interface MovieService {

    Movie addMovie(Movie movie);
    void addToWatchlist(Movie movie);
    void markAsWatched(Movie movie);
    void addToFavorites(Movie movie);
    List getWatchlist();
    List getAlreadyWatched();
    List getFavorites();
    List<Movie> getAllMovies();
}
