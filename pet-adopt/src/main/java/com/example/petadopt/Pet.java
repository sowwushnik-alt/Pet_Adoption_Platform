package com.example.petadopt;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Objects;
import jakarta.persistence.*;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "pet_category")
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Dog.class, name = "Dog"),
    @JsonSubTypes.Type(value = Cat.class, name = "Cat")
})
public abstract class Pet {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private int age;

    @Column(name = "pet_category", insertable = false, updatable = false)
    private String petCategory;

    public Pet(String name, String type, int age){
        setName(name);
        setType(type);
        setAge(age);
    }

    public abstract void displayInfo();

    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
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

    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pet pet = (Pet) obj;
        return age == pet.age && Objects.equals(name, pet.name) && Objects.equals(type, pet.type);
    }

    public int hashCode(){
        return Objects.hash(name, type, age);
    }

    @Override
    public String toString(){
        return type + ": " + name + " (" + age + ")";
    }

    protected Pet() {}
}
