package multithreading.synchronizedDemo;

/**
 * 测试异常会释放锁
 *
 * @author yuxiaoyu
 */
public class MyDomain5_Father {

    synchronized public void testMethod() {
        try {
            System.out.println(System.currentTimeMillis() + "	" + Thread.currentThread().getName() + "进入父类synchronized方法");
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
