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

    @GetMapping({"/filter"})
    public String showMoviesByNameAndGenre(Model model,
                                           @RequestParam(required = false) String name,
                                           @RequestParam(required = false) String genre) {
        List<Movie> movies;
        if (name != null && genre != null) {
            movies = service.queryMoviesByNameAndGenre(name, genre);
        } else if (name != null) {
            movies = service.queryMoviesByName(name);
        } else if (genre != null) {
            movies = service.queryMoviesByGenre(genre);
        } else {
            movies = service.queryMovies();
        }
        model.addAttribute("movies", movies);
        return "movies";
    }

    @GetMapping("/watchLater")
    public String showMoviesWatchLater(Model model) {
        List<Movie> watchLaterMovies = movieService.getWatchlist();
        if (watchLaterMovies != null) {
            System.out.println("Watch Later Movies: " + watchLaterMovies.size());
        } else {
            System.out.println("No movies found in the watch later list.");
        }
        model.addAttribute("watchLaterMovies", watchLaterMovies);
        return "watchLater.html";
    }

    @GetMapping("/alreadyWatched")
    public String showMoviesWatchedAlready(Model model) {
        List<Movie> alreadyWatchedMovies = movieService.getAlreadyWatched();
        model.addAttribute("alreadyWatchedMovies", alreadyWatchedMovies);
        return "alreadyWatched.html";
    }

    @GetMapping("/favourites")
    public String showMoviesFavourites(Model model) {
        List<Movie> favouriteMovies = movieService.getFavorites();
        model.addAttribute("favouriteMovies", favouriteMovies);
        return "favourites.html";
    }

    @PostMapping("/add")
    public String addMovie(@ModelAttribute Movie movie, Model model) {
        movieService.addMovie(movie);
        List<Movie> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        return "redirect:/movies";
    }

    @PostMapping("/watchlist/add")
    public String addToWatchlist(@RequestParam Long id, Model model) {
        Movie movie = movieService.findById(id);
        if (movie != null) {
            System.out.println("Adding to watchlist: " + movie.getName());
            movieService.addToWatchlist(movie);
        } else {
            System.out.println("Movie not found for ID: " + id);
        }
        return "redirect:/movies";
    }

    @PostMapping("/watched/add")
    public String markAsWatched(@RequestParam Long id) {
        Movie movie = movieService.findById(id);
        if (movie != null) {
            movieService.markAsWatched(movie);
        }
        return "redirect:/movies/alreadyWatched";
    }

    @PostMapping("/favorites/add")
    public String addToFavorites(@RequestParam Long id) {
        Movie movie = movieService.findById(id);
        if (movie != null) {
            movieService.addToFavorites(movie);
        }
        return "redirect:/movies/favourites";
    }
}