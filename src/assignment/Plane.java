/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ryan Ng
 */
class Plane extends Thread {

    int id;
    int noOfPassenger;
    int passengersToBoard;

    boolean disembarked = false;
    boolean refueled = false;

    PassengerRange pr;
    PlaneStates state;
    Airport airport;
    ATC atc;
    AtomicInteger passengerCount = new AtomicInteger(0);

    Plane(int id, Airport airport, ATC atc) {
        this.id = id;
        this.airport = airport;
        this.atc = atc;
        this.passengersToBoard = (int) Math.floor(Math.random() * (PassengerRange.MAX.getValue() - PassengerRange.MIN.getValue() + 1) + PassengerRange.MIN.getValue());
        this.noOfPassenger = (int) Math.floor(Math.random() * (PassengerRange.MAX.getValue() - PassengerRange.MIN.getValue() + 1) + PassengerRange.MIN.getValue());
        this.state = PlaneStates.WANTTOLAND;
        System.out.println("Plane " + id + " has been generated.");
    }

    public void run() {
        while (this.state == PlaneStates.WANTTOLAND) {
            System.out.println("Plane " + this.id + " wants to land! Requesting permission from ATC...");
            atc.checkRunwayForArrival(this);
        }
        while (this.state == PlaneStates.ONRUNWAYARRIVAL) {
            try {
                for (int i = 1; i < 4; i++) {
                    switch (i) {
                        case 1:
                            System.out.println("Plane " + this.id + " is deploying landing gear...");
                            break;
                        case 2:
                            System.out.println("Plane " + this.id + " has landed safely...");
                            break;
                        case 3:
                            System.out.println("Plane " + this.id + " is coasting to the gate terminals...");
                            break;
                    }
                    Thread.sleep(1000);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            System.out.println("Plane " + this.id + " is waiting for available gates...");
            atc.checkGate(this);
        }

        while (this.state == PlaneStates.ATGATE) {
            System.out.println("Plane " + this.id + " successfully docked at gate...");
            System.out.println("Plane " + this.id + " getting ready to disembark its passengers...");
            this.state = PlaneStates.PASSENGERDISEMBARKING;
        }
        boolean passengerGeneratorSpawned = false;
        boolean fuelTruckCheckingThreadSpawned = false;

        while (this.state == PlaneStates.PASSENGERDISEMBARKING) {
            if (!fuelTruckCheckingThreadSpawned) {
                Thread fuelTruckCheckingThread = new Thread(() -> {
                    while (true) {
                        atc.checkFuelTruck(this);
                    }
                });
                fuelTruckCheckingThread.start();
                fuelTruckCheckingThreadSpawned = true;
            }

            if (!passengerGeneratorSpawned) {
                Thread passengerGenerator = new Thread(() -> {
                    if (!this.disembarked) {
                        this.passengerCount.set(noOfPassenger);
                        System.out.println("Passengers waiting to disembark Plane " + this.id + ": " + noOfPassenger);
                        for (int i = 1; i <= noOfPassenger; i++) {
                            Passenger passenger = new Passenger(i, this);
                            passenger.start();
                            try {
                                passenger.join();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        if (this.passengerCount.get() == 0) {
                            System.out.println("All passengers have disembarked Plane " + this.id + ".");
                            this.disembarked = true;
                        }
                    }
                });
                passengerGenerator.start();
                passengerGeneratorSpawned = true;
            }
            try {
                this.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
            }
            //System.out.println( this.disembarked + "," + this.refueled + "," + this.id);
            if (this.disembarked && this.refueled) {
                this.state = PlaneStates.CLEANINGANDRESTOCKING;
            }
        }

        while (this.state == PlaneStates.CLEANINGANDRESTOCKING) {
            try {
                Janitor jan = new Janitor("Janitor", this);
                Crew crew = new Crew("Crew Member", this);
                jan.start();
                crew.start();
                
                jan.join();
                crew.join();
                this.state = PlaneStates.PASSENGERONBOARDING;
            } catch (InterruptedException ex) {
                Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        while (this.state == PlaneStates.PASSENGERONBOARDING) {
            this.passengerCount.set(passengersToBoard);
            System.out.println("Passengers waiting to board Plane " + this.id + ": " + passengersToBoard);
            for (int i = 1; i <= passengersToBoard; i++) {
                Passenger passenger = new Passenger(i, this);
                passenger.start();
                try {
                    passenger.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (this.passengerCount.get() == 0) {
                System.out.println("All passengers have boarded Plane " + this.id + ".");
                this.state = PlaneStates.LEAVEGATE;
            }
        }
        
        if (this.state == PlaneStates.LEAVEGATE){
            System.out.println("Plane " + this.id + " is ready to leave gate. Asking permisson for departure..." );
        }        
        while (this.state == PlaneStates.LEAVEGATE)
        {
            atc.checkRunwayForDeparture(this);
        }
        
        while (this.state == PlaneStates.ONRUNWAYDEPARTURE)
        {
            try {
                for (int i = 1; i < 4; i++) {
                    switch (i) {
                        case 1:
                            System.out.println("Plane " + this.id + " is coasting to runway...");
                            break;
                        case 2:
                            System.out.println("Plane " + this.id + " is accelerating...");
                            break;
                        case 3:
                            System.out.println("Plane " + this.id + " is gaining altitude...");
                            break;
                    }
                    Thread.sleep(1000);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            atc.gate.gateSem.release();
            this.state = PlaneStates.DEPARTED;
        }
        
        if (this.state == PlaneStates.DEPARTED)
        {
            System.out.println("Plane " + this.id + " has left the airport." );
            atc.runway.runwaySem.release();
            airport.airportCapacity.decrementAndGet();
            atc.landPlaneOnRunway();
        }
    }

}
