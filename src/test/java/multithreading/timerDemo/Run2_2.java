package multithreading.timerDemo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Run2_2 {
    static public class MyTaskA extends TimerTask {
        @Override
        public void run() {
            try {
                System.out.println("A运行了！时间为：" + new Date());
                Thread.sleep(4000);
                System.out.println("A结束了！时间为：" + new Date());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            MyTaskA taskA = new MyTaskA();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = "2018-06-19 14:18:00";
            Timer timer = new Timer();
            Date dateRef = sdf.parse(dateString);
            System.out.println("字符串时间：" + dateRef.toLocaleString() + " 当前时间："
                    + new Date().toLocaleString());
            timer.scheduleAtFixedRate(taskA, dateRef, 3000);
            timer.scheduleAtFixedRate(taskA, 1000, 3000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
