package com.example.petadopt.repository;

import com.example.petadopt.Pet;
import com.example.petadopt.Adopter;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IPetRepository {
    List<Pet> findAll();
    Optional<Pet> findById(Long id);
    Pet save(Pet pet);
    void deleteById(Long id);
    boolean existsById(Long id);
    void addPet(String name, String type, int age) throws SQLException;
    void registerAdoptionInDb(Adopter adopter, Long petId);
    List<Adopter> findAllAdopters();
}