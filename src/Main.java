public class Main{
    public static void main(String[] args) {
        Pet pet1 = new Pet("Muiza", "Cat", 3);
        Pet pet2 = new Pet("Basian", "Cat", 2);


        Adopter adopter1 = new Adopter("Meirkhan", 17);
        Adopter adopter2 = new Adopter("Merey", 15);

        Shelter shelter = new Shelter("Four Paws");
        shelter.setPet(pet1);


        pet1.displayInfo();
        pet2.displayInfo();
        adopter1.displayInfo();
        adopter2.displayInfo();
        shelter.displayInfo();

        if (pet1.isOlderThan(pet2)){
            System.out.println(pet1.getName() + " is older than " + pet2.getName());
        }
        else{
            System.out.println(pet2.getName() + " is older than " + pet1.getName());
        }
    }
}