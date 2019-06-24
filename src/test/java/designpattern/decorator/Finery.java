package designpattern.decorator;

public class Finery extends Person {

    private Person componet;

    public void Decorator(Person componet) {
        this.componet = componet;
    }

    @Override
    public void show() {
        if (componet != null) {
            componet.show();
        }
    }

}
