import java.util.Scanner;
import java.util.List;

public class ShelterMenu {
    private Shelter shelter;
    private Adopter defaultAdopter;
    private Scanner scanner;

    public ShelterMenu(Shelter shelter){
        this.shelter = shelter;
        this.defaultAdopter = new Adopter("System Admin", 99);
        this.scanner = new Scanner(System.in);
    }

    public void start(){
        boolean running = true;
        while(running){
            System.out.println("\n--- Shelter Management System ---");
            System.out.println("1. Add Pet (Create)");
            System.out.println("2. View All Pets (Read)");
            System.out.println("3. Update Pet Info (Update)");
            System.out.println("4. Remove Pet / Adopt (Delete)");
            System.out.println("5. Search Pet by Name");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                case 1: addPetMenu(); break;
                case 2: shelter.displayInfo(); break;
                case 3: updatePetMenu(); break;
                case 4: adoptPetMenu(); break;
                case 5: searchPet(); break;
                case 0: running = false; break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private void addPetMenu() {
        System.out.print("Enter Type (Dog/Cat): ");
        String type = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();

        if (type.equalsIgnoreCase("Dog")) {
            shelter.addPet(new Dog(name, age));
        } else {
            shelter.addPet(new Cat(name, age));
        }
        System.out.println("Pet added successfully!");
    }

    private void updatePetMenu() {
        System.out.print("Enter the name of the pet to update: ");
        String name = scanner.nextLine();
        Pet pet = shelter.searchByName(name);

        if (pet != null) {
            System.out.print("Enter new name: ");
            pet.setName(scanner.nextLine());
            System.out.print("Enter new age: ");
            pet.setAge(scanner.nextInt());
            System.out.println("Pet updated!");
        } else {
            System.out.println("Pet not found.");
        }
    }

    private void adoptPetMenu() {
        System.out.print("Enter pet name to adopt: ");
        String name = scanner.nextLine();
        Pet pet = shelter.searchByName(name);

        if (pet != null) {
            shelter.adoptPet(defaultAdopter, pet);
        } else {
            System.out.println("Pet not found.");
        }
    }

    private void searchPet() {
        System.out.print("Enter pet name: ");
        String name = scanner.nextLine();
        Pet pet = shelter.searchByName(name);
        if (pet != null) {
            pet.displayInfo();
        } else {
            System.out.println("Pet not found.");
        }
    }
}
