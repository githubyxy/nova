package test.yxy;

import org.apache.commons.codec.binary.Hex;
import org.apache.tomcat.util.buf.HexUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class YxyTest {
    @Test
    public void test() throws Exception {
        A a = new A();
        A a1 = new A("a");
        A b = new A("b");
    }


    @Test
    public void test2() throws Exception {
//        List<String> s = Arrays.asList("01", "03", "00", "10", "00", "01", "85", "CF");

        byte[] bytes = Hex.decodeHex("01030010000185CF");

        System.out.println(Hex.encodeHexString(bytes));
        for (int i=0; i<bytes.length; i++) {
            System.out.println(bytes[i]);
        }
    }

    @Test
    public void optest() {
        Optional<Object> empty = Optional.empty();
//        Optional<String> s = Optional.of("123");
        Optional<Object> o = Optional.ofNullable(null);

//        System.out.println(s.get());
        System.out.println(o.isPresent());

        o.ifPresent(s1 -> {
            System.out.println(s1);
        });
    }

    @Test
    public void test3() {
        String strA = new String();
        String strB = new String();
        strA = "java";
        strB = "java";
        System.out.println(strA == strB);
    }

}
