package com.example.petadopt.controller;


import com.example.petadopt.Pet;
import com.example.petadopt.factory.PetFactory;
import com.example.petadopt.service.PetService;
import com.example.petadopt.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pets")
public class PetViewController {

    @Autowired
    private PetService petService;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetFactory petFactory;

    @GetMapping("/list")
    public String viewAllPets(Model model) {
        List<Pet> pets = petService.getAllPets();
        model.addAttribute("pets", pets);
        return "pets";
    }

    //Lambda
    @GetMapping("/{id}")
    public String viewPetDetails(@PathVariable Long id, Model model) {
        return petService.getPetById(id)
                .map(p -> {
                    model.addAttribute("pet", p);
                    model.addAttribute("adopters", petRepository.findAllAdopters());
                    return "pet-details";
                })
                .orElse("redirect:/pets/list");
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
                          @RequestParam(required = false) String imageUrl,
                          @RequestParam(required = false) String description) {

        System.out.println("DEBUG: Saving pet with description: " + description);

        Pet pet = petFactory.createPet(type, name, age);

        pet.setId(id);
        pet.setImageUrl(imageUrl);

        if (description == null || description.trim().isEmpty()) {
            pet.setDescription("Этот хвостик очень ждет встречи с вами!");
        } else {
            pet.setDescription(description);
        }

        petRepository.save(pet);

        return "redirect:/pets/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        return petRepository.findById(id)
                .map(pet -> {
                    model.addAttribute("pet", pet);
                    return "pet-form";
                })
                .orElse("redirect:/pets/list");
    }

    @GetMapping("/delete/{id}")
    public String deletePet(@PathVariable Long id) {
        petRepository.deleteById(id);
        return "redirect:/pets/list";
    }

    @PostMapping("/unadopt")
    public String unadoptPet(@RequestParam Long petId) {
        petRepository.removeAdoption(petId);
        return "redirect:/pets/" + petId;
    }
}