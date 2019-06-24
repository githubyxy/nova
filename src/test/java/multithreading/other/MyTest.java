package multithreading.other;

import com.google.gson.Gson;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class MyTest {

    public static void main(String[] args) throws InterruptedException {

//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//		String[] dateStringArray = new String[] { "2000-01-01", "2000-01-02",
//				"2000-01-03", "2000-01-04", "2000-01-05", "2000-01-06",
//				"2000-01-07", "2000-01-08", "2000-01-09", "2000-01-10" };
//
//		MyThread[] threadArray = new MyThread[10];
//		for (int i = 0; i < 10; i++) {
////			sdf = new SimpleDateFormat("yyyy-MM-dd");
//			threadArray[i] = new MyThread(sdf, dateStringArray[i]);
//		}
//		for (int i = 0; i < 10; i++) {
//			Thread.sleep(1000);
//			threadArray[i].start();
//		}


    }

    @Test
    public void Test2() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(6);
        list.add(5);

        list.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;        //[6,5,1]
//                return o1 - o2;		//[1,5,6]	
            }
        });

        System.err.println(new Gson().toJson(list));

    }


    @Test
    public void test() {
        ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<>();

        for (int i = 0; i < 1000; i++) {
            map.put(i, "aaaaaaaaa");

            MyThread myThread = new MyThread(i, map);
            new Thread(myThread).start();
        }

        for (Entry<Integer, String> entry : map.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    public class MyThread implements Runnable {

        private int key;
        private ConcurrentHashMap<Integer, String> map;

        public MyThread(int key, ConcurrentHashMap<Integer, String> map) {
            this.key = key;
            this.map = map;
        }

        @Override
        public void run() {
            map.put(key, map.get(key) + "ssssssss");
        }

    }
}
