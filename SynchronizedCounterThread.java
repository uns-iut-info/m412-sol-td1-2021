/*
 * M412 2019-2020: distributed programming
 */

public class SynchronizedCounterThread implements Runnable {
	private SynchronizedCounter count;
	private int nInc;
	private int nDec;

	private SynchronizedCounterThread(SynchronizedCounter count, int nInc, int nDec) {
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
		SynchronizedCounter count = new SynchronizedCounter();
		System.out.println("Initial value for count: " + count.value());
		SynchronizedCounterThread sct1 = new SynchronizedCounterThread(count, 10000, 5000);
		Thread t1 = new Thread(sct1);
		SynchronizedCounterThread sct2 = new SynchronizedCounterThread(count, 5000, 10000);
		Thread t2 = new Thread(sct2);
		
		t1.start();
		t2.start();
		t1.join(); // si on ne fait pas join : résultat faux
		t2.join();
		
		System.out.println("Final value for count: " + count.value());
	}
}
