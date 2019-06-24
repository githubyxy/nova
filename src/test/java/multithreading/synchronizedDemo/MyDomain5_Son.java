package multithreading.synchronizedDemo;

/**
 * 测试异常会释放锁
 *
 * @author yuxiaoyu
 */
public class MyDomain5_Son extends MyDomain5_Father {

    @Override
    public void testMethod() {
        try {
            super.testMethod();
            System.out.println(System.currentTimeMillis() + "	" + Thread.currentThread().getName() + "进入子类非synchronized方法");
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
