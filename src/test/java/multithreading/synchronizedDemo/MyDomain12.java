package multithreading.synchronizedDemo;

/**
 * @author yuxiaoyu
 */
public class MyDomain12 {

    public synchronized static void printA() {
        try {
            System.out.println(
                    "线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "进入printA()方法");
            Thread.sleep(3000);
            System.out.println(
                    "线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "离开printA()方法");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void printB() {
        // synchronized静态方法持有的是对当前.java文件对应的Class类加锁
        synchronized (MyDomain12.class) {
            System.out.println(
                    "线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "进入printB()方法");
            System.out.println(
                    "线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "离开printB()方法");
        }

    }
//	public synchronized static void printB() {
//		System.out.println(
//				"线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "进入printB()方法");
//		System.out.println(
//				"线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "离开printB()方法");
//		
//	}

    public synchronized void printC() {
        System.out.println(
                "线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "进入printC()方法");
        System.out.println(
                "线程名称为：" + Thread.currentThread().getName() + "在" + System.currentTimeMillis() + "离开printC()方法");
    }

}
