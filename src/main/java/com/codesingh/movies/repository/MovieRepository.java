package com.codesingh.movies.repository;

import com.codesingh.movies.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface MovieRepository extends MongoRepository<Movie,String> {

  @Query("{'title':?0}")
  Optional<Movie> findByTitle(String title);
}
