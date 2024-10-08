package test.yxy;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

public class SortTest {


    public static void main(String[] args) {
        int[] Integer = new int[]{100, 22, 3, 4, 51, 6, 74, 8, 9, 10};
//        System.out.println(JSONObject.toJSONString(MergeSort(Integer)));
        System.out.println(JSONObject.toJSONString(QuickSort(Integer,0, Integer.length - 1)));
    }



    /**
     * 快速排序方法
     * @param array
     * @param start
     * @param end
     * @return
     */
    public static int[] QuickSort(int[] array, int start, int end) {
        if (array.length < 1 || start < 0 || end >= array.length || start > end) return null;
        int smallIndex = partition(array, start, end);
        if (smallIndex > start)
            QuickSort(array, start, smallIndex - 1);
        if (smallIndex < end)
            QuickSort(array, smallIndex + 1, end);
        return array;
    }
    /**
     * 快速排序算法——partition
     * @param array
     * @param start
     * @param end
     * @return
     */
    public static int partition(int[] array, int start, int end) {
        int pivot = (int) (start + Math.random() * (end - start + 1));
        int smallIndex = start - 1;
//        swap(array, pivot, end);
        for (int i = start; i <= end; i++)
            if (array[i] <= array[end]) {
                smallIndex++;
                if (i > smallIndex)
                    swap(array, i, smallIndex);
            }
        return smallIndex;
    }

    /**
     * 交换数组内两个元素
     * @param array
     * @param i
     * @param j
     */
    public static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * 归并排序
     *
     * @param array
     * @return
     */
    public static int[] MergeSort(int[] array) {
        if (array.length < 2) return array;
        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);
        return merge(MergeSort(left), MergeSort(right));
    }
    /**
     * 归并排序——将两段排序好的数组结合成一个排序数组
     *
     * @param left
     * @param right
     * @return
     */
    public static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        for (int index = 0, i = 0, j = 0; index < result.length; index++) {
            if (i >= left.length)
                result[index] = right[j++];
            else if (j >= right.length)
                result[index] = left[i++];
            else if (left[i] > right[j])
                result[index] = right[j++];
            else
                result[index] = left[i++];
        }
        return result;
    }

    @Test
    public void test() {
        List<Person> list = new ArrayList<>();

        list.add(new Person("a", 3));
        list.add(new Person("a", 4));
//        list.add(new Person("a", 5));
//        list.add(new Person("b", 3));
//        list.add(new Person("b", 4));
//        list.add(new Person("b", 5));
//        List<Person> collect = list.stream()
//                .sorted(Comparator.comparing(Person::getAge).reversed())
//                .sorted(Comparator.comparing(Person::getName).reversed())
//                .collect(Collectors.toList());
        List<Person> collect = list.subList(1, list.size());
        System.out.println(JSONObject.toJSONString(collect));
    }

    @Test
    public void test2() {
        List<TimeMetric> list = new ArrayList<>();

        list.add(new TimeMetric("a",1));
        list.add(new TimeMetric("a", 4));
        list.add(new TimeMetric("a", 5));
        list.add(new TimeMetric("b", 3));
        list.add(new TimeMetric("b", 4));
        list.add(new TimeMetric("b", 5));
        list.add(new TimeMetric(null, 5));
//        Map<String, Long> collect = list.stream().collect(Collectors.toMap(TimeMetric::getTime, TimeMetric::getCount, (k1, k2) -> k1 + k2));
//
//        System.out.println(JSONObject.toJSONString(collect));

        list.stream().sorted(Comparator.comparing(TimeMetric::getTime)).collect(Collectors.toList());
    }
}
