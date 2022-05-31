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

    PassengerRange pr;
    PlaneStates state;
    Airport airport;
    ATC atc;
    AtomicInteger passengerCount = new AtomicInteger(0);

    Plane(int id, Airport airport, ATC atc) {
        this.id = id;
        this.airport = airport;
        this.atc = atc;
        this.noOfPassenger = (int) Math.floor(Math.random() * (PassengerRange.MAX.getValue() - PassengerRange.MIN.getValue() + 1) + PassengerRange.MIN.getValue());
        this.state = PlaneStates.WANTTOLAND;
        System.out.println("Plane " + id + " has been generated.");
    }

    public void run() {
        while (this.state == PlaneStates.WANTTOLAND) {
            System.out.println("Plane " + this.id + " wants to land! Requesting permission from ATC...");
            atc.checkRunway(this);
        }
        while (this.state == PlaneStates.ONRUNWAYARRIVAL) {
            try {
                for (int i = 1; i < 4; i++) {
                    switch (i) {
                        case 1:
                            System.out.println("Plane " + this.id + " is deploying landing gear...");
                            break;
                        case 2:
                            System.out.println("Plane " + this.id + " had landed safely...");
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

        while (this.state == PlaneStates.PASSENGERDISEMBARKING) {
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
                this.state = PlaneStates.PASSENGERONBOARDING;
            }
        }

        while (this.state == PlaneStates.PASSENGERONBOARDING) {

        }
    }

}
