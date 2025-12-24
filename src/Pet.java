import java.util.Objects;

public abstract class Pet {
    private String name;
    private String type;
    private int age;

    public Pet(String name, String type, int age){
        setName(name);
        setType(type);
        setAge(age);
    }

    public abstract void displayInfo();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType(){
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age){
        this.age = age;
    }

    public String toString(){
        return "Pet Name = " + name + ", Type = " + type + ", Age = " + age;
    }

    public boolean equals(Object obj){
        if (this == obj){return true;}
        if (obj == null || getClass() != obj.getClass()){return false;}
        Pet pet = (Pet) obj;
        return age == pet.age && name.equals(pet.name) && type.equals(pet.type);
    }

    public int hashCode(){
        return Objects.hash(name, type, age);
    }

    public String compareAge(Pet otherPet){
        if(this.age == otherPet.getAge()){
            return "They are the same age";
        } else if (this.age > otherPet.getAge()) {
            return this.getType() + " " + this.name + " is older than " + otherPet.name;
        } else {
            return otherPet.getType() + " " + otherPet.getName() + " is older than " + this.name;
        }
    }
}
