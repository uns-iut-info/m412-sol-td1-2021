/*
 * M412 2019-2020: distributed programming
 */

class SynchronizedCounter {
	private int c = 0;

	synchronized void increment() {
		c++;
	}

	synchronized void decrement() {
		c--;
	}

	synchronized int value() { // pas utile
		return c;
	}

}
