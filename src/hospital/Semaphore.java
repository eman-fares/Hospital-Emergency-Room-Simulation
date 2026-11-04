package hospital;

public class Semaphore {

    protected int value = 0;

    public Semaphore() {
        value = 0;
    }

    public Semaphore(int initial) {
        value = initial;
    }

    public synchronized void acquire() throws InterruptedException {
        value--;
        if (value < 0) {
            wait();
        }
    }

    public synchronized void release() {
        value++;
        if (value <= 0) {
            notify();
        }
    }

    public synchronized boolean tryAcquire(long timeoutMs) throws InterruptedException {
        if (value > 0) {
            value--;
            return true;
        }
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (value <= 0) {
            long remaining = deadline - System.currentTimeMillis();
            if (remaining <= 0)
                return false;
            wait(remaining);
        }
        value--;
        return true;
    }

    public synchronized int availablePermits() {
        return Math.max(value, 0);
    }
}
