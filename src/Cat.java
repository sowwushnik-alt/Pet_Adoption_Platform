public class Cat extends Pet{
    public Cat(String name, int age){
        super(name, "Cat", age);
    }

    @Override
    public void displayInfo() {
        System.out.println("[Cat Profile] Name: " + getName() + ", Age: " + getAge());
    }
}
