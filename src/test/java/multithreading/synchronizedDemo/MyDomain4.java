package multithreading.synchronizedDemo;

/**
 * 测试异常会释放锁
 *
 * @author yuxiaoyu
 */
public class MyDomain4 {

    synchronized public void testMethod() {
        try {
            System.out.println(Thread.currentThread().getName() + "进入synchronized方法");
            long l = Integer.MAX_VALUE;
            while (true) {
                long lo = 2 / l;
                l--;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
