/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.LinkedList;
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

    private final String threadName = "ATC: ";
    LinkedList<Plane> listOfPlanes;

    ATC(Runway runway, Gate gate, Airport airport) {
        //this.plane = plane;
        this.runway = runway;
        this.gate = gate;
        this.airport = airport;
        listOfPlanes = new LinkedList<Plane>();
    }

    public void run() {
//        while (plane.state == PlaneStates.PASSENGERDISEMBARKING) {
//            Passenger passenger = new Passenger(plane);
//            passenger.run();
//            plane.state = PlaneStates.PASSENGERONBOARDING;
//        }
//        while (plane.state == PlaneStates.PASSENGERONBOARDING) {
//            Passenger passenger = new Passenger(plane);
//            passenger.run();
//            plane.state = PlaneStates.LEAVEGATE;
//        }
    }

    public void checkRunway(Plane plane) {
        System.out.println(threadName + "Received landing request from Plane " + plane.id + ", checking available runways...");
        if (runway.runwaySem.availablePermits() != 0
                //&& listOfPlanes.peek() == plane
                && airport.airportCapacity.get() != airport.MAX_CAPACITY) {
            System.out.println(threadName + "Runway is available!");
            try {
                runway.runwaySem.acquire();
                airport.airportCapacity.incrementAndGet();
                System.out.println(threadName + "Plane " + plane.id + " has taken runway.");
                plane.state = PlaneStates.ONRUNWAYARRIVAL;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            if (listOfPlanes.indexOf(plane) == -1) {
                System.out.println(threadName + "The airport is full now, please wait in queue.");
                addPlaneToQueue(plane);
            }
            synchronized (listOfPlanes) {
                try {
                    listOfPlanes.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ATC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void checkGate(Plane plane) {
        System.out.println(threadName + "Plane " + plane.id + " is waiting for available gates...");
        if (gate.gateSem.availablePermits() != 0) {
            System.out.println("Gates are available.");
            try {
                gate.gateSem.acquire();
                System.out.println("Plane " + plane.id + " has taken a Gate.");
                runway.runwaySem.release();
                landPlaneOnRunway(plane);
                plane.state = PlaneStates.ATGATE;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }    
    
    synchronized void addPlaneToQueue(Plane plane) {
        synchronized (listOfPlanes) {
            ((LinkedList<Plane>) listOfPlanes).offer(plane);
            System.out.println("Plane " + plane.id + " has been entered into waiting queue.");

//            if (listOfPlanes.size() == 0) {
//                listOfPlanes.notify();
//            }
        }
    }

    public void landPlaneOnRunway(Plane plane) {
        synchronized (listOfPlanes) {
            if (airport.airportCapacity.get() != airport.MAX_CAPACITY) {
                System.out.println("ATC found a plane in queue.");
                listOfPlanes.pollFirst();
                listOfPlanes.notify();
            }
            else
                try {
                    listOfPlanes.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ATC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
