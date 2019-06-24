package algorithm.sortingAlgorithm;

/**
 * 快速排序的基本思想：通过一趟排序将待排记录分隔成独立的两部分，其中一部分记录的关键字均比另一部分的关键字小，则可分别对这两部分记录继续进行排序，以达到整个序列有序。
 *
 * @author yuxiaoyu
 */
public class QuickSort {

    /**
     * 快速排序方法
     * 快速排序使用分治法来把一个串（list）分为两个子串（sub-lists）。具体算法描述如下：
     * 从数列中挑出一个元素，称为 “基准”（pivot）；
     * 重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分区退出之后，该基准就处于数列的中间位置。
     * 递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序。
     *
     * @param array
     * @param start
     * @param end
     * @return
     */
    public static int[] quickSort(int[] array, int start, int end) {
        if (start < end) {
            // Swap(s[start], s[(start + end) / 2]); //将中间的这个数和第一个数交换 参见注1
            int i = start, j = end, x = array[start];
            while (i < j) {
                while (i < j && array[j] >= x) // 从右向左找第一个小于x的数
                    j--;
                if (i < j)
                    array[i++] = array[j];

                while (i < j && array[i] < x) // 从左向右找第一个大于等于x的数
                    i++;
                if (i < j)
                    array[j--] = array[i];
            }
            array[i] = x;
            quickSort(array, start, i - 1); // 递归调用
            quickSort(array, i + 1, end);
        }
        return array;
    }


    public static void main(String[] args) {
        int[] array = new int[]{72, 6, 57, 88, 60, 42, 83, 73, 48, 85};
        array = quickSort(array, 0, array.length - 1);
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }
}
