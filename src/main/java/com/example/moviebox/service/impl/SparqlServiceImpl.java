package com.example.moviebox.service.impl;

import com.example.moviebox.model.Movie;
import com.example.moviebox.repository.MovieRepository;
import com.example.moviebox.service.SparqlService;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.FileManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SparqlServiceImpl implements SparqlService {

    private final MovieRepository movieRepository;

    public SparqlServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> queryMovies() {

        String queryString = """
                    PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
                    PREFIX dbr: <http://dbpedia.org/resource/>
                    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
                    
                    SELECT DISTINCT ?movie ?name ?releaseYear ?genre ?director ?description
                    WHERE {
                    ?movie a rdf:Type ;
                          dbr:name ?name ;                 
                          dbr:genre ?genre ;
                          dbr:releaseYear ?releaseYear ;  
                          dbr:description ?description ;                         
                          dbr:director ?director .
                    }
                """;
        Model model = ModelFactory.createDefaultModel();
        String filePath = "src/main/java/com/example/moviebox/movie-data.ttl";
        FileManager.get().readModel(model, filePath, "TTL");

        return executeQuery(queryString, model);
    }

    private List<Movie> executeQuery(String queryString, Model model) {
        Map<Long, Movie> moviesMap = new HashMap<>();
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qe = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qe.execSelect();
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();

                String movieName = handleValue(solution, "name");
                String genre = handleValue(solution, "genre");

                Movie existingMovie = movieRepository.findMovieByName(movieName);

                if (existingMovie != null) {
                    List<String> updatedGenres = existingMovie.getGenres();
                    if (!updatedGenres.contains(genre)) {
                        updatedGenres.add(genre);
                    }
                    existingMovie.setGenres(updatedGenres);

                    existingMovie.setDirector(handleValue(solution, "director"));
                    existingMovie.setReleaseYear(handleValue(solution, "releaseYear"));
                    existingMovie.setDescription(handleValue(solution, "description"));

                    movieRepository.save(existingMovie);
                    moviesMap.put(existingMovie.getId(), existingMovie);

                } else {
                    Movie newMovie = new Movie();
                    List<String> initializeGenre = new ArrayList<>();
                    initializeGenre.add(genre);

                    newMovie.setName(movieName);
                    newMovie.setReleaseYear(handleValue(solution, "releaseYear"));
                    newMovie.setGenres(initializeGenre);
                    newMovie.setDirector(handleValue(solution, "director"));
                    newMovie.setDescription(handleValue(solution, "description"));

                    movieRepository.save(newMovie);
                    moviesMap.put(newMovie.getId(), newMovie);

                }

                System.out.printf("Movie: %s, Name: %s, Release Year: %s, Genre: %s, Director: %s, Description: %s%n",
                        existingMovie != null ? existingMovie : "New Movie",
                        movieName,
                        handleValue(solution, "releaseYear"),
                        genre,
                        handleValue(solution, "director"),
                        handleValue(solution, "description"));
            }
        } catch (QueryParseException e) {
            System.err.println("Query parse error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Query execution error: " + e.getMessage());
        }
        System.out.println();
        List<Movie> movies = new ArrayList<>(moviesMap.values());
        return movies;
    }

    private static String handleValue(QuerySolution solution, String varName) {
        try {
            if (solution.contains(varName)) {
                RDFNode node = solution.get(varName);
                if (node.isLiteral()) {
                    return node.asLiteral().getString();
                } else if (node.isResource()) {
                    return node.asResource().getLocalName();
                } else {
                    return "Unknown Type";
                }
            } else {
                return "No Value";
            }
        } catch (Exception e) {
            System.err.printf("Error for variable %s: %s%n", varName, e.getMessage());
            return "Error";
        }
    }
}
