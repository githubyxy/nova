package designpattern.decorator;

public class Main {
    public static void main(String[] args) {
        Person person = new Person("宇");
        TShirts tShirts = new TShirts();
        tShirts.Decorator(person);

        ShortTrouser shortTrouser = new ShortTrouser();
        shortTrouser.Decorator(tShirts);

        shortTrouser.show();
    }
}
