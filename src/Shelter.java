import java.util.ArrayList;
import java.util.List;

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

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public ArrayList<Pet> getPets() {
        return new ArrayList<>(pets);
    }



    public void displayInfo(){
        System.out.println("Shelter: " + name);

        if (pets.isEmpty()){
            System.out.println("There is no pet currently");
        }
        else{
            for (Pet pet : pets){
                pet.displayInfo();
            }
        }
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

        public Pet searchByName(String name){
            for(Pet pet : pets){
                if (pet.getName().equalsIgnoreCase(name)){
                    return pet;
                }
            }
            return null;
        }

        public void sortByAge(){
            pets.sort((pet1,pet2) -> Integer.compare(pet1.getAge(), pet2.getAge()));
        }

        public boolean adoptPet(Adopter adopter, Pet pet){
            if (pets.contains(pet)){
                adopter.adoptPet(pet);
                pets.remove(pet);
                System.out.println(adopter.getName() + "has adopted" + pet.getName());
                return true;
            }
            else{
                System.out.println("Pet " + pet.getName() + " is not available for adoption");
                return false;
            }
        }

}
