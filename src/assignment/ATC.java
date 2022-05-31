/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ryan Ng
 */
class ATC implements Runnable {

    //Plane plane;
    Runway runway;
    Gate gate;
    Airport airport;
    PlaneStates state;

    ATC( Runway runway, Gate gate, Airport airport) {
        //this.plane = plane;
        this.runway = runway;
        this.gate = gate;
        this.airport = airport;
    }

    public void run() { 
        while (plane.state == PlaneStates.WANTTOLAND) {
            System.out.println("Plane " + plane.id + " wants to land! Checking available runways...");
            if (runway.runwaySem.availablePermits() != 0
                    //&& airport.listOfPlanes.peek() == plane
                    && airport.airportCapacity.get() != airport.MAX_CAPACITY) {
                System.out.println("Runway is available");
                try {
                    airport.landPlaneOnRunway();
                    runway.runwaySem.acquire();
                    airport.airportCapacity.incrementAndGet();
                    System.out.println("Plane " + plane.id + " has taken runway.");
                    plane.state = PlaneStates.ONRUNWAYARRIVAL;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                try { 
                    plane.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ATC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                
        }
        while (plane.state == PlaneStates.ONRUNWAYARRIVAL) {
            System.out.println("Plane " + plane.id + " is waiting for available gates...");
            if (gate.gateSem.availablePermits() != 0) {
                System.out.println("Gates are available.");
                try {
                    gate.gateSem.acquire();
                    System.out.println("Plane " + plane.id + " has taken a Gate.");
                    runway.runwaySem.release();

                    plane.state = PlaneStates.ATGATE;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        while (plane.state == PlaneStates.ATGATE) {
            System.out.println("Plane " + plane.id + " getting ready to disembark its passengers...");
            System.out.println("Gate ternminals are opening...");
            plane.state = PlaneStates.PASSENGERDISEMBARKING;
        }
        while (plane.state == PlaneStates.PASSENGERDISEMBARKING) {
            Passenger passenger = new Passenger(plane);
            passenger.run();
            plane.state = PlaneStates.PASSENGERONBOARDING;
        }
        while (plane.state == PlaneStates.PASSENGERONBOARDING) {
            Passenger passenger = new Passenger(plane);
            passenger.run();
            plane.state = PlaneStates.LEAVEGATE;
        }
    }
}
