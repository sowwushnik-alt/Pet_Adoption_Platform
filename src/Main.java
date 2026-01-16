import java.sql.Connection;
import java.sql.DriverManager;

public class Main{
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/pet_adoption_platform"; // or mysql
        String user = "postgres";
        String password = "7777";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            System.out.println("Connected to Database!");

            Shelter myShelter = new Shelter("Four Paws");

            ShelterMenu menu = new ShelterMenu(myShelter, conn);
            menu.start();

        } catch (Exception e) {
            System.out.println("Error: Variable 'conn' could not be initialized.");
            e.printStackTrace();
        }


        Shelter myShelter = new Shelter("Sunrise Rescue");
        ShelterMenu menu = new ShelterMenu(myShelter, conn);
        menu.start();

        Pet pet1 = new Cat("Kasian",3);
        Pet pet2 = new Cat("Basian",2);
        Pet pet3 = new Dog("Aqtos", 9);
        Pet pet4 = new Dog("Peanut", 4);
        Pet pet5 = new Cat("Mr.Kitty", 1);


        Adopter adopter1 = new Adopter("Meirkhan", 17);
        Adopter adopter2 = new Adopter("Merey", 15);

        Shelter shelter = new Shelter("Four Paws");
        shelter.addPet(pet1);
        shelter.addPet(pet2);
        shelter.addPet(pet3);

        shelter.adoptPet(adopter1, pet1);
        shelter.adoptPet(adopter1, pet3);
        shelter.adoptPet(adopter2, pet2);

        adopter1.displayInfo();
        adopter2.displayInfo();

        System.out.println("\nUpdated Shelter Info:");
        shelter.displayInfo();

        System.out.println("\nAre Adopter1 and Adopter2 are the same? = " + adopter1.equals(adopter2));

        System.out.println("\nAdopter1 Info: " + adopter1);

        System.out.println(pet1.compareAge(pet3));
    }
}