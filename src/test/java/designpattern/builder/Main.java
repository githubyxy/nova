package designpattern.builder;

public class Main {

    public static void main(String[] args) {

        Director director = new Director();
        ConcreteBuilder concreteBuilder = new ConcreteBuilder();
        director.construct(concreteBuilder);

        Product result = concreteBuilder.getResult();
        result.show();
    }
}
