package com.example.petadopt;
import java.util.Objects;
import jakarta.persistence.*;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "pet_category")
public abstract class Pet {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private int age;

    public Pet(String name, String type, int age){
        setName(name);
        setType(type);
        setAge(age);
    }

    public abstract void displayInfo();

    public void setId(Long id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType(){
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age){
        this.age = age;
    }

    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pet pet = (Pet) obj;
        return age == pet.age && Objects.equals(name, pet.name) && Objects.equals(type, pet.type);
    }

    public int hashCode(){
        return Objects.hash(name, type, age);
    }

    @Override
    public String toString(){
        return type + ": " + name + " (" + age + ")";
    }

    protected Pet() {}
}
