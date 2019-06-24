package suggest.serializeable;

import java.io.Serializable;

/**
 * 反序列化时，一下情况 final类型的值不会被重新赋值
 * 1.通过构造函数为final变量赋值
 * 2.通过方法为final变量赋值
 * 3.final修饰的属性不是基本类型。(基本类型包括：8种基本类型、数组、字符串)
 *
 * @author yuxiaoyu
 */
public class Person implements Serializable {
    private static final long serialVersionUID = 91282334L;
    public String name = "常量";
//	public  final String name=initName();	
//	public String initName(){
//		return "方法";
//	}

//	public  final String name;
//	public Person(){
//		name="构造";
//	}
}
