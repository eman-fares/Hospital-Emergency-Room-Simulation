
package hospital;

import java.util.LinkedList;
import java.util.Queue;

public class WaitingRoom {

    private Queue<Patient> queue;
    private int capacity;

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

    public void addPatient(Patient patient) throws InterruptedException {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }

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

        boolean removed = false;
        boolean locked = false;
        Patient patient = null;

        try {
            mutex.acquire();
            locked = true;

            patient = queue.poll();
            removed = patient != null;

        } finally {
            if (locked) {
                mutex.release();
            }

            if (!removed) {
                fullSlots.release();
            }
        }

        emptySlots.release();
        return patient;
    }
}
