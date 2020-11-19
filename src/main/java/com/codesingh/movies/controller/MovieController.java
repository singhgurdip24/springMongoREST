package com.codesingh.movies.controller;

import com.codesingh.movies.model.Movie;
import com.codesingh.movies.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class MovieController {

  @Autowired
  private MovieRepository movieRepository;

  @GetMapping(value = "/movies")
  public ResponseEntity<String> getAllMovies(){
    List<Movie> movies = movieRepository.findAll();

    if(movies.size()!=0) {
      return new ResponseEntity(movies, HttpStatus.OK);
    } else {
      return new ResponseEntity("No movies found", HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping(value = "/movies")
  public ResponseEntity<String> createMovie(@RequestBody Movie movie){
    try{
      movieRepository.save(movie);
      return new ResponseEntity<>("Movie saved successfully "+movie.getTitle(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping(value="/movies/{id}")
  public ResponseEntity updateById(@PathVariable("id") String id,@RequestBody Movie newMovie)
  {
    Optional<Movie> movieOptional =movieRepository.findById(id);
    if(movieOptional.isPresent())
    {
      Movie movieToSave=movieOptional.get();
      movieToSave.setTitle(newMovie.getTitle());
      movieToSave.setRating(newMovie.getRating());
      movieToSave.setGenre(newMovie.getGenre());
      movieRepository.save(movieToSave);
      return new ResponseEntity("Updated Movie with id "+id,HttpStatus.OK);
    }
    else
    {
      return new ResponseEntity("No Movie with id "+id+" found",HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping(value="/movies/{id}")
  public ResponseEntity deleteMovieById(@PathVariable("id") String id)
  {
    try{
      movieRepository.deleteById(id);
      return new ResponseEntity("Successfully deleted movie with id "+id,HttpStatus.OK);
    }
    catch (Exception e)
    {
      return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
    }
  }

}
