import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.List;


public class ShelterMenu {
    private final Shelter shelter;
    private final PetRepository repository;
    private final Scanner scanner;

    public ShelterMenu(Shelter shelter,PetRepository repository){
        this.shelter = shelter;
        this.repository = repository;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getUserInput("Select an option: ");
            try {
                switch (choice) {
                    case 1 -> addPetUI();
                    case 2 -> repository.viewPets();
                    case 3 -> updatePetUI();
                    case 4 -> deletePetUI();
                    case 5 -> addAdopterUI();
                    case 6 -> processAdoptionUI();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n--- Shelter Management System ---");
        System.out.println("1. Add Pet (Create)");
        System.out.println("2. View All Pets (Read)");
        System.out.println("3. Update Pet Info (Update)");
        System.out.println("4. Remove Pet / Adopt (Delete)");
        System.out.println("5. Add Adopter");
        System.out.println("6. Process Adoption");
        System.out.println("0. Exit");
    }

    private int getUserInput(String prompt){
        while (true){
            System.out.print(prompt);
            try{
                int choice = scanner.nextInt();
                scanner.nextLine();
                return choice;
            } catch(InputMismatchException e){
                System.out.println("Enter a valid number");
                scanner.nextLine();
            }
        }
    }

    private void addPetUI() throws SQLException {
        System.out.print("Type (Cat/Dog): "); String type = scanner.nextLine();
        System.out.print("Name: "); String name = scanner.nextLine();
        System.out.print("Age: "); int age = scanner.nextInt();
        repository.addPet(name, type, age);
        System.out.println("Pet added successfully.");
    }

    private void updatePetUI() throws SQLException {
        System.out.print("ID of pet to update: "); int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("New Name: "); String newName = scanner.nextLine();
        repository.updatePetName(id, newName);
        System.out.println("Pet updated.");
    }

    private void deletePetUI() throws SQLException {
        System.out.print("ID of pet to delete: "); int id = scanner.nextInt();
        repository.deletePet(id);
        System.out.println("Pet record deleted and sequence reset.");
    }

    private void addAdopterUI() throws SQLException {
        System.out.print("Adopter Name: "); String name = scanner.nextLine();
        System.out.print("Adopter Age: "); int age = scanner.nextInt();
        repository.addAdopter(name, age);
        System.out.println("Adopter registered.");
    }

    private void processAdoptionUI() throws SQLException {
        System.out.print("Adopter ID: "); int aId = scanner.nextInt();
        System.out.print("Pet ID: "); int pId = scanner.nextInt();
        repository.recordAdoption(aId, pId);
        System.out.println("Adoption recorded!");
    }

    private void filterPetsUI() {
        System.out.println("Filter by: 1. Type  2. Age");
        int choice = getUserInput("Choice: ");

        PetSpecification spec = null;
        if (choice == 1) {
            System.out.print("Enter type: ");
            spec = new TypeSpecification(scanner.nextLine());
        } else if (choice == 2) {
            System.out.print("Enter minimum age: ");
            spec = new AgeSpecification(scanner.nextInt());
            scanner.nextLine();
        }

        if (spec != null) {
            List<Pet> results = shelter.filterPets(spec);
            if (results.isEmpty()) {
                System.out.println("No pets found.");
            } else {
                results.forEach(p -> System.out.println(p.toString()));
            }
        }
    }
}
