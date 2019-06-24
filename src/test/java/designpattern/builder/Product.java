package designpattern.builder;

import java.util.ArrayList;
import java.util.List;

public class Product {

    private List<String> parts = new ArrayList<>();

    public void add(String part) {
        parts.add(part);
    }

    public void show() {
        for (String string : parts) {
            System.out.println(string);
        }
    }
}
