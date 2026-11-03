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

        // Try to acquire an empty slot non-blockingly
        if (!emptySlots.tryAcquire(0)) {
            return false;
        }

        mutex.acquire();
        queue.add(patient);
        mutex.release();

        fullSlots.release();
        return true;
    }

    public void enterWaitingRoom(Patient patient) throws InterruptedException {
        emptySlots.acquire();
        mutex.acquire();
        queue.add(patient);
        mutex.release();
        fullSlots.release();
    }

    public void reenterWaitingRoom(Patient patient) throws InterruptedException {
        mutex.acquire();
        queue.add(patient);
        mutex.release();
        fullSlots.release();
    }

    public Patient removePatient() throws InterruptedException {
        fullSlots.acquire();

        Patient patient;
        mutex.acquire();
        patient = queue.poll();
        mutex.release();

        return patient;
    }

    public void confirmPatientDeparture() {
        emptySlots.release();
    }

    public synchronized boolean isFull() {
        return queue.size() >= capacity;
    }
}