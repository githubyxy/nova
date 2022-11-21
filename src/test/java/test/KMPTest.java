package test;

import org.junit.Test;

/**
 * @author yuxiaoyu
 * @date 2020/9/10 上午10:25
 * @Description
 */
public class KMPTest {

    @Test
    public void getNext() {
        int[] next = new int[8];
        char[] chars = new char[]{'a', 'b', 'a', 'b', 'a', 'b', 'b'};

        int i, j, slen;
        slen = chars.length;
        i = 0;
        j = -1;
        next[0] = -1;
        while (i < slen) {
            if (j == -1 || chars[i] == chars[j]) {
                ++i;
                ++j;
                next[i] = j;
            } else {
                j = next[j];
            }
        }

        for (int m = 0; m < next.length; m++) {
            System.out.println(next[m]);
        }
    }

    @Test
    public void StringToHex16String() {
        String s = " ";
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        System.out.println(str);
    }
}
