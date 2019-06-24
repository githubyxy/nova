package testMethod.Test;

import org.junit.Test;

public class MyTest {

    public static void main(String[] args) {
        String s2 = new String("abc");
        String s1 = "abc";
        String s3 = "abc";
        System.out.println(s3 == s1);
    }

    @Test
    public void testBinary() {
        // 在计算机系统中，数值一律用补码来表示（存储）。
        //正数的补码：与原码相同
        //负数的补码：符号位(最高位)为1，其余位为该数绝对值的原码按位取反；然后整个数加1。
        int n = -3;
        System.out.println(Integer.toBinaryString(n));
        //	1000 0000 0000 0000 0000 0000 0000 0011
        n = n >>> 1;
        System.out.println(n);
        System.out.println(Integer.toBinaryString(n));

        System.out.println(Integer.toBinaryString(2147483646));
        System.out.println(Math.pow(2, 31) - 2);
	    
	    
	   /* 2147483646
	    1111111111111111111111111111110*/
    }
}
