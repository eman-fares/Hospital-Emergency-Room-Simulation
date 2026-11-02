package hospital;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CopyOnWriteArrayList;

public class Doctor extends Thread {

    private int doctorId;
    private WaitingRoom waitingRoom;
    private TreatmentRoomManager treatmentRooms;
    private int patientsCount;

    private static AtomicInteger completedPatients = new AtomicInteger(0);
    private static CopyOnWriteArrayList<Doctor> allDoctors =
            new CopyOnWriteArrayList<>();

    public Doctor(int doctorId,
                  WaitingRoom waitingRoom,
                  TreatmentRoomManager treatmentRooms,
                  int patientsCount) {

        this.doctorId = doctorId;
        this.waitingRoom = waitingRoom;
        this.treatmentRooms = treatmentRooms;
        this.patientsCount = patientsCount;

        if (doctorId == 1) {
            completedPatients.set(0);
            allDoctors.clear();
        }

        allDoctors.add(this);
    }

    @Override
    public void run() {

        while (completedPatients.get() < patientsCount) {

            Patient patient = null;
            boolean roomAcquired = false;
            boolean treatmentCompleted = false;

            try {

                patient = waitingRoom.removePatient();

                Logger.log("Doctor " + doctorId
                        + " is assigned to P"
                        + patient.getPatientId() + ".");

                Logger.log("Doctor " + doctorId
                        + " is trying to acquire a treatment room.");

                roomAcquired = treatmentRooms.acquireRoom();

                if (!roomAcquired) {

                    Logger.log("Doctor " + doctorId
                            + " could not acquire a treatment room.");

                    Logger.log("Doctor " + doctorId
                            + " is sending P"
                            + patient.getPatientId()
                            + " back to the waiting room.");

                    waitingRoom.addPatient(patient);

                    Logger.log("Doctor " + doctorId
                            + " is now available.");

                    continue;
                }

                Logger.log("Doctor " + doctorId
                        + " acquired a treatment room.");

                Logger.log("Doctor " + doctorId
                        + " begins treating P"
                        + patient.getPatientId() + ".");

                int treatmentTime =
                        (int) (Math.random() * 2000) + 1000;

                Thread.sleep(treatmentTime);

                Logger.log("Doctor " + doctorId
                        + " finished treating P"
                        + patient.getPatientId() + ".");

                treatmentCompleted = true;

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                Logger.log("Doctor " + doctorId
                        + " was interrupted.");

                return;

            } finally {

                if (roomAcquired) {
                    treatmentRooms.releaseRoom();

                    Logger.log("Doctor " + doctorId
                            + " released the treatment room.");
                }
            }

            if (treatmentCompleted) {

                int completed = completedPatients.incrementAndGet();

                Logger.log("Doctor " + doctorId
                        + " is now available.");

                Logger.log("P" + patient.getPatientId()
                        + " left.");

                if (completed >= patientsCount) {
                    for (Doctor doctor : allDoctors) {
                        if (doctor != this) {
                            doctor.interrupt();
                        }
                    }
                }
            }
        }
    }
}