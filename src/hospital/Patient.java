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

            if (waitingRoom.tryEnter(this)) {
                Logger.log("P" + patientId + " entered the waiting room.");
            } else {
                Logger.log("The waiting room is full.");
                Logger.log("P" + patientId + " is waiting outside for an available seat.");
                waitingRoom.enterWaitingRoom(this);
                Logger.log("P" + patientId + " entered the waiting room.");
            }

            treated.acquire();

            Logger.log("P" + patientId + " left.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}