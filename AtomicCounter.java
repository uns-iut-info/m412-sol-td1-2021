/*
 * M412 2019-2020: distributed programming
 */

import java.util.concurrent.atomic.AtomicInteger;

class AtomicCounter {
    private AtomicInteger c = new AtomicInteger(0);

    void increment() {
        //c.set(c.incrementAndGet()); // false
        c.incrementAndGet();
    }

    void decrement() {
        c.decrementAndGet();
    }

    int value() {
        return c.get();
    }
}