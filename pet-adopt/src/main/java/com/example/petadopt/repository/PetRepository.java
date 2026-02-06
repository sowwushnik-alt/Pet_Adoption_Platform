package com.example.petadopt.repository;

import com.example.petadopt.Pet;
import com.example.petadopt.Cat;
import com.example.petadopt.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PetRepository {

    private final DataSource dataSource;

    @Autowired
    public PetRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        try (Connection conn = dataSource.getConnection()) {
            createTablesIfNotExist(conn);
        } catch (SQLException e) {
            System.err.println("Failed to initialize database tables: " + e.getMessage());
        }
    }

    private void createTablesIfNotExist(Connection connection) throws SQLException {
        String createPet = "CREATE TABLE IF NOT EXISTS pet (" +
                "pet_id SERIAL PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "type VARCHAR(50) NOT NULL, " +
                "age INT NOT NULL, " +
                "description TEXT, " +
                "image_url VARCHAR(255), " +
                "pet_category VARCHAR(50), " +
                "bark_volume VARCHAR(50), " +
                "is_indoor BOOLEAN DEFAULT FALSE)";

        // ... создание таблиц adopter и adoption (как в вашем коде) ...

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createPet);
        }
    }

    // 1. Метод FIND ALL
    public List<Pet> findAll() {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pet ORDER BY pet_id";
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pets.add(mapRowToPet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching pets: " + e.getMessage());
        }
        return pets;
    }

    // 2. Метод FIND BY ID (Тот самый, которого не хватало!)
    public Optional<Pet> findById(Long id) {
        String sql = "SELECT * FROM pet WHERE pet_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToPet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding pet: " + e.getMessage());
        }
        return Optional.empty();
    }

    // 3. Метод SAVE (Insert & Update)
    public Pet save(Pet pet) {
        String sql;
        boolean isUpdate = pet.getId() != null;
        if (!isUpdate) {
            sql = "INSERT INTO pet (name, type, age, pet_category, description, image_url, bark_volume, is_indoor) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE pet SET name = ?, type = ?, age = ?, pet_category = ?, description = ?, image_url = ?, bark_volume = ?, is_indoor = ? WHERE pet_id = ?";
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, pet.getName());
            pstmt.setString(2, pet.getType());
            pstmt.setInt(3, pet.getAge());
            pstmt.setString(4, pet.getType());
            pstmt.setString(5, pet.getDescription());
            pstmt.setString(6, pet.getImageUrl());

            if (pet instanceof Dog) {
                pstmt.setString(7, ((Dog) pet).getBarkVolume());
                pstmt.setNull(8, java.sql.Types.BOOLEAN);
            } else if (pet instanceof Cat) {
                pstmt.setNull(7, java.sql.Types.VARCHAR);
                pstmt.setBoolean(8, ((Cat) pet).isIndoor());
            } else {
                pstmt.setNull(7, java.sql.Types.VARCHAR);
                pstmt.setNull(8, java.sql.Types.BOOLEAN);
            }

            if (isUpdate) pstmt.setLong(9, pet.getId());

            pstmt.executeUpdate();
            if (!isUpdate) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) pet.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving pet: " + e.getMessage());
        }
        return pet;
    }

    // 4. Метод DELETE
    public void deleteById(Long id) {
        String sql = "DELETE FROM pet WHERE pet_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting pet: " + e.getMessage());
        }
    }

    // Добавьте это в PetRepository.java
    public void addPet(String name, String type, int age) throws SQLException {
        // pet_category заполняем типом, а остальные новые поля (description и т.д.) будут null по умолчанию
        String sql = "INSERT INTO pet (name, type, age, pet_category) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setInt(3, age);
            pstmt.setString(4, type);
            pstmt.executeUpdate();
        }
    }

    // 5. Метод EXISTS
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM pet WHERE pet_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking existence: " + e.getMessage());
        }
        return false;
    }

    // Вспомогательный метод для маппинга (чтобы не дублировать код в findAll и findById)
    private Pet mapRowToPet(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        String name = rs.getString("name");
        int age = rs.getInt("age");

        Pet pet;
        if ("Dog".equalsIgnoreCase(type)) {
            Dog dog = new Dog(name, age);
            dog.setBarkVolume(rs.getString("bark_volume"));
            pet = dog;
        } else if ("Cat".equalsIgnoreCase(type)) {
            Cat cat = new Cat(name, age);
            cat.setIndoor(rs.getBoolean("is_indoor"));
            pet = cat;
        } else {
            pet = new Pet(name, type, age) { @Override public void displayInfo() {} };
        }

        pet.setId(rs.getLong("pet_id"));
        pet.setDescription(rs.getString("description"));
        pet.setImageUrl(rs.getString("image_url"));
        return pet;
    }
}