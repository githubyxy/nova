package test;

public class MyTest extends Thread {


        boolean flag = false;
        int i = 0;

        @Override
        public void run() {
            while (!flag) {
                i++;
                System.out.println("This is running..." + flag);
            }
        }

        public static void main(String[] args) throws Exception {
            MyTest vt = new MyTest();
            vt.start();
            Thread.sleep(2000);
            vt.flag = true;
            //System.out.println("stope" + vt.i);
        }

}
