package java8;

import java.util.List;
import java.util.function.Function;

import suggest.serializeable.Student;


public class MyFunction implements Function<List<Student>, String> {

    @Override
    public String apply(List<Student> t) {
        return t.get(0).getName2();
    }

}
