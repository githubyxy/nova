package testMethod.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;

import com.google.gson.Gson;

public class ListTest {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 50000; i++) {
            list.add(i + "");
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < list.size() - 1; i++) {
            String first = list.get(i);
            for (int j = list.size() - 1; j > i; j--) {
                String next = list.get(i);
                if (first.equals(next)) {
                    list.remove(j);
                }
            }
        }
        System.out.println(System.currentTimeMillis() - start);

        long start1 = System.currentTimeMillis();
        Map<String, String> map = new HashMap<>();
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (map.get(next) != null) {
                iterator.remove();
            } else {
                map.put(next, "1");
            }
        }
        System.out.println(System.currentTimeMillis() - start1);


    }
}
