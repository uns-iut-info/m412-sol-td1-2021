/*
 * M412 2019-2020: distributed programming
 */

public class CounterThread implements Runnable {
	private Counter count;
	private int nInc;
	private int nDec;

	private CounterThread(Counter count, int nInc, int nDec) {
		this.count = count;
		this.nInc = nInc;
		this.nDec = nDec;
	}

	public void run() {
		for (int i = 0; i < nInc; i++) {
			count.increment();
		}
		for (int i = 0; i < nDec; i++) {
			count.decrement();
		}
	}

	public static void main(String[] args) throws InterruptedException {

		System.out.println("Memory consistency problem demo");
		Counter count = new Counter();
		System.out.println("Initial value for count: " + count.value());

		CounterThread ct1 = new CounterThread(count, 100000, 100000);
		Thread t1 = new Thread(ct1);
		CounterThread ct2 = new CounterThread(count, 100000, 100000);
		Thread t2 = new Thread(ct2);

		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
		
		System.out.println("Final value for count: " + count.value());

	}

}
