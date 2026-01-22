import java.sql.*;
import java.util.Scanner;


public class ShelterMenu {
    private final Shelter shelter;
    private final Connection conn;
    private final Scanner scanner;

    public ShelterMenu(Shelter shelter,Connection conn){
        this.shelter = shelter;
        this.conn = conn;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getUserInput("Select an option: ");
            switch (choice) {
                case 1 -> addPetDB();
                case 2 -> viewPetsDB();
                case 3 -> updatePetDB();
                case 4 -> deletePetDB();
                case 5 -> addAdopterDB();
                case 6 -> processAdoptionDB();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice.");
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

    private void addPetDB() {
        System.out.print("Type (Cat/Dog): "); String type = scanner.nextLine();
        System.out.print("Name: "); String name = scanner.nextLine();
        System.out.print("Age: "); int age = scanner.nextInt();

        String sql = "INSERT INTO pet (name, type, age) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, type);
            stmt.setInt(3, age);
            stmt.executeUpdate();
            System.out.println("Pet added to database.");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private int getUserInput(String prompt) {
        int choice = -1;
        while (choice < 0) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } else {
                System.out.println("Invalid input, please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
        return choice;
    }

    private void viewPetsDB() {
        String sql = "SELECT * FROM pet";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("pet_id") + " | " + rs.getString("type") +
                        ": " + rs.getString("name") + " (" + rs.getInt("age") + ")");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void updatePetDB() {
        System.out.print("ID of pet to update: "); int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("New Name: "); String newName = scanner.nextLine();

        String sql = "UPDATE pet SET name = ? WHERE pet_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            System.out.println("Pet updated.");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void deletePetDB() {
        System.out.print("ID of pet to delete: ");
        int id = scanner.nextInt();
        String sql = "DELETE FROM pet WHERE pet_id = ?";
        String resetSequence = "SELECT setval('public.pet_new_id_seq', (SELECT MAX(pet_id) FROM pet))";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Pet record deleted.");

            try (Statement resetStmt = conn.createStatement()) {
                resetStmt.executeQuery(resetSequence);
                System.out.println("Sequence reset successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void addAdopterDB() {
        System.out.print("Adopter Name: "); String name = scanner.nextLine();
        System.out.print("Adopter Age: "); int age = scanner.nextInt();
        String sql = "INSERT INTO adopter (name, age) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.executeUpdate();
            System.out.println("Adopter registered.");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void processAdoptionDB() {
        System.out.print("Adopter ID: "); int aId = scanner.nextInt();
        System.out.print("Pet ID: "); int pId = scanner.nextInt();
        String sql = "INSERT INTO adoption (adopter_id, pet_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, aId);
            stmt.setInt(2, pId);
            stmt.executeUpdate();
            System.out.println("Adoption recorded!");
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
