package testMethod.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

public class Mytest2 {

    @Test
    public void checkIstree() {
        Integer[] array = new Integer[]{8, 4, 10, 2, 12, null, 20, 1, 3, 5, 6, 7, 9, 17, 25};
        List<Integer> asList = Arrays.asList(array);
        List<Integer> left = new ArrayList<>();
        left.add(asList.get(1));
        getOneNodeLeafList(1, asList, left);
        List<Integer> right = new ArrayList<>();
        right.add(asList.get(2));
        getOneNodeLeafList(2, asList, right);

        for (Integer integer : left) {
            System.out.print(integer + ",");
        }
        System.out.println("------");
        for (Integer integer : right) {
            System.out.print(integer + ",");
        }
    }

    private static void getOneNodeLeafList(int i, List<Integer> asList, List<Integer> dest) {
        int size = asList.size();
        if (2 * i + 1 < size) {
            dest.add(asList.get(2 * i + 1));
            if (size > 4 * i + 3) {
                getOneNodeLeafList(2 * i + 1, asList, dest);
            }
        }
        if (2 * i + 2 < size) {
            dest.add(asList.get(2 * i + 2));
            if (size > 4 * i + 6) {
                getOneNodeLeafList(2 * i + 2, asList, dest);
            }
        }
    }


    @Test
    public void checkIstree2() {
//		Integer[] array = new Integer[]{8,4,10,2,12,null,20,1,3,5,6,7,9,17,25};
        Integer[] array = new Integer[]{10, 5, 16, 2, 7, 13, 20, 1, 3, 6, 8, 11, 14, 18, 22};
        List<Integer> asList = Arrays.asList(array);
        List<Integer> order = new ArrayList<>();
        guangduOrder(0, asList, order);

        for (Integer integer : order) {
            System.out.println(integer);
        }
    }

    private void zhongOrder(int i, List<Integer> asList, List<Integer> order) {
        int size = asList.size();
        if (size > 2 * i + 1) {
            zhongOrder(2 * i + 1, asList, order);
        }

        order.add(asList.get(i));

        if (size > 2 * i + 2) {
            zhongOrder(2 * i + 2, asList, order);
        }
    }

    private void qianOrder(int i, List<Integer> asList, List<Integer> order) {
        int size = asList.size();
        order.add(asList.get(i));
        if (size > 2 * i + 1) {
            qianOrder(2 * i + 1, asList, order);
        }

        if (size > 2 * i + 2) {
            qianOrder(2 * i + 2, asList, order);
        }
    }

    private void houOrder(int i, List<Integer> asList, List<Integer> order) {
        int size = asList.size();
        if (size > 2 * i + 1) {
            houOrder(2 * i + 1, asList, order);
        }

        if (size > 2 * i + 2) {
            houOrder(2 * i + 2, asList, order);
        }
        order.add(asList.get(i));
    }

    private void guangduOrder(int i, List<Integer> asList, List<Integer> order) {
        int size = asList.size();
        for (int j = 0; j < size / 2; j++) {
            order.add(asList.get(j));
        }
    }

    public static void main(String[] args) {
        JSONArray array = new JSONArray();
        for (int i = 0; i < 2000; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("customerName", "李化" + i);

            String perfix = i + "";
            while (perfix.length() < 6) {
                perfix = "0" + perfix;
            }

            jsonObject.put("phoneNum", "11111" + perfix);
            jsonObject.put("gender", "男");

            array.add(jsonObject);
        }
        System.out.println(JSON.toJSONString(array));
    }

}
