package com.example.petadopt;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Cat") // Это значение будет записываться в колонку pet_category
public class Cat extends Pet {

    private boolean isIndoor; // Пример специфичного поля для кошек (домашняя или нет)

    // Пустой конструктор обязателен для Hibernate
    public Cat() {
        super();
    }

    public Cat(String name, int age) {
        super(name,"Cat",age);
    }

    // Ваш конструктор, адаптированный под логику проекта
    public Cat(String name, int age, boolean isIndoor) {
        super(name, "Cat", age);
        this.isIndoor = isIndoor;
    }

    @Override
    public void displayInfo() {
        System.out.println("[Cat Profile] Name: " + getName() + ", Age: " + getAge() + ", Indoor: " + isIndoor);
    }

    // Геттеры и сеттеры
    public boolean isIndoor() {
        return isIndoor;
    }

    public void setIndoor(boolean indoor) {
        isIndoor = indoor;
    }
}