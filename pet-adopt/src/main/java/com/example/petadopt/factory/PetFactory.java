package com.example.petadopt.factory;

import com.example.petadopt.Cat;
import com.example.petadopt.Dog;
import com.example.petadopt.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetFactory {
    public Pet createPet(String type, String name, int age){
        if (type == null){
            throw new IllegalArgumentException("Тип животного не может быть пустым");
        }

        return switch (type.toLowerCase()){
            case "dog" -> new Dog(name, age);
            case "cat" -> new Cat(name, age);
            default -> throw new IllegalArgumentException("Неизвестный тип животного: " + type);
        };
    }
}
