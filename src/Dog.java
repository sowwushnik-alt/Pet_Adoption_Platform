public class Dog extends Pet{
    public Dog(String name, int age){
        super(name, "Dog", age);
    }

    public void displayInfo(){
        System.out.println("Dog Name: " + getName() + ", Age: " + getAge());
    }
}
