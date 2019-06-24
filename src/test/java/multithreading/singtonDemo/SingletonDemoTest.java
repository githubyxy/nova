
package multithreading.singtonDemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class SingletonDemoTest {

    private static final String MySon = null;

    @Test
    public void test1() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
//		MyThread1 a = new MyThread1();
//		MyThread1 b = new MyThread1();
//		MyThread1 c = new MyThread1();
//		
//		a.start();
//		b.start();
//		c.start();

        MySingletonObject1 instance = MySingletonObject1.getInstance();
        System.out.println(instance.hashCode());
        System.out.println(instance.getClass().getName());
        Constructor<?> constructor = MySingletonObject1.class.getDeclaredConstructor(null);
        constructor.setAccessible(true);
        MySingletonObject1 newInstance = (MySingletonObject1) constructor.newInstance();
        System.out.println(newInstance.hashCode());
    }

    @Test
    public void test2() throws InterruptedException {
        MyThread2 a = new MyThread2();
        MyThread2 b = new MyThread2();
        MyThread2 c = new MyThread2();

        a.start();
        b.start();
        c.start();

        a.join();
        b.join();
        c.join();
    }

    @Test
    public void test3() throws InterruptedException {
        MyThread3 a = new MyThread3();
        MyThread3 b = new MyThread3();
        MyThread3 c = new MyThread3();

        a.start();
        b.start();
        c.start();

        a.join();
        b.join();
        c.join();
    }

    @Test
    public void test4() throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        System.out.println(MySingletonObject4.getInstance().hashCode());
        Constructor<?> constructor = MySingletonObject4.class.getDeclaredConstructor(null);
        constructor.setAccessible(true);
        MySingletonObject4 newInstance = (MySingletonObject4) constructor.newInstance();
        System.out.println(newInstance.hashCode());
    }

    @Test
    public void test5() {
        try {
            MySingletonObject5 myObject = MySingletonObject5.getInstance();
            FileOutputStream fosRef = new FileOutputStream(new File(
                    "myObjectFile.txt"));
            ObjectOutputStream oosRef = new ObjectOutputStream(fosRef);
            oosRef.writeObject(myObject);
            oosRef.close();
            fosRef.close();
            System.out.println(myObject.hashCode());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            FileInputStream fisRef = new FileInputStream(new File(
                    "myObjectFile.txt"));
            ObjectInputStream iosRef = new ObjectInputStream(fisRef);
            MySingletonObject5 myObject = (MySingletonObject5) iosRef.readObject();
            iosRef.close();
            fisRef.close();
            System.out.println(myObject.hashCode());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test6() throws InterruptedException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
//		for (int i = 0; i < 10; i++) {
//			Thread aThread = new MyThread6();
//			aThread.start();
//			aThread.join();
//		}
        MyClass instance = MySingletonObject6.A.getInstance();
        System.out.println(instance);

        System.out.println(instance.getClass().getName());
        Class forName = Class.forName("multithreading.singtonDemo.MyClass");

        System.out.println(forName);
        System.out.println("----------");
        System.out.println(forName.newInstance());
    }


    @Test
    public void test7() throws SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
//		Class<?> demo1=null;
//        Class<?> demo2=null;
//        Class<?> demo3=null;
//        try{
//            //一般尽量采用这种形式
//            demo1=Class.forName("multithreading.singtonDemo.MyClass");
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        demo2=new MyClass().getClass();
//        demo3=MyClass.class;
//         
//        System.out.println("类名称   "+demo1);
//        System.out.println("类名称   "+demo2);
//        System.out.println("类名称   "+demo3);


        Constructor<?>[] constructors = Class.forName("multithreading.singtonDemo.MyClass").getConstructors();
        System.out.println(constructors.length);
        Object newInstance = constructors[0].newInstance();
        Object newInstance2 = Class.forName("multithreading.singtonDemo.MyClass").getDeclaredConstructor().newInstance();
        Object newInstance3 = Class.forName("multithreading.singtonDemo.MyClass").getConstructor().newInstance();

    }

    @Test
    public void test8() throws Exception {
//		MySingletonObject1 instance = MySingletonObject1.getInstance();
//		System.out.println(instance);

        // forName 获取的Class 无法创建已经私有化构造方法的实例对象
        Class<?> class1 = Class.forName("multithreading.singtonDemo.MySingletonObject1");
        Object newInstance = class1.newInstance();
        System.out.println(newInstance);

        // 获取构造函数，然后可以创建实例对象（即使私有化的构造方法）
//		Constructor constructor= MySingletonObject1.class.getDeclaredConstructor(null);
//		constructor.setAccessible(true);
//		System.out.println(constructor.newInstance(null));
//		System.out.println(constructor.newInstance(null));
//		System.out.println(constructor.newInstance(null));
//		
//		Constructor<?>[] constructors= MySingletonObject1.class.getDeclaredConstructors();
//		constructors[0].setAccessible(true);
//		System.out.println(constructors[0].newInstance(null));
    }

    @Test
    public void test9() throws Exception {
        Class<?> class1 = MyFather.class;
//		Object newInstance = class1.newInstance();
        System.out.println("----------------------");

        Class<?> class2 = Class.forName("multithreading.singtonDemo.MyFather");
//		// 需要无参构造方法
//		MyFather newInstance2 = (MyFather)class2.newInstance();
//		System.out.println(newInstance2.toString());
        System.out.println("----------------------");

//		Constructor constructor = MyFather.class.getDeclaredConstructor(String.class,String.class);
        Constructor constructor = MyFather.class.getConstructor(String.class, String.class);
        MyFather newInstance3 = (MyFather) constructor.newInstance("2", "22");
        System.out.println(newInstance3.toString());

        // 获取所有的构造函数
        System.out.println(MyFather.class.getDeclaredConstructors().length);
        // 获取到私有的构造方法
        Constructor constructor2 = MyFather.class.getDeclaredConstructor(String.class);
        constructor2.setAccessible(true);
        MyFather newInstance4 = (MyFather) constructor2.newInstance("22");
        System.out.println(newInstance4.toString());

        // 获取公有的构造函数
        System.out.println(MyFather.class.getConstructors().length);
    }

    @Test
    public void test10() throws Exception {
        Class<?> class1 = Class.forName("multithreading.singtonDemo.MyFather");

        // 获取public 成员变量
        for (Field field : class1.getFields()) {
            System.out.println(field.getName());
        }
        System.out.println("----------------");
        for (Field field : class1.getDeclaredFields()) {
            System.out.println(field.getName());
        }
        System.out.println("----------------");

        for (Field field : class1.getDeclaredFields()) {
            field.setAccessible(true);
            System.out.println(Modifier.toString(field.getModifiers()) + " - " + field.getType().toString() + " - " + field.getName());

            // 可以获取静态成员变量
            System.out.println(field.get(class1));
//			if ("i".equals(field.getName())) {
//				field.setInt(class1, 20);
//				System.out.println(field.getInt(class1));
//			}

        }

    }

    @Test
    public void test11() throws Exception {
//		Class<?> class1 = Class.forName("multithreading.singtonDemo.Test1");
//		Class<?> class1 = Test1.class;
        Class<?> class1 = new Test1().getClass();

        int i = 1;
        for (Field field : class1.getDeclaredFields()) {
            Field declaredField = class1.getDeclaredField(field.getName());
            System.out.println(i++ + ":" + declaredField.get(class1));
        }

        // 封装类型跟Object类型 可以被修改
        setFinalStatic(class1.getDeclaredField("short1"), (short) 98);
        setFinalStatic(class1.getDeclaredField("short2"), (short) 98);    //
        setFinalStatic(class1.getDeclaredField("char1"), 'b');
        setFinalStatic(class1.getDeclaredField("char2"), 'b');
        setFinalStatic(class1.getDeclaredField("int1"), 211);
        setFinalStatic(class1.getDeclaredField("Integer"), 211);        //
        setFinalStatic(class1.getDeclaredField("float1"), 22.34f);
        setFinalStatic(class1.getDeclaredField("float2"), 44.31f);    //
        setFinalStatic(class1.getDeclaredField("long1"), 2L);
        setFinalStatic(class1.getDeclaredField("long2"), 3L);        //
        setFinalStatic(class1.getDeclaredField("double1"), 2D);
        setFinalStatic(class1.getDeclaredField("double2"), 3D);    //
        setFinalStatic(class1.getDeclaredField("str"), "new str");
        setFinalStatic(class1.getDeclaredField("boolean1"), false);
        setFinalStatic(class1.getDeclaredField("boolean2"), false);    //
        setFinalStatic(class1.getDeclaredField("modifyObject"), "modify2");    //

        System.out.println(Test1.short1);
        System.out.println(Test1.short2);
        System.out.println(Test1.char1);
        System.out.println(Test1.char2);
        System.out.println(Test1.int1);
        System.out.println(Test1.Integer);
        System.out.println(Test1.float1);
        System.out.println(Test1.float2);
        System.out.println(Test1.long1);
        System.out.println(Test1.long2);
        System.out.println(Test1.double1);
        System.out.println(Test1.double2);
        System.out.println(Test1.str);
        System.out.println(Test1.boolean1);
        System.out.println(Test1.boolean2);
        System.out.println(Test1.modifyObject);
    }

    static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }

    @Test
    public void test12() throws Exception {
        Class<?> class1 = Class.forName("multithreading.singtonDemo.MyFather");
        System.out.println(class1.getDeclaredMethods().length);
        System.out.println(class1.getMethods().length);
        // 仅在当前类中 本类中声明的所有方法（包括私有）
        for (Method method : class1.getDeclaredMethods()) {
            System.out.println(Modifier.toString(method.getModifiers()) + " - " + method.getReturnType() + " - " + method.getName());
            System.out.println(method);
        }
        System.out.println("--------------------");
        // 本类中public方法 + Object中的九个方法
        for (Method method : class1.getMethods()) {
            System.out.println(Modifier.toString(method.getModifiers()) + " - " + method.getTypeParameters().toString() + " - " + method.getName());
        }
    }

    @Test
    public void test13() throws Exception {
        Class<?> class1 = Class.forName("multithreading.singtonDemo.MySon");
        System.out.println(class1.getDeclaredMethods().length);
        System.out.println(class1.getMethods().length);
        // 仅在当前类中 本类中声明的所有方法（包括私有）
        for (Method method : class1.getDeclaredMethods()) {
            System.out.println(Modifier.toString(method.getModifiers()) + " - " + method.getReturnType().toString() + " - " + method.getName());
        }
        System.out.println("--------------------");
        //  当前对象所有父类可以调用的方法 public
        for (Method method : class1.getMethods()) {
            System.out.println(Modifier.toString(method.getModifiers()) + " - " + method.getReturnType().toString() + " - " + method.getName());
        }
//		MySon MySon = (MySon)class1.newInstance();
//		MySon.method();
        Method method = class1.getMethod("toString");
        System.out.println(method.invoke(class1.newInstance()));
    }


}
