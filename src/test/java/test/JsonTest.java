package test;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

public class JsonTest {

    @Test
    public void test() {
//        Map mobileMap = new HashMap<>();
//        mobileMap.put("a", "b");
//
//
//        mobileMap.forEach((key,value)->{
//
//        });

        List<A> list = new ArrayList();
//        list.add(new A(3));
        list.add(new A(2));
        list.add(new A(0));
        list.add(new A(0));
        list.add(new A(0));

        Optional<A> max = list.stream().filter(item -> item.getWeight() > 0)
                .max(Comparator.comparing(A::getWeight));

        System.out.println(max.isPresent()?  max.get().getWeight() : null);

    }

    class A {
        private int weight;

        A(int weight) {
            this.weight = weight;
        }


        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }

}
