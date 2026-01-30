import java.sql.Connection;
import java.sql.DriverManager;

public class Main{
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/pet_adoption_platform"; // or mysql
        String user = "postgres";
        String password = "0000";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to Database!");

            Shelter myShelter = new Shelter("Four Paws");
            PetRepository repository = new PetRepository(conn);

            ShelterMenu menu = new ShelterMenu(myShelter, repository);
            menu.start();



        } catch (Exception e) {
            System.out.println("Error: Variable 'conn' could not be initialized.");
            e.printStackTrace();
        }

    }
}