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
        long deadline = System.currentTimeMillis() + timeoutMs;
        long duration = timeoutMs;
        value--;
        if (value < 0) {
            while (value < 0) {
                if (duration <= 0) {
                    value++;
                    return false;
                }
                wait(duration);
                duration = deadline - System.currentTimeMillis();
            }
        }
        return true;
    }
}
