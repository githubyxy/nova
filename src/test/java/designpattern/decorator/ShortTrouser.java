package designpattern.decorator;

public class ShortTrouser extends Finery {

    @Override
    public void show() {
        System.out.println("短裤");
        super.show();
    }
}
