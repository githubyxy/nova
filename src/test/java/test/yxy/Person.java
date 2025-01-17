package test.yxy;

public class Person {
    private String name;
    private int age;

    private CheckModeEnum  checkModeEnum;

    public Person() {
    }

    public Person(String name, int age) {
        if (name == null) {
            throw new RuntimeException("name is null");
        }
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void printPerson() {
        System.out.println("Name: " + name + ", Age: " + age);
    }

    public CheckModeEnum getCheckModeEnum() {
        return checkModeEnum;
    }

    public void setCheckModeEnum(CheckModeEnum checkModeEnum) {
        this.checkModeEnum = checkModeEnum;
    }
}

