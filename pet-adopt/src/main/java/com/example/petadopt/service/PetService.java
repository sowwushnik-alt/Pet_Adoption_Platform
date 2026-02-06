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


    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Optional<Pet> getPetById(Long id) {
        return petRepository.findById(id);
    }

    public Pet createPet(Pet pet) {
        return petRepository.save(pet);
    }

    public Pet updatePet(Long id, Pet pet) {
        pet.setId(id);
        return petRepository.save(pet);
    }

    public boolean deletePet(Long id) {
        if (petRepository.existsById(id)) {
            petRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void createPet(String name, String type, int age) throws SQLException {
        petRepository.addPet(name, type, age);
    }
    public void adoptPet(Adopter adopter, Long petId) {
        petRepository.registerAdoptionInDb(adopter, petId);
    }
}
