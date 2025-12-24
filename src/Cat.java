public class Cat extends Pet{
    public Cat(String name, int age){
        super(name, "Cat", age);
    }

    public void displayInfo(){
        System.out.println("Cat Name: " + getName() + ", Age: " + getAge());
    }
}
