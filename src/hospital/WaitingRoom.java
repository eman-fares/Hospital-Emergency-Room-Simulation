
package hospital;

import java.util.LinkedList;
import java.util.Queue;

public class WaitingRoom {

    private Queue<Patient> queue;
    public int capacity;

    private Semaphore emptySlots;
    private Semaphore fullSlots;
    private Semaphore mutex;

    public WaitingRoom(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }

        this.capacity = capacity;
        this.queue = new LinkedList<>();

        this.emptySlots = new Semaphore(capacity);
        this.fullSlots = new Semaphore(0);
        this.mutex = new Semaphore(1);
    }

    public boolean addPatient(Patient patient) throws InterruptedException {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }

        if (!emptySlots.tryAcquire(0)) {
            return false;
        }

        boolean added = false;
        boolean locked = false;

        try {
            mutex.acquire();
            locked = true;

            queue.add(patient);
            added = true;

        } finally {
            if (locked) {
                mutex.release();
            }

            if (!added) {
                emptySlots.release();
            }
        }

        fullSlots.release();
        return true;
    }

    public void enterWaitingRoom(Patient patient) throws InterruptedException {
        emptySlots.acquire();
        boolean added = false;
        boolean locked = false;
        try {
            mutex.acquire();
            locked = true;
            queue.add(patient);
            added = true;
        } finally {
            if (locked) {
                mutex.release();
            }
            if (!added) {
                emptySlots.release();
            }
        }
        fullSlots.release();
    }

    public Patient removePatient() throws InterruptedException {
        fullSlots.acquire();

        boolean locked = false;
        Patient patient = null;

        try {
            mutex.acquire();
            locked = true;

            patient = queue.poll();
        } finally {
            if (locked) {
                mutex.release();
            }
        }
        emptySlots.release();
        return patient;
    }

    public synchronized boolean isFull() {
        return queue.size() >= capacity;
    }
}
