package hospital;

import java.util.LinkedList;
import java.util.Queue;

public class WaitingRoom {

    private final Queue<Patient> queue;
    public final int capacity;

    private final Semaphore emptySlots;
    private final Semaphore fullSlots;
    private final Semaphore mutex;

    public WaitingRoom(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException("Capacity must be positive");
        this.capacity = capacity;
        this.queue = new LinkedList<>();
        this.emptySlots = new Semaphore(capacity);
        this.fullSlots = new Semaphore(0);
        this.mutex = new Semaphore(1);
    }

    public boolean tryEnter(Patient p) throws InterruptedException {
        mutex.acquire();
        if (!emptySlots.tryAcquire(0)) {
            mutex.release();
            return false;
        }
        queue.add(p);
        mutex.release();
        fullSlots.release();
        return true;
    }

    public void enterWaitingRoom(Patient p) throws InterruptedException {
        emptySlots.acquire();
        mutex.acquire();
        queue.add(p);
        mutex.release();
        fullSlots.release();
    }

    public void reenterWaitingRoom(Patient p) throws InterruptedException {
        mutex.acquire();
        queue.add(p);
        mutex.release();
        fullSlots.release();
    }

    public Patient removePatient() throws InterruptedException {
        fullSlots.acquire();
        mutex.acquire();
        Patient p = queue.poll();
        mutex.release();
        return p;
    }

    public synchronized boolean isFull() {
        return emptySlots.availablePermits() == 0;
    }

    public synchronized int size() {
        return queue.size();
    }

    public void confirmDeparture() {
        emptySlots.release();
    }
}