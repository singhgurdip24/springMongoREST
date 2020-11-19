package com.codesingh.movies.controller;

import com.codesingh.movies.Exception.MovieCollectionException;
import com.codesingh.movies.Service.MovieService;
import com.codesingh.movies.model.Movie;
import com.codesingh.movies.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@RestController
public class MovieController {

  @Autowired
  private MovieService movieService;

  @GetMapping(value = "/movies")
  public ResponseEntity<String> getAllMovies(){
    List<Movie> movies = movieService.getAllMovies();

    return new ResponseEntity(movies, movies.size()>0?HttpStatus.OK:HttpStatus.NOT_FOUND);
  }

  @PostMapping(value = "/movies")
  public ResponseEntity<String> createMovie(@RequestBody Movie movie){
    try{
      movieService.createMovie(movie);
      return new ResponseEntity<>("Movie saved successfully "+movie.getTitle(), HttpStatus.OK);
    } catch(ConstraintViolationException e){
      return new ResponseEntity(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
    } catch(MovieCollectionException e) {
      return new ResponseEntity(e.getMessage(),HttpStatus.CONFLICT);
    }
  }

  @PutMapping(value="/movies/{id}")
  public ResponseEntity updateById(@PathVariable("id") String id,@RequestBody Movie newMovie)
  {
    try {
      movieService.updateMovie(id,newMovie);
      return new ResponseEntity("Updated movie with id "+id+"",HttpStatus.OK);
    }
    catch(ConstraintViolationException e)
    {
      return new ResponseEntity(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
    }
    catch (MovieCollectionException e) {
      return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping(value="/movies/{id}")
  public ResponseEntity deleteMovieById(@PathVariable("id") String id) {
    try {
      movieService.deleteMovieById(id);
      return new ResponseEntity("Successfully deleted movie with id " + id, HttpStatus.OK);
    } catch (MovieCollectionException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

}
