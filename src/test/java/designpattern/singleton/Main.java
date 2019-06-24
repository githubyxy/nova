package designpattern.singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) {
        System.out.println(Singleton.getInstance() == SingletonEnum.INSTANCE.getInstance());


        Class<?> classType = SingletonEnum.class;
        Constructor<?> constructor;
        try {
            constructor = classType.getDeclaredConstructor(null);
            constructor.setAccessible(true);
            constructor.newInstance();
//			constructor = classType.getDeclaredConstructor(null);
//			constructor.setAccessible(true);
//			Singleton singleton = (Singleton) constructor.newInstance();
//			System.out.println(Singleton.getInstance() == singleton);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
