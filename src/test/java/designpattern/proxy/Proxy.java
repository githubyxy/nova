package designpattern.proxy;

public class Proxy implements IGiveGift {

    // 被代理的实体对象
    private Pursuit pursuit;

    public Proxy(Person person) {
        this.pursuit = new Pursuit(person);
    }

    @Override
    public void GiveFlowers() {
        // 实际调用的还是被代理对象的方法
        pursuit.GiveFlowers();
    }

}
