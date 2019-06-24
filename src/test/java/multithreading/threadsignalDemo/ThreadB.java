package multithreading.threadsignalDemo;

public class ThreadB extends Thread {

    private MyList list;

    public ThreadB(MyList list) {
        super();
        this.list = list;
    }


    @Override
    public void run() {
        try {
            while (true) {
//				System.out.println("threadB开始执行");
                Thread.sleep(1000);
//				System.out.println("list=" + list.size());
                if (list.size() == 5) {
                    System.out.println("==5 了  线程 b 要退出了");
                    throw new InterruptedException();
                }
                if (list.size() == 10) {
                    System.out.println("ThreadA执行结束了");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
