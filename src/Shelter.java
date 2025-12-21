import java.util.ArrayList;

public class Shelter {
    private String name;
    private ArrayList<Pet>   pets;

    public Shelter(String name, Pet pet){
        this.name = name;
        this.pets = new ArrayList<>();

        if (pet != null){
            pets.add(pet);
        }
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
        return pets;
    }

    public void addPet(Pet pet) {
        pets.add(pet);
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
}
