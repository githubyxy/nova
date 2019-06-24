package algorithm.sortingAlgorithm;

import java.util.Arrays;

/**
 * 和选择排序一样，归并排序的性能不受输入数据的影响，但表现比选择排序好的多，因为始终都是O(n log n）的时间复杂度。代价是需要额外的内存空间。
 * 归并排序是建立在归并操作上的一种有效的排序算法。该算法是采用分治法（Divide and Conquer）的一个非常典型的应用。
 * 归并排序是一种稳定的排序方法。将已有序的子序列合并，得到完全有序的序列；即先使每个子序列有序，再使子序列段间有序。若将两个有序表合并成一个有序表，称为2-路归并。
 *
 * @author yuxiaoyu
 */
public class MergeSort {

    /**
     * 归并排序
     * 把长度为n的输入序列分成两个长度为n/2的子序列；
     * 对这两个子序列分别采用归并排序；
     * 将两个排序好的子序列合并成一个最终的排序序列。
     *
     * @param array
     * @return
     */
    public static int[] mergeSort(int[] array) {
        if (array.length < 2)
            return array;
        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);
        return merge(mergeSort(left), mergeSort(right));
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
            else if (left[i] > right[j])        // >:顺序  <:倒序
                result[index] = right[j++];
            else
                result[index] = left[i++];
        }
        return result;
    }

    public static void main(String[] args) {
        int[] array = new int[]{0, 8, 9, 1, 7, 2, 3, 5, 4, 6};
//		array = mergeSort(array);
        array = mergeSort2(array);
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }


    // 非递归 归并算法
    public static int[] mergeSort2(int[] a) {
        if (a.length < 2)
            return a;
        int len = 1;
        int[] array = new int[10];
        while (len < a.length) {
            for (int i = 0; i < a.length; i += 2 * len) {
                Merge(a, i, len);
            }
            len *= 2;
        }
        return a;
    }


    private static void Merge(int[] a, int i, int len) {
        int start = i;
        int len_i = i + len;//归并的前半部分数组
        int j = i + len;
        int len_j = j + len;//归并的后半部分数组
        int[] temp = new int[2 * len];
        int count = 0;
        while (i < len_i && j < len_j && j < a.length) {
            if (a[i] <= a[j]) {
                temp[count++] = a[i++];
            } else {
                temp[count++] = a[j++];
            }
        }
        while (i < len_i && i < a.length) {//注意：这里i也有可能超过数组长度
            temp[count++] = a[i++];
        }
        while (j < len_j && j < a.length) {
            temp[count++] = a[j++];
        }
        count = 0;
        while (start < j && start < a.length) {
            a[start++] = temp[count++];
        }
    }

    private static void Merge(int[] SR, int[] TR, int i, int m, int n) {
        // TODO Auto-generated method stub
        int j, k, l;
        for (j = m + 1, k = i; i <= m && j < n; k++) {
            if (SR[i] < SR[j]) {
                TR[k] = SR[i++];
            } else {
                TR[k] = SR[j++];
            }
        }
        if (i < m) {
            for (l = 0; l <= m - i; l++) {
                TR[k + 1] = SR[i + 1];
            }
        }

        if (j < n - 1) {
            for (l = 0; l < n - j; l++) {
                TR[k + 1] = SR[j + 1];
            }
        }

    }

}
