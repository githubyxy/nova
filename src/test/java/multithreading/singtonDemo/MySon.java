package multithreading.singtonDemo;

public class MySon extends MyFather {

    public MySon() {
    }

    public MySon(String name, String age) {
        super(name, age);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "MyFather [a2=" + super.a2 + ", b2=" + super.b2 + ", name=" + super.getName() + ", age=" + super.getAge() + "]";
    }
}
