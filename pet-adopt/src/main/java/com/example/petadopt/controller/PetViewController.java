package com.example.petadopt.controller;

import com.example.petadopt.Cat;
import com.example.petadopt.Dog;
import com.example.petadopt.Pet;
import com.example.petadopt.Adopter;
import com.example.petadopt.service.PetService;
import com.example.petadopt.repository.PetRepository; // Добавь этот импорт
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
    private PetRepository petRepository; // Теперь это поле есть в классе

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

            // Загружаем список всех готовых хозяев для выпадающего списка
            List<Adopter> allAdopters = petRepository.findAllAdopters();
            model.addAttribute("adopters", allAdopters);

            return "pet-details";
        }
        return "redirect:/pets/list";
    }

    // Оставляем только этот метод для выбора существующего хозяина
    @PostMapping("/adopt")
    public String processAdoption(@RequestParam Long petId,
                                  @RequestParam Long adopterId) {
        // Вызываем метод репозитория для создания связи
        petRepository.adoptPet(adopterId, petId);
        return "redirect:/pets/" + petId;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("pet", new Pet("", "", 0) { @Override public void displayInfo() {} });
        return "pet-form"; // Создадим этот HTML
    }

    // 2. Сохранение (и создание, и обновление)
    @PostMapping("/save")
    public String savePet(@RequestParam(required = false) Long id,
                          @RequestParam String name,
                          @RequestParam String type,
                          @RequestParam int age,
                          @RequestParam String imageUrl,
                          @RequestParam String description) {

        Pet pet;
        // Создаем конкретный объект в зависимости от типа
        if ("Dog".equalsIgnoreCase(type)) {
            Dog dog = new Dog(name, age);
            // Здесь можно добавить специфичные поля для собак
            pet = dog;
        } else {
            Cat cat = new Cat(name, age);
            // Здесь можно добавить специфичные поля для кошек
            pet = cat;
        }

        pet.setId(id); // Если id null — это INSERT, если есть — это UPDATE
        pet.setImageUrl(imageUrl);
        pet.setDescription(description);

        petRepository.save(pet);
        return "redirect:/pets/list";
    }

    // 3. Форма редактирования
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Pet> pet = petRepository.findById(id);
        if (pet.isPresent()) {
            model.addAttribute("pet", pet.get());
            return "pet-form";
        }
        return "redirect:/pets/list";
    }

    // 4. Удаление
    @GetMapping("/delete/{id}")
    public String deletePet(@PathVariable Long id) {
        petRepository.deleteById(id);
        return "redirect:/pets/list";
    }
}