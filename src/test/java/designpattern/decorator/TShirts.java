package designpattern.decorator;

public class TShirts extends Finery {

    @Override
    public void show() {
        System.out.println("T恤");
        super.show();
    }

}
