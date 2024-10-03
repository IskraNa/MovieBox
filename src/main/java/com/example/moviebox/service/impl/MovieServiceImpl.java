package com.example.moviebox.service.impl;

import com.example.moviebox.model.Movie;
import com.example.moviebox.repository.MovieRepository;
import com.example.moviebox.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    private final List<Movie> watchlist = new ArrayList<>();
    private final List<Movie> alreadyWatched = new ArrayList<>();
    private final List<Movie> favorites = new ArrayList<>();


    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public List<Movie> getAllMovies() {
        return null;
    }

    @Override
    public Movie findById(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid movie ID"));
    }

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public void addToWatchlist(Movie movie) {
        if (!watchlist.contains(movie)) {
            watchlist.add(movie);
        }
    }

    @Override
    public void markAsWatched(Movie movie) {
        if (watchlist.contains(movie)) {
            watchlist.remove(movie);
            if (!alreadyWatched.contains(movie)) {
                alreadyWatched.add(movie);
            }
        }
    }

    @Override
    public void addToFavorites(Movie movie) {
        if (!favorites.contains(movie)) {
            favorites.add(movie);

        }
    }

    public List<Movie> getWatchlist() {
        return watchlist;
    }

    public List<Movie> getAlreadyWatched() {
        return alreadyWatched;
    }

    public List<Movie> getFavorites() {
        return favorites;
    }

}
