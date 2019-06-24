package designpattern.builder;

/**
 * 指挥类确定具体的建造顺序
 *
 * @author yuxiaoyu
 */
public class Director {

    public void construct(Builder builder) {
        builder.builderPartA();
        builder.builderPartB();
    }
}
