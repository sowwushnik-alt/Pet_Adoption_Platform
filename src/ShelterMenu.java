import java.sql.*;
import java.util.Scanner;


public class ShelterMenu {
    private Shelter shelter;
    private Connection conn;
    private Scanner scanner;

    public ShelterMenu(Shelter shelter,Connection conn){
        this.shelter = shelter;
        this.conn = conn;
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

    private void viewPetsDB() {
        String sql = "SELECT * FROM pet";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | " + rs.getString("type") +
                        ": " + rs.getString("name") + " (" + rs.getInt("age") + ")");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void updatePetDB() {
        System.out.print("ID of pet to update: "); int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("New Name: "); String newName = scanner.nextLine();

        String sql = "UPDATE pet SET name = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            System.out.println("Pet updated.");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void deletePetDB() {
        System.out.print("ID of pet to delete: "); int id = scanner.nextInt();
        String sql = "DELETE FROM pet WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Pet record deleted.");
        } catch (SQLException e) { e.printStackTrace(); }
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
