package multithreading.threadsignalDemo;

import java.util.ArrayList;
import java.util.List;

public class MyList {

    private List<String> list = new ArrayList<>();

    public void add(String str) {
        list.add(str);
    }

    public int size() {
        return list.size();
    }
}
