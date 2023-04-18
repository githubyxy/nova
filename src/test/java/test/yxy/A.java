package test.yxy;

import org.springframework.beans.factory.InitializingBean;

public class A implements InitializingBean {

    private String a;

    public A() {
    }

    public A(String a) {
        this.a = a;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }

}
