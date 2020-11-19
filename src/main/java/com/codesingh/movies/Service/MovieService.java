package com.codesingh.movies.Service;

import com.codesingh.movies.Exception.MovieCollectionException;
import com.codesingh.movies.model.Movie;
import com.codesingh.movies.repository.MovieRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.*;

@Service
public class MovieService {

  @Autowired
  private MovieRepository movieRepository;

  public List<Movie> getAllMovies() {
    List<Movie> movies=movieRepository.findAll();
    if(movies.size()>0)
    {
      return movies;
    }
    else
    {
      return new ArrayList<Movie>();
    }
  }

  public void deleteMovieById(String id) throws MovieCollectionException {
    Optional<Movie> movieOptional=movieRepository.findById(id);
    if(!movieOptional.isPresent())
    {
      throw new MovieCollectionException(MovieCollectionException.NotFoundException(id));
    }
    else
    {
      movieRepository.deleteById(id);
    }
  }

  public void createMovie(Movie movie) throws ConstraintViolationException,MovieCollectionException {

    Optional<Movie> movieNameOptional=movieRepository.findByTitle(movie.getTitle());
    if(movieNameOptional.isPresent())
    {
      throw new MovieCollectionException(MovieCollectionException.TitleAlreadyExists());
    }
    else
    {
      movieRepository.save(movie);
    }

  }

  public void updateMovie(String id,Movie newMovie) throws ConstraintViolationException,MovieCollectionException
  {
    Optional<Movie> movieWithId=movieRepository.findById(id);
    Optional<Movie> movieWithSameTitle=movieRepository.findByTitle(newMovie.getTitle());
    if(movieWithId.isPresent()) {
      if(movieWithSameTitle.isPresent() && !movieWithSameTitle.get().getId().equals(id))
      {

        throw new MovieCollectionException(MovieCollectionException.TitleAlreadyExists());
      }
      Movie movieToUpdate=movieWithId.get();
      BeanUtils.copyProperties(newMovie,movieToUpdate);

      // To make sure that newMovie doesn't get added as a new document
      movieToUpdate.setId(id);
      movieRepository.save(movieToUpdate);
    } else {
      throw new MovieCollectionException(MovieCollectionException.NotFoundException(id));
    }
  }

}
