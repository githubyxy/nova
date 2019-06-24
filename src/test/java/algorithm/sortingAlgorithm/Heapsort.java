package algorithm.sortingAlgorithm;

/**
 * 堆排序（Heapsort）是指利用堆这种数据结构所设计的一种排序算法。
 * 堆积是一个近似完全二叉树的结构，并同时满足堆积的性质：即子结点的键值或索引总是小于（或者大于）它的父节点。
 *
 * @author yuxiaoyu
 */
public class Heapsort {

    //声明全局变量，用于记录数组array的长度；
    static int len;

    /**
     * 堆排序算法
     *
     * @param array
     * @return
     */
    public static int[] HeapSort(int[] array) {
        len = array.length;
        if (len < 1) return array;
        //1.构建一个最大堆
        buildMaxHeap(array);
        //2.循环将堆首位（最大值）与末位交换，然后在重新调整最大堆
        while (len > 0) {
            swap(array, 0, len - 1);
            len--;
            adjustHeap(array, 0);
        }
        return array;
    }

    /**
     * 建立最大堆
     *
     * @param array
     */
    public static void buildMaxHeap(int[] array) {
        //从最后一个非叶子节点开始向上构造最大堆
        for (int i = (len / 2 - 1); i >= 0; i--) {
            adjustHeap(array, i);
        }
    }

    /**
     * 调整使之成为大顶堆
     *
     * @param array
     * @param i
     */
    public static void adjustHeap(int[] array, int i) {
        int maxIndex = i;
        //如果有左子树，且左子树大于父节点，则将最大指针指向左子树
        if (i * 2 + 1 < len && array[i * 2 + 1] > array[maxIndex])
            maxIndex = i * 2 + 1;
        //如果有右子树，且右子树大于父节点，则将最大指针指向右子树
        if (i * 2 + 2 < len && array[i * 2 + 2] > array[maxIndex])
            maxIndex = i * 2 + 2;
        //如果父节点不是最大值，则将父节点与最大值交换，并且递归调整与父节点交换的位置。
        if (maxIndex != i) {
            swap(array, maxIndex, i);
            adjustHeap(array, maxIndex);
        }
    }

    /**
     * 调整使之成为小顶堆
     * @param array
     * @param i
     */
//	    public static void adjustHeap(int[] array, int i) {
//	    	int maxIndex = i;
//	    	//如果有左子树，且左子树小于父节点，则将最小指针指向左子树
//	    	if (i * 2 +1 < len && array[i * 2 +1] < array[maxIndex])
//	    		maxIndex = i * 2 +1;
//	    	//如果有右子树，且右子树小于父节点，则将最小指针指向右子树
//	    	if (i * 2 + 2 < len && array[i * 2 + 2] < array[maxIndex])
//	    		maxIndex = i * 2 + 2;
//	    	//如果父节点不是最小值，则将父节点与最小值交换，并且递归调整与父节点交换的位置。
//	    	if (maxIndex != i) {
//	    		swap(array, maxIndex, i);
//	    		adjustHeap(array, maxIndex);
//	    	}
//	    }

    /**
     * 交换数组内两个元素
     *
     * @param array
     * @param i
     * @param j
     */
    public static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void main(String[] args) {
        int[] array = new int[]{5, 4, 3, 8, 7};
        array = HeapSort(array);
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }
}
