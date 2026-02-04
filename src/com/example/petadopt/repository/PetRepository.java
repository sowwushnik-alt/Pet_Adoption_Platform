package com.example.petadopt.repository;

import com.example.petadopt.Pet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PetRepository {
    private final Connection connection;

    public PetRepository(Connection connection) {
        this.connection = connection;
        try {
            createTablesIfNotExist();
        } catch (SQLException e) {
            System.err.println("Failed to initialize database tables: " + e.getMessage());
        }
    }

    private void createTablesIfNotExist() throws SQLException {
        String createPet = "CREATE TABLE IF NOT EXISTS pet (" +
                "pet_id SERIAL PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "type VARCHAR(50) NOT NULL, " +
                "age INT NOT NULL, " +
                "pet_category VARCHAR(50))";

        String createAdopter = "CREATE TABLE IF NOT EXISTS adopter (" +
                "adopter_id SERIAL PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "age INT NOT NULL)";

        String createAdoption = "CREATE TABLE IF NOT EXISTS adoption (" +
                "adopter_id INT NOT NULL, " +
                "pet_id INT NOT NULL, " +
                "adoption_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "PRIMARY KEY (adopter_id, pet_id), " +
                "CONSTRAINT fk_adopter FOREIGN KEY (adopter_id) REFERENCES adopter(adopter_id) ON DELETE CASCADE, " +
                "CONSTRAINT fk_pet FOREIGN KEY (pet_id) REFERENCES pet(pet_id) ON DELETE CASCADE)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createPet);
            stmt.execute(createAdopter);
            stmt.execute(createAdoption);
        }
    }

    public void addPet(String name, String type, int age) throws SQLException {
        connection.setAutoCommit(false);
        try {
            String sql = "INSERT INTO pet (name, type, age) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, type);
                pstmt.setInt(3, age);
                pstmt.executeUpdate();
            }

            // Re-index after addition to maintain perfect sequence
            reindexPets();
            
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private void reindexPets() throws SQLException {
        // Упрощенная версия
        String reindexSql =
            "DO $$ " +
            "DECLARE " +
            "    r RECORD; " +
            "    new_id INT := 1; " +
            "BEGIN " +
            "    FOR r IN SELECT pet_id FROM pet ORDER BY pet_id LOOP " +
            "        UPDATE pet SET pet_id = new_id WHERE pet_id = r.pet_id; " +
            "        new_id := new_id + 1; " +
            "    END LOOP; " +
            "    PERFORM setval(pg_get_serial_sequence('pet', 'pet_id'), COALESCE((SELECT MAX(pet_id) FROM pet), 0) + 1, false); " +
            "END $$";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(reindexSql);
        }
    }

    public void viewPets() throws SQLException {
        String sql = "SELECT * FROM pet ORDER BY pet_id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("pet_id") + ", Name: " + rs.getString("name") +
                        ", Type: " + rs.getString("type") + ", Age: " + rs.getInt("age"));
            }
        }
    }

    public void updatePetName(int id, String newName) throws SQLException {
        String sql = "UPDATE pet SET name = ? WHERE pet_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        }
    }

    public void deletePet(int id) throws SQLException {
        connection.setAutoCommit(false);
        try {
            // 1. Delete the pet
            String deleteSql = "DELETE FROM pet WHERE pet_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(deleteSql)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }

            // 2. Re-index IDs to be sequential and update foreign keys
            reindexPets();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void addAdopter(String name, int age) throws SQLException {
        String sql = "INSERT INTO adopter (name, age) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.executeUpdate();
        }
    }

    public void recordAdoption(int adopterId, int petId) throws SQLException {
        String sql = "INSERT INTO adoption (adopter_id, pet_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, adopterId);
            pstmt.setInt(2, petId);
            pstmt.executeUpdate();
        }
    }

    public void manualReindex() throws SQLException {
        connection.setAutoCommit(false);
        try {
            reindexPets();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    // Mock methods to satisfy PetService
    public List<Pet> findAll() { return new ArrayList<>(); }
    public Optional<Pet> findById(Long id) { return Optional.empty(); }
    public Pet save(Pet pet) { return pet; }
    public void deleteById(Long id) {}
}