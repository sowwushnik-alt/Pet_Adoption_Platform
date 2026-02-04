import com.example.petadopt.Pet;
import com.example.petadopt.repository.PetRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Main{
    public static void main(String[] args) {
        Properties props = new Properties();
        String propertiesPath = "pet-adopt/src/main/resources/application.properties";

        try (FileInputStream fis = new FileInputStream(propertiesPath)) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("Warning: Could not load application.properties from " + propertiesPath);
            System.err.println("Using default hardcoded credentials.");

            props.setProperty("spring.datasource.url", "jdbc:postgresql://localhost:5432/pet_adoption_platform");
            props.setProperty("spring.datasource.username", "postgres");
            props.setProperty("spring.datasource.password", "0000");
        }

        String url = props.getProperty("spring.datasource.url");
        String user = props.getProperty("spring.datasource.username");
        String password = props.getProperty("spring.datasource.password");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to Database: " + url);

            Shelter myShelter = new Shelter("Four Paws");
            PetRepository repository = new PetRepository(conn);

            ShelterMenu menu = new ShelterMenu(myShelter, repository);
            menu.start();

        } catch (Exception e) {
            System.out.println("Error: Connection to database failed.");
            e.printStackTrace();
        }

    }
}