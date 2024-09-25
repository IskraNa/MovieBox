package com.example.moviebox.web;

import com.example.moviebox.model.Movie;
import com.example.moviebox.service.MovieService;


import com.example.moviebox.service.impl.SparqlServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequestMapping({"/movies","/"})
public class MovieController {

    private final MovieService movieService;
    private final SparqlServiceImpl service;


    public MovieController(MovieService movieService, SparqlServiceImpl service) {
        this.movieService = movieService;
        this.service = service;
    }

    @GetMapping()
    public String showMovies(Model model) {
        List<Movie> movies = service.queryMovies();
        model.addAttribute("movies", movies);
        return "movies";
    }

    @GetMapping({"/name"})
    public String showMoviesHome(Model model, @RequestParam (required = false) String name) {
        List<Movie> movies = service.queryMoviesByName(name);
        model.addAttribute("movies", movies);
        return "movies";
    }

    @GetMapping("/watchLater")
    public String showMoviesWatchLater(Model model) {
        List<Movie> movies = service.queryMovies();
        model.addAttribute("movies", movies);
        return "watchLater";
    }

    @GetMapping("/alreadyWatched")
    public String showMoviesWatchedAlready(Model model) {
        List<Movie> movies = service.queryMovies();
        model.addAttribute("movies", movies);
        return "alreadyWatched";
    }

    @GetMapping("/favourites")
    public String showMoviesFavourites(Model model) {
        List<Movie> movies = service.queryMovies();
        model.addAttribute("movies", movies);
        return "favourites";
    }

    @PostMapping("/add")
    public String addMovie(@ModelAttribute Movie movie) {
        movieService.addMovie(movie);
        return "redirect:/movies";
    }

    @PostMapping("/watchlist/add")
    public String addToWatchlist(@ModelAttribute Movie movie) {
        movieService.addToWatchlist(movie);
        return "redirect:/movies/watchlist";
    }

    @PostMapping("/watched/add")
    public String markAsWatched(@ModelAttribute Movie movie) {
        movieService.markAsWatched(movie);
        return "redirect:/movies/watched";
    }

    @PostMapping("/favorites/add")
    public String addToFavorites(@ModelAttribute Movie movie) {
        movieService.addToFavorites(movie);
        return "redirect:/movies/favorites";
    }

//    @GetMapping("/watchlist")
//    public String getWatchlist(Model model) {
//        List<Movie> watchlist = movieService.getWatchlist();
//        model.addAttribute("watchlist", watchlist);
//        return "watchlist";
//    }
//
//    @GetMapping("/watched")
//    public String getAlreadyWatched(Model model) {
//        List<Movie> watchedMovies = movieService.getAlreadyWatched();
//        model.addAttribute("watchedMovies", watchedMovies);
//        return "watched";
//    }
//
//    @GetMapping("/favorites")
//    public String getFavorites(Model model) {
//        List<Movie> favorites = movieService.getFavorites();
//        model.addAttribute("favorites", favorites);
//        return "favorites";
//    }
}