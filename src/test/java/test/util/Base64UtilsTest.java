package test.util;

import org.junit.Test;

/**
 * @author yuxiaoyu
 * @date 2020/11/2 下午4:28
 * @Description
 */
public class Base64UtilsTest {

    @Test
    public void assignTest() {
        // 每次分配的起点终点表示区间为
        //[count*seqno/cores, count*(seqno+1)/cores)
        int count=5, cores=4;
        for(int taskNumber = 0; taskNumber < cores; taskNumber++) {
//            int seqno = taskNumber;
            int max = count * (taskNumber + 1) / cores;
            int j = count * taskNumber / cores;
            for (int i = j; i < max; i++){
                System.out.print(i);
            }
            System.out.println("----" + (max-j));
        }
    }

    @Test
    public void contextLoads() {
        //imei前缀
//        String base = "86582003";
        //a,b,c,d,分别记录四组分到的imei个数
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        for (int i = 0; i < 100; i++) {
            //模拟15位的imei码
//            String str = (int) ((Math.random() + 1) * 9) * 10 + "";
            String str = Math.abs(i-50) + "";
            //模4 将FnvHash算法得到的固定结果分成四组
            int hash = FnvHash(str)%4;
            switch (hash){
                case 0:a++;break;
                case 1:b++;break;
                case 2:c++;break;
                case 3:d++;break;
            }
        }
        System.out.println("a：" + a);
        System.out.println("b：" + b);
        System.out.println("c：" + c);
        System.out.println("d：" + d);
        System.out.println("a+b+c+d：" + (a + b + c + d));
    }


    public static int FnvHash(String key) {
        final int p = 16777619;
        long hash = (int) 2166136261L;
        for (int i = 0, n = key.length(); i < n; i++) {
            hash = (hash ^ key.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return ((int) hash & 0x7FFFFFFF) ;
    }


}
