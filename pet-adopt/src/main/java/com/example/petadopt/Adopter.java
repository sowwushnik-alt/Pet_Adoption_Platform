package com.example.petadopt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Adopter {
    private String name;
    private int age;
    private final List<Pet> adoptedPets;
    private Long id;

    public Adopter(String name, int age){
        this.name = name;
        this.age = age;
        this.adoptedPets = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void adoptPet(Pet pet){
        adoptedPets.add(pet);
    }

    public String toString(){
        return "Adopter: " + name + ", Age: " + age;
    }

    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        Adopter adopter = (Adopter) obj;
        return age == adopter.age && name.equals(adopter.name);
    }

    public int hashCode(){
        return Objects.hash(name, age);
    }

    //Encapsulation
    public List<Pet> getAdoptedPets(){
        return new ArrayList<>(adoptedPets);
    }
}
