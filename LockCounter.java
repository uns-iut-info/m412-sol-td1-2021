/*
 * M412 2019-2020: distributed programming
 */

class LockCounter {
	private int c = 0;
	private final Object lock = new Object();

	void increment() {
		synchronized (lock) {
			c++;
		}
	}

	void decrement() {
		synchronized (lock) {
			c--;
		}
	}

	int value() {
		synchronized (lock) { // pas utile
			return c;
		}
	}

}
