package com.example.petadopt;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Cat")
public class Cat extends Pet {

    private boolean isIndoor;

    public Cat() {
        super();
    }

    public Cat(String name, int age) {
        super(name,"Cat",age);
    }

    public Cat(String name, int age, boolean isIndoor) {
        super(name, "Cat", age);
        this.isIndoor = isIndoor;
    }

    @Override
    public void displayInfo() {
        System.out.println("[Cat Profile] Name: " + getName() + ", Age: " + getAge() + ", Indoor: " + isIndoor);
    }

    public boolean isIndoor() {
        return isIndoor;
    }

    public void setIndoor(boolean indoor) {
        isIndoor = indoor;
    }
}