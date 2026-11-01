# Hospital Emergency Room Simulation

A Java-based simulation of a hospital emergency room using **Multithreading**, **Semaphores**, and the **Producer-Consumer Pattern**.

## Overview

This project simulates the workflow of a hospital emergency room, where multiple patients arrive and wait for available doctors. Doctors serve patients while sharing a limited number of treatment rooms.

The system uses synchronization mechanisms to coordinate access to shared resources and prevent race conditions.

## Features

- Multiple patient and doctor threads.
- Bounded waiting room with a configurable capacity.
- Producer-Consumer synchronization.
- Semaphore-based resource management.
- Two shared treatment rooms.
- Timed treatment room acquisition with a 3-second timeout.
- Thread-safe access to shared resources.
- FIFO patient handling in the waiting room.

## Technologies

- Java
- Multithreading
- Semaphores
- Synchronization
- Producer-Consumer Pattern
- Bounded Buffer

## Project Structure

```text
Hospital-Emergency-Room-Simulation/
│
├── src/
│   └── hospital/
│       ├── Hospital.java
│       ├── Patient.java
│       ├── Doctor.java
│       ├── Semaphore.java
│       ├── WaitingRoom.java
│       └── TreatmentRoomManager.java
│
├── README.md
└── .gitignore
```

*Note: The project structure may be updated as the team integrates the different components.*

## How It Works

1. Patients arrive at the hospital.
2. Patients enter the waiting room if space is available.
3. Doctors wait for patients to become available.
4. A doctor takes a patient from the waiting room.
5. The doctor attempts to acquire one of the two treatment rooms.
6. If no room becomes available within 3 seconds, the doctor follows the specified waiting-room flow.
7. After treatment, the doctor releases the treatment room.
8. The process continues until all patients have been treated.

## Synchronization

The project uses semaphores to manage access to shared resources:

- **Waiting Room:** Controls the number of available slots and waiting patients.
- **Mutex:** Protects access to the shared patient queue.
- **Treatment Rooms:** Limits simultaneous treatment sessions to two doctors.

## Team

Developed as part of the CS241 / SCS241 assignment.

## Contributors



---

**Course:** CS241 / SCS241  
**Semester:** Fall 2026
