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
    private DataSource dataSource;
    private Connection manualConnection;

    @Autowired
    public PetRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        try (Connection conn = dataSource.getConnection()) {
            createTablesIfNotExist(conn);
        } catch (SQLException e) {
            System.err.println("Failed to initialize database tables: " + e.getMessage());
        }
    }

    // Constructor for manual JDBC connection (Console App)
    public PetRepository(Connection connection) {
        this.manualConnection = connection;
        try {
            createTablesIfNotExist(connection);
        } catch (SQLException e) {
            System.err.println("Failed to initialize database tables: " + e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        if (dataSource != null) {
            return dataSource.getConnection();
        }
        return manualConnection;
    }

    private void closeConnection(Connection conn) throws SQLException {
        if (dataSource != null && conn != null) {
            conn.close();
        }
        // If it's a manual connection, we don't close it here as it's managed by the caller
    }

    private void createTablesIfNotExist(Connection connection) throws SQLException {
        String createPet = "CREATE TABLE IF NOT EXISTS pet (" +
                "pet_id SERIAL PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "type VARCHAR(50) NOT NULL, " +
                "age INT NOT NULL, " +
                "pet_category VARCHAR(50))";

        String alterPet = "DO $$ " +
                "BEGIN " +
                "    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='pet' AND column_name='pet_category') THEN " +
                "        ALTER TABLE pet ADD COLUMN pet_category VARCHAR(50); " +
                "        UPDATE pet SET pet_category = type; " +
                "    END IF; " +
                "END $$";

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
            stmt.execute(alterPet);
            stmt.execute(createAdopter);
            stmt.execute(createAdoption);
        }
    }

    public void addPet(String name, String type, int age) throws SQLException {
        Connection connection = getConnection();
        try {
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
                reindexPets(connection);
                
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } finally {
            closeConnection(connection);
        }
    }

    private void reindexPets(Connection connection) throws SQLException {
        // Упрощенная версия переиндексации
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
        Connection connection = getConnection();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("pet_id") + ", Name: " + rs.getString("name") +
                        ", Type: " + rs.getString("type") + ", Age: " + rs.getInt("age"));
            }
        } finally {
            closeConnection(connection);
        }
    }

    public void updatePetName(int id, String newName) throws SQLException {
        String sql = "UPDATE pet SET name = ? WHERE pet_id = ?";
        Connection connection = getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } finally {
            closeConnection(connection);
        }
    }

    public void deletePet(int id) throws SQLException {
        Connection connection = getConnection();
        try {
            connection.setAutoCommit(false);
            try {
                // 1. Delete the pet
                String deleteSql = "DELETE FROM pet WHERE pet_id = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(deleteSql)) {
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();
                }

                // 2. Re-index IDs to be sequential and update foreign keys
                reindexPets(connection);

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } finally {
            closeConnection(connection);
        }
    }

    public void addAdopter(String name, int age) throws SQLException {
        String sql = "INSERT INTO adopter (name, age) VALUES (?, ?)";
        Connection connection = getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.executeUpdate();
        } finally {
            closeConnection(connection);
        }
    }

    public void recordAdoption(int adopterId, int petId) throws SQLException {
        String sql = "INSERT INTO adoption (adopter_id, pet_id) VALUES (?, ?)";
        Connection connection = getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, adopterId);
            pstmt.setInt(2, petId);
            pstmt.executeUpdate();
        } finally {
            closeConnection(connection);
        }
    }

    public void manualReindex() throws SQLException {
        Connection connection = getConnection();
        try {
            connection.setAutoCommit(false);
            try {
                reindexPets(connection);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } finally {
            closeConnection(connection);
        }
    }

    public List<Pet> findAll() {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pet ORDER BY pet_id";
        try {
            Connection connection = getConnection();
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Long id = rs.getLong("pet_id");
                    String name = rs.getString("name");
                    String type = rs.getString("type");
                    int age = rs.getInt("age");

                    Pet pet;
                    if ("Dog".equalsIgnoreCase(type)) {
                        pet = new Dog(name, age);
                    } else if ("Cat".equalsIgnoreCase(type)) {
                        pet = new Cat(name, age);
                    } else {
                        // Fallback for unknown types
                        pet = new Pet(name, type, age) {
                            @Override
                            public void displayInfo() {
                                System.out.println("Pet: " + getName() + " (" + getType() + ")");
                            }
                        };
                    }
                    pet.setId(id);
                    pets.add(pet);
                }
            } finally {
                closeConnection(connection);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching pets: " + e.getMessage());
        }
        return pets;
    }

    public Optional<Pet> findById(Long id) {
        String sql = "SELECT * FROM pet WHERE pet_id = ?";
        try {
            Connection connection = getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setLong(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String name = rs.getString("name");
                        String type = rs.getString("type");
                        int age = rs.getInt("age");

                        Pet pet;
                        if ("Dog".equalsIgnoreCase(type)) {
                            pet = new Dog(name, age);
                        } else if ("Cat".equalsIgnoreCase(type)) {
                            pet = new Cat(name, age);
                        } else {
                            pet = new Pet(name, type, age) {
                                @Override
                                public void displayInfo() {
                                    System.out.println("Pet: " + getName() + " (" + getType() + ")");
                                }
                            };
                        }
                        pet.setId(id);
                        return Optional.of(pet);
                    }
                }
            } finally {
                closeConnection(connection);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching pet by id: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Pet save(Pet pet) {
        try {
            Connection connection = getConnection();
            try {
                if (pet.getId() == null) {
                    // Insert
                    String sql = "INSERT INTO pet (name, type, age, pet_category) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                        pstmt.setString(1, pet.getName());
                        pstmt.setString(2, pet.getType());
                        pstmt.setInt(3, pet.getAge());
                        pstmt.setString(4, pet.getType());
                        pstmt.executeUpdate();
                        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                pet.setId(generatedKeys.getLong(1));
                            }
                        }
                    }
                } else {
                    // Update
                    String sql = "UPDATE pet SET name = ?, type = ?, age = ?, pet_category = ? WHERE pet_id = ?";
                    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setString(1, pet.getName());
                        pstmt.setString(2, pet.getType());
                        pstmt.setInt(3, pet.getAge());
                        pstmt.setString(4, pet.getType());
                        pstmt.setLong(5, pet.getId());
                        pstmt.executeUpdate();
                    }
                }
                return pet;
            } finally {
                closeConnection(connection);
            }
        } catch (SQLException e) {
            System.err.println("Error saving pet: " + e.getMessage());
        }
        return pet;
    }

    public void deleteById(Long id) {
        try {
            deletePet(id.intValue());
        } catch (SQLException e) {
            System.err.println("Error deleting pet by id: " + e.getMessage());
        }
    }
}