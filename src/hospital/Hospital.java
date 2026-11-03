package hospital;

import java.util.Scanner;

public class Hospital {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        int capacity;
        int doctorsCount;
        int patientsCount;

        System.out.println("Hospital Emergency Room Simulation");

        System.out.print("Waiting room capacity: ");
        capacity = input.nextInt();

        System.out.print("Number of doctors: ");
        doctorsCount = input.nextInt();

        System.out.print("Number of patients: ");
        patientsCount = input.nextInt();

        if (capacity <= 0 || doctorsCount <= 0 || patientsCount <= 0) {
            Logger.log("Error: All inputs must be positive numbers.");
            input.close();
            return;
        }

        Logger.log("Hospital simulation is starting...");

        WaitingRoom waitingRoom = new WaitingRoom(capacity);

        TreatmentRoomManager treatmentRooms = new TreatmentRoomManager();

        Doctor[] doctors = new Doctor[doctorsCount];

        Patient[] patients = new Patient[patientsCount];

        for (int i = 0; i < doctorsCount; i++) {

            doctors[i] = new Doctor(
                    i + 1,
                    waitingRoom,
                    treatmentRooms,
                    patientsCount);
        }

        for (int i = 0; i < patientsCount; i++) {

            patients[i] = new Patient(
                    i + 1,
                    waitingRoom);
        }

        Logger.log("Patients arriving:");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < patientsCount; i++) {
            sb.append("P").append(patients[i].getPatientId());
            if (i < patientsCount - 1) {
                sb.append(" ");
            }
        }
        Logger.log(sb.toString());

        for (Doctor doctor : doctors) {
            doctor.start();
        }

        for (Patient patient : patients) {
            patient.start();
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        try {

            for (Patient patient : patients) {
                patient.join();
            }

            for (Doctor doctor : doctors) {
                doctor.interrupt();
            }

            for (Doctor doctor : doctors) {
                doctor.join();
            }

        } catch (InterruptedException e) {

            Logger.log("Hospital simulation was interrupted.");

            Thread.currentThread().interrupt();

            input.close();
            return;
        }

        Logger.log("All patients have been treated.");
        Logger.log("Simulation completed.");

        input.close();
    }
}
