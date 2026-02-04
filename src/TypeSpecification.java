import com.example.petadopt.Pet;

public class TypeSpecification implements PetSpecification{
    private String type;

    public TypeSpecification(String type){
        this.type = type;
    }

    @Override
    public boolean isSatisfied(Pet pet){
        return pet.getType().equalsIgnoreCase(type);
    }
}


