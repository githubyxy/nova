package suggest.list;

import java.util.ArrayList;
import java.util.List;

public class Client {
    public static void main(String[] args) {
        List<String> c = new ArrayList<String>();
        c.add("A");
        c.add("B");
        List<String> c1 = new ArrayList<String>(c);
        List<String> c2 = c.subList(0, c.size());
        c.add("C");
//		System.out.println("c == c1? " + c.equals(c1));
//		System.out.println("c == c2? " + c.equals(c2));
//		
        for (String string : c2) {
            System.out.println(string);
        }
    }
}
