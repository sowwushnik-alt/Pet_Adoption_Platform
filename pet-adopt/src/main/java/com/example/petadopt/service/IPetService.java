package com.example.petadopt.service;

import com.example.petadopt.Adopter;
import com.example.petadopt.Pet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IPetService {
    List<Pet> getAllPets();
    Optional<Pet> getPetById(Long id);
    Pet createPet(Pet pet);
    Pet updatePet(Long id, Pet pet);
    boolean deletePet(Long id);
    void createPet(String name, String type, int age) throws SQLException;
    void adoptPet(Adopter adopter, Long petId);

    List<Pet> getPetsSortedByAge();
    List<Pet> filterPetsByType(String type);
}