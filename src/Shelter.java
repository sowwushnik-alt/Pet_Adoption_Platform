import com.example.petadopt.Pet;

import java.util.ArrayList;
import java.util.List;

public class Shelter {
    private String name;
    private List<Pet> pets;

    public Shelter(String name){
        this.name = name;
        this.pets = new ArrayList<>();
    }

    public void addPet(Pet pet){
        pets.add(pet);
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public ArrayList<Pet> getPets() {
        return new ArrayList<>(pets);
    }

    public void showAllPets(){
        System.out.println("Shelter: " + name);
        for(Pet pet : pets){
            pet.displayInfo();
        }
    }

}
