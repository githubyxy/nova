package multithreading.threadDemo;//package multithreading.threadDemo;
//
//import org.junit.Test;
//
//public class ThreadMainTest {
//
//	@Test
//	public void test1() {
//		// 继承Thread，重写父类的run()方法。
//		Mythread mt0 = new Mythread();
//	    mt0.start();
//	        
//	    for (int i = 0; i < 5; i++) {
//	    		// Thread.currentThread():返回代码段正在被哪个线程调用的信息
//	        System.out.println(Thread.currentThread().getName() + "在运行！");
//	    }
//	}
//	@Test
//	public void test2() {
//		// 实现Runnable接口。和继承自Thread类差不多，不过实现Runnable后，还是要通过一个Thread来启动：
//		Mythread2 mt0 = new Mythread2();
//		new Thread(mt0).start();
//		
//		for (int i = 0; i < 5; i++) {
//			System.out.println(Thread.currentThread().getName() + "在运行！");
//		}
//	}
//	
//	/**
//	 * 多个线程实例，其内部实例变量不共享
//	 * 每个线程实例，都会逐步减少自己的count值
//	 */
//	@Test
//	public void test3() {
//		Mythread3 a = new Mythread3("A");
//		Mythread3 b = new Mythread3("B");
//		Mythread3 c = new Mythread3("C");
//		a.start();
//		b.start();
//		c.start();
//	}
//	/**
//	 * 多个线程实例，访问同一个实例变量，内部实例变量共享
//	 * 非线程安全问题：多个线程对同一个对象中的同一个实例变量操作时
//	 */
//	@Test
//	public void test3_2() {
//		Mythread3_2 mythread3_2 = new Mythread3_2();
//		Thread a = new Thread(mythread3_2, "A");
//		Thread b = new Thread(mythread3_2, "B");
//		Thread c = new Thread(mythread3_2, "C");
//		Thread d = new Thread(mythread3_2, "D");
//		Thread e = new Thread(mythread3_2, "E");
//		a.start();
//		b.start();
//		c.start();
//		d.start();
//		e.start();
//	}
//	
//	@Test
//	public void test4() {
//		Mythread4 a = new Mythread4();
//		a.start();
////		a.run();
//	}
//	@Test
//	public void test7() {
//		Mythread7 a = new Mythread7();
//		a.start();
////		a.run();
//	}
//	/**
//	 * 由于this和Thread.currentThread()之间的差异
//	 * 只有在当前实例对象调用了start方法时，this.isAlive()才返回true
//	 */
//	@Test
//	public void test7_2() {
//		Mythread7_2 c = new Mythread7_2();
//
////		Thread t1 = new Thread(c);
////		System.out.println("main begin t1 isAlive=" + t1.isAlive());
////		t1.setName("A");
////		t1.start();
////		System.out.println("main end t1 isAlive=" + t1.isAlive());
//		
//		System.out.println("main begin t1 isAlive=" + c.isAlive());
//		c.setName("A");
//		c.start();
//		System.out.println("main end t1 isAlive=" + c.isAlive());
//	}
//	
//	@Test
//	public void test11() {
//		Mythread11 a = new Mythread11();
//		a.start();
//		// 直接调用interrupt 线程方法并不会立刻停止
//		a.interrupt();
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	@Test
//	public void test11_2() {
//		try {
//			Mythread11_2 a = new Mythread11_2();
//			a.start();
//			Thread.sleep(1);
//			// 直接调用interrupt 线程方法并不会立刻停止
//			a.interrupt();
//			System.out.println(Thread.interrupted());
//			System.out.println(Thread.interrupted());
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	@Test
//	public void test11_3() {
//		try {
//			Mythread11_3 a = new Mythread11_3();
//			a.start();
//			Thread.sleep(1);
//			// 直接调用interrupt 线程方法并不会立刻停止
//			a.interrupt();
//			System.out.println(a.isInterrupted());
//			System.out.println(a.isInterrupted());
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	/**
//	 * 测试 守护线程必须等待非守护线程执行完毕时（main线程）,会自动退出
//	 */
//	@Test
//	public void test21() {
//		try {
//			Mythread21 thread = new Mythread21();
//			thread.setDaemon(true);
//			thread.start();
//			Thread.sleep(5000);
//			System.out.println(Thread.currentThread().isDaemon());
//			System.out.println("我离开thread对象了");
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//}
