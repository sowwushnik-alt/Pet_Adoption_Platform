package com.example.petadopt.service;

import com.example.petadopt.Adopter;
import com.example.petadopt.Pet;
import com.example.petadopt.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;


    // Get all pets
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    // Get a pet by ID
    public Optional<Pet> getPetById(Long id) {
        return petRepository.findById(id);
    }

    // Create a new pet
    public Pet createPet(Pet pet) {
        return petRepository.save(pet);
    }

    // Update an existing pet
    public Pet updatePet(Long id, Pet pet) {
        pet.setId(id);  // Ensure the ID is set for updating the pet
        return petRepository.save(pet);
    }

    // Delete a pet by ID
    public boolean deletePet(Long id) {
        // Check if the pet exists before deleting
        if (petRepository.existsById(id)) {
            petRepository.deleteById(id);
            return true;  // Successfully deleted
        }
        return false;  // Pet not found
    }

    public void createPet(String name, String type, int age) throws SQLException {
        petRepository.addPet(name, type, age);
    }
    public void adoptPet(Adopter adopter, Long petId) {
        petRepository.registerAdoptionInDb(adopter, petId);
    }
}
