package hospital;

public class Patient extends Thread {

    private final int patientId;
    private final WaitingRoom waitingRoom;
    private final Semaphore treated = new Semaphore(0);

    public Patient(int patientId, WaitingRoom waitingRoom) {
        this.patientId = patientId;
        this.waitingRoom = waitingRoom;
    }

    public int getPatientId() {
        return patientId;
    }

    public void finishTreatment() {
        treated.release();
    }

    @Override
    public void run() {
        try {
            Logger.log("P" + patientId + " arrived.");

            waitingRoom.addPatient(this);
            Logger.log("P" + patientId + " entered the waiting room.");

            waitForTreatment();

            Logger.log("P" + patientId + " left.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void waitForTreatment() throws InterruptedException {
        treated.acquire();
    }
}