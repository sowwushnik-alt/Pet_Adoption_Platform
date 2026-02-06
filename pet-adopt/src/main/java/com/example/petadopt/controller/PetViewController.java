package com.example.petadopt.controller;

import com.example.petadopt.Cat;
import com.example.petadopt.Dog;
import com.example.petadopt.Pet;
import com.example.petadopt.Adopter;
import com.example.petadopt.service.PetService;
import com.example.petadopt.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pets")
public class PetViewController {

    @Autowired
    private PetService petService;

    @Autowired
    private PetRepository petRepository;

    @GetMapping("/list")
    public String viewAllPets(Model model) {
        List<Pet> pets = petService.getAllPets();
        model.addAttribute("pets", pets);
        return "pets";
    }

    @GetMapping("/{id}")
    public String viewPetDetails(@PathVariable Long id, Model model) {
        Optional<Pet> pet = petService.getPetById(id);
        if (pet.isPresent()) {
            model.addAttribute("pet", pet.get());

            List<Adopter> allAdopters = petRepository.findAllAdopters();
            model.addAttribute("adopters", allAdopters);

            return "pet-details";
        }
        return "redirect:/pets/list";
    }

    @PostMapping("/adopt")
    public String processAdoption(@RequestParam Long petId,
                                  @RequestParam Long adopterId) {
        petRepository.adoptPet(adopterId, petId);
        return "redirect:/pets/" + petId;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("pet", new Pet("", "", 0) { @Override public void displayInfo() {} });
        return "pet-form";
    }

    @PostMapping("/save")
    public String savePet(@RequestParam(required = false) Long id,
                          @RequestParam String name,
                          @RequestParam String type,
                          @RequestParam int age,
                          @RequestParam String imageUrl,
                          @RequestParam String description) {

        Pet pet;
        if ("Dog".equalsIgnoreCase(type)) {
            Dog dog = new Dog(name, age);
            pet = dog;
        } else {
            Cat cat = new Cat(name, age);
            pet = cat;
        }

        pet.setId(id);
        pet.setImageUrl(imageUrl);
        pet.setDescription(description);

        petRepository.save(pet);
        return "redirect:/pets/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Pet> pet = petRepository.findById(id);
        if (pet.isPresent()) {
            model.addAttribute("pet", pet.get());
            return "pet-form";
        }
        return "redirect:/pets/list";
    }

    @GetMapping("/delete/{id}")
    public String deletePet(@PathVariable Long id) {
        petRepository.deleteById(id);
        return "redirect:/pets/list";
    }
}