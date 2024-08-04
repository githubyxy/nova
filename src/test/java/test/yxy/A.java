package test.yxy;

import org.springframework.beans.factory.InitializingBean;

public class A  {

    private String a;

    public A() {
    }

    public A(String a) {
        this.a = a;
    }


    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }
}
