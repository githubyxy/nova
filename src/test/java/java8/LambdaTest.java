package java8;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import suggest.serializeable.Student;

/**
 * 参考博客园 ：https://www.cnblogs.com/9dragon/p/10913289.html
 *
 * @author yuxiaoyu
 */
public class LambdaTest {

    public static void main(String[] args) {
        List<Student> list = new ArrayList<>();
        Student student = new Student();
        student.setName2("yxy");
        list.add(student);

        MyFunction myFunction = new MyFunction();
        String apply = myFunction.apply(list);
        System.out.println(apply);
    }
}
