package com.example.petadopt.service;

import com.example.petadopt.Adopter;
import com.example.petadopt.Pet;
import com.example.petadopt.exception.InvalidPetDataException;
import com.example.petadopt.exception.PetNotFoundException;
import com.example.petadopt.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetService implements IPetService {

    @Autowired
    private PetRepository petRepository;

    @Override
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    @Override
    public Optional<Pet> getPetById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));
        return Optional.of(pet);
    }

    @Override
    public Pet createPet(Pet pet) {
        if (pet.getAge() < 0 || pet.getAge() > 30) {
            throw new InvalidPetDataException("Возраст питомца должен быть от 0 до 30 лет!");
        }
        if (pet.getName() == null || pet.getName().isBlank()) {
            throw new InvalidPetDataException("Имя питомца не может быть пустым!");
        }
        return petRepository.save(pet);
    }

    @Override
    public void createPet(String name, String type, int age) throws SQLException {
        petRepository.addPet(name, type, age);
    }

    @Override
    public Pet updatePet(Long id, Pet pet) {
        pet.setId(id);
        return petRepository.save(pet);
    }

    @Override
    public boolean deletePet(Long id) {
        if (petRepository.existsById(id)) {
            petRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void adoptPet(Adopter adopter, Long petId) {
        petRepository.registerAdoptionInDb(adopter, petId);
    }

    @Override
    public List<Pet> getPetsSortedByAge() {
        return petRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Pet::getAge))
                .collect(Collectors.toList());
    }

    @Override
    public List<Pet> filterPetsByType(String type) {
        return petRepository.findAll().stream()
                .filter(p -> p.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public <T> void logAction(T item) {
        System.out.println("Action performed on: " + item.toString());
    }
}