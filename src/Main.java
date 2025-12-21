public class Main{
    public static void main(String[] args) {
        Pet pet1 = new Pet("Kasian", "Cat", 3);
        Pet pet2 = new Pet("Basian", "Cat", 2);
        Pet pet3 = new Pet("Aqtos", "Dog", 9);


        Adopter adopter1 = new Adopter("Meirkhan", 17);
        Adopter adopter2 = new Adopter("Merey", 15);

        Shelter shelter = new Shelter("Four Paws");
        shelter.addPet(pet1);
        shelter.addPet(pet2);




        System.out.println(pet1.compareAge(pet3));
    }
}