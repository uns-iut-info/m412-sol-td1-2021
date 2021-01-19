/*
 * M412 2019-2020: distributed programming
 */

public class LockCounterThread implements Runnable {
	private LockCounter count;
	private int nInc;
	private int nDec;

	private LockCounterThread(LockCounter count, int nInc, int nDec) {
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

		LockCounter count = new LockCounter();
		System.out.println("Initial value for count: " + count.value());

		LockCounterThread sct1 = new LockCounterThread(count, 10000, 5000);
		Thread t1 = new Thread(sct1);
		LockCounterThread sct2 = new LockCounterThread(count, 10000, 10000);
		Thread t2 = new Thread(sct2);

		t1.start();
		t2.start();

		t2.join();
		t1.join();

		System.out.println("Final value for count: " + count.value());
	}
}
