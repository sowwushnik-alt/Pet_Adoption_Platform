public class Pet {
    private String name;
    private String type;
    private int age;

    public Pet(String name, String type, int age){
        this.name = name;
        this.type = type;
        this.age = age;
    }



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


    public void displayInfo(){
        System.out.println("Pet Name: " + name + " Type: " + type + " Age: " + age);
    }


    public String compareAge(Pet otherPet){
        if(this.age == otherPet.age){
            return "They are the same age";
        } else if (this.age > otherPet.age) {
            return this.name + " is older than " + otherPet.name;
        } else {
            return otherPet.name + " is older than " + this.name;
        }
    }
}
