package designpattern.proxy;

/**
 * 代理模式  被代理类
 *
 * @author yuxiaoyu
 */
public class Pursuit implements IGiveGift {

    private Person person;

    public Pursuit(Person person) {
        this.person = person;
    }

    @Override
    public void GiveFlowers() {
        System.out.println(person.getName() + " 送你花");
    }

}
