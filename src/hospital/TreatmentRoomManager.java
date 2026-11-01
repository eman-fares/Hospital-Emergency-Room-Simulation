
package hospital;

public class TreatmentRoomManager {

    private Semaphore treatmentRooms;

    public TreatmentRoomManager() {
        this.treatmentRooms = new Semaphore(2);
    }

    public boolean acquireRoom() throws InterruptedException {
        return treatmentRooms.tryAcquire(3000);
    }

    public void releaseRoom() {
        treatmentRooms.release();
    }
}

