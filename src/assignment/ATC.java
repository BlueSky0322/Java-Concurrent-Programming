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

    FuelTruck fuelTruck;
    Runway runway;
    Gate gate;
    Airport airport;
    PlaneStates state;
     
    private boolean checkRunwayDepart = true;
    private final String threadName = "ATC: ";
    LinkedList<Plane> listOfPlanes;

    ATC(FuelTruck fuelTruck, Runway runway, Gate gate, Airport airport) {
        this.fuelTruck = fuelTruck;
        this.runway = runway;
        this.gate = gate;
        this.airport = airport;
        listOfPlanes = new LinkedList<Plane>();
    }

    public void run() {
        System.out.println("ATC is online, ready to start simulation.");
    }

    public void checkRunwayForArrival(Plane plane) {
        System.out.println(threadName + "Received landing request from Plane " + plane.id + ", checking available runways...");
        if (runway.runwaySem.availablePermits() != 0
                && airport.airportCapacity.get() != airport.MAX_CAPACITY) {
            System.out.println(threadName + "Runway is available for arrival!");
            try {
                runway.runwaySem.acquire();
                airport.airportCapacity.incrementAndGet();
                System.out.println(threadName + "Plane " + plane.id + " has taken runway for arrival.");
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

    public void checkRunwayForDeparture(Plane plane) {
        if (checkRunwayDepart) {
            System.out.println(threadName + "Received departure request from Plane " + plane.id + ", checking available runways...");
            checkRunwayDepart = false;
        }
        try {
            Plane.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(ATC.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (runway.runwaySem.availablePermits() != 0) {
            System.out.println(threadName + "Runway is available for departure!");
            try {
                runway.runwaySem.acquire();
                System.out.println(threadName + "Plane " + plane.id + " has taken runway for departure.");
                plane.state = PlaneStates.ONRUNWAYDEPARTURE;
                checkRunwayDepart = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkGate(Plane plane) {
        if (gate.gateSem.availablePermits() != 0) {
            System.out.println(threadName + "Gates are available.");
            try {
                gate.gateSem.acquire();
                System.out.println(threadName + "Plane " + plane.id + " has taken a gate, runway is now available again.");
                runway.runwaySem.release();
                landPlaneOnRunway();
                plane.state = PlaneStates.ATGATE;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkFuelTruck(Plane plane) {
        //while (true) {
        if (fuelTruck.fuelTruckSem.availablePermits() != 0 && !plane.refueled) {
            System.out.println(threadName + "Fuel Truck is available!");
            try {
                fuelTruck.fuelTruckSem.acquire();
                System.out.println(threadName + "Plane " + plane.id + " is currently refuelling...");
                plane.sleep(1000);
                fuelTruck.fuelTruckSem.release();
                plane.refueled = true;
                System.out.println("Refueled Plane " + plane.id + ".");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //}
    }

    public void addPlaneToQueue(Plane plane) {
        synchronized (listOfPlanes) {
            ((LinkedList<Plane>) listOfPlanes).offer(plane);
            System.out.println(threadName + "Plane " + plane.id + " has been entered into waiting queue.");
            System.out.println("Planes in queue now: " + listOfPlanes.size());
        }
    }

    public void landPlaneOnRunway() {
        synchronized (listOfPlanes) {
            if (airport.airportCapacity.get() != airport.MAX_CAPACITY && listOfPlanes.size() != 0) {
                System.out.println(threadName + "Found a plane in queue.");
                listOfPlanes.pollFirst();
                listOfPlanes.notify();
            } else {
                System.out.println(threadName + "Queue empty. Simulation has ended.");
            }
        }
    }
}
