import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetRepository {
    private final Connection conn;

    public PetRepository(Connection conn){
        this.conn = conn;
    }

    public void addPet(String name, String type, int age) throws SQLException {
        String sql = "INSERT INTO pet (name, type, age) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, type);
            stmt.setInt(3, age);
            stmt.executeUpdate();
        }
    }

    public void viewPets() throws SQLException {
        String sql = "SELECT * FROM pet";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("pet_id") + " | " + rs.getString("type") +
                        ": " + rs.getString("name") + " (" + rs.getInt("age") + ")");
            }
        }
    }

    public void updatePetName(int id, String newName) throws SQLException {
        String sql = "UPDATE pet SET name = ? WHERE pet_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public void deletePet(int id) throws SQLException {
        String sql = "DELETE FROM pet WHERE pet_id = ?";
        String resetSequence = "SELECT setval('public.pet_new_id_seq', (SELECT MAX(pet_id) FROM pet))";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            try (Statement resetStmt = conn.createStatement()) {
                resetStmt.executeQuery(resetSequence);
            }
        }
    }

    public void addAdopter(String name, int age) throws SQLException {
        String sql = "INSERT INTO adopter (name, age) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.executeUpdate();
        }
    }

    public void recordAdoption(int aId, int pId) throws SQLException {
        String sql = "INSERT INTO adoption (adopter_id, pet_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, aId);
            stmt.setInt(2, pId);
            stmt.executeUpdate();
        }
    }
}
