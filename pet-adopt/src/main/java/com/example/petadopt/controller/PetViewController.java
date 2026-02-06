package com.example.petadopt.controller;

import com.example.petadopt.Pet;
import com.example.petadopt.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

// pet-adopt\src\main\java\com\example\petadopt\controller\PetViewController.java
@Controller
@RequestMapping("/pets")
public class PetViewController {

    @Autowired
    private PetService petService;

    // Render pets to Thymeleaf (view)
    @GetMapping("/list") // Changed from @GetMapping
    public String viewAllPets(Model model) {
        List<Pet> pets = petService.getAllPets();
        model.addAttribute("pets", pets);
        return "pets"; // Thymeleaf template name (pets.html)
    }

    @GetMapping("/{id}")
    public String viewPetDetails(@PathVariable Long id, Model model) {
        Optional<Pet> pet = petService.getPetById(id);
        if (pet.isPresent()) {
            model.addAttribute("pet", pet.get());
            return "pet-details"; // Создайте файл pet-details.html
        }
        return "redirect:/pets/list"; // Если не найден, возвращаем к списку
    }
}
