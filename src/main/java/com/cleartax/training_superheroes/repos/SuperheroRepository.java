package com.cleartax.training_superheroes.repos;

import com.cleartax.training_superheroes.dto.Superhero;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SuperheroRepository extends MongoRepository<Superhero, String> {
//    void deleteByName(String name);
//
//    Optional<Superhero> findByName(String name);

    Superhero findByName(String name);
    void deleteByName(String name);

    Superhero findByUniverse(String universe);
}
