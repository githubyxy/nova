package innerClass;

import org.junit.Test;

import innerClass.MemberInnerClass.Draw;
import innerClass.StaticInnerClass.Inner;

public class Main {

    public static void main(String[] args) {
//		MemberInnerClass a = new MemberInnerClass(1);
//		System.out.println(a.count);
//		
//		Draw draw = a.new Draw();
////		Draw draw = a.getDrawInstance();
//		draw.drawSahpe();


        StaticInnerClass b = new StaticInnerClass();
//		System.out.println(b.a);

        Inner inner = new StaticInnerClass.Inner();
    }

    @Test
    public void test2() {
        new StaticInnerClass();
    }

}
