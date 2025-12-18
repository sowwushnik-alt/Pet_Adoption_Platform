public class Shelter {
    private String name;
    private Pet pet;

    public Shelter(String name, Pet pet){
        this.name = name;
        this.pet = pet;
    }

    public Shelter(String name){
        this.name = name;
        this.pet = null;
    }


    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public void displayInfo(){
        if (pet != null){
            pet.displayInfo();
        }
        else {
            System.out.println("There's no pet currently");
        }
    }
}
