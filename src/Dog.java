public class Dog extends Pet{
    public Dog(String name, int age){
        super(name, "Dog", age);
    }

    @Override
    public void displayInfo() {
        System.out.println("[Dog Profile] Name: " + getName() + ", Age: " + getAge());
    }
}
