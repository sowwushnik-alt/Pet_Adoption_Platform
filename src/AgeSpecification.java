import com.example.petadopt.Pet;

public class AgeSpecification implements PetSpecification{
    private int minAge;

    public AgeSpecification(int minAge){
        this.minAge = minAge;
    }

    @Override
    public boolean isSatisfied(Pet pet){
        return pet.getAge() >= minAge;
    }
}