package com.example.petadopt;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Dog")
public class Dog extends Pet {

    private String barkVolume;

    public Dog() {
        super();
    }

    public Dog(String name, int age) {
        super(name,"Dog",age);
    }

    public Dog(String name, String type, int age, String barkVolume) {
        super(name, type, age);
        this.barkVolume = barkVolume;
    }

    @Override
    public void displayInfo() {
        System.out.println("Это собака " + getName() + " с громкостью лая: " + barkVolume);
    }

    public String getBarkVolume() {
        return barkVolume;
    }

    public void setBarkVolume(String barkVolume) {
        this.barkVolume = barkVolume;
    }
}