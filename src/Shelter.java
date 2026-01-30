import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Shelter {
    private String name;
    private List<Pet> pets;

    public Shelter(String name, Pet pet){
        this.name = name;
        this.pets = new ArrayList<>();
    }

    public void addPet(Pet pet){
        pets.add(pet);
    }

    public Shelter(String name){
        this.name = name;
        this.pets = new ArrayList<>();
    }

    public List<Pet> filterPets(PetSpecification spec){
        return pets.stream()
                .filter(spec::isSatisfied)
                .collect(Collectors.toList());
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


    public List<Pet> filterByType(String type){
        List<Pet> filteredPets = new ArrayList<>();
        for(Pet pet : pets){
            if (pet.getType().equalsIgnoreCase(type)){
                filteredPets.add(pet);
            }
        }
        return filteredPets;
    }

    public void showAllPets(){
        System.out.println("Shelter: " + name);
        for(Pet pet : pets){
            pet.displayInfo();
        }
    }

}
