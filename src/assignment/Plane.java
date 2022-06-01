/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Ng Lum Thyn TP061914
 */

class Plane extends Thread {
     
    int id;
    int passengersToDisembark;
    int passengersToBoard;

    boolean disembarked = false;
    boolean refueled = false;
    
    Statistics stat;
    PassengerRange pr;
    PlaneStates state;
    Airport airport;
    ATC atc;
    
    //AtomicInteger to keep count of the flow of passengers
    AtomicInteger passengerCount = new AtomicInteger(0);

    Plane(int id, Airport airport, ATC atc, Statistics stat) {
        this.id = id;
        this.airport = airport;
        this.atc = atc;
        this.stat = stat;
        //Generate random amount of passengers wanting to board plane
        this.passengersToBoard = (int) Math.floor(Math.random() * 
                (PassengerRange.MAX.getValue() - PassengerRange.MIN.getValue() + 1) 
                + PassengerRange.MIN.getValue());
        //Generate random amount of passengers wanting to disembark plane
        this.passengersToDisembark = (int) Math.floor(Math.random() * 
                (PassengerRange.MAX.getValue() - PassengerRange.MIN.getValue() + 1) 
                + PassengerRange.MIN.getValue());
        this.state = PlaneStates.WANTTOLAND;
        System.out.println("Plane " + id + " is arriving at the airport...");
        //this.stat.setStartTime(System.nanoTime());
    }
    
    public void run() {
        //Plane is preparing to land
        while (this.state == PlaneStates.WANTTOLAND) {
            System.out.println("Plane " + this.id + " wants to land! Requesting permission from ATC...");
            //Plane request permission to land from ATC
            atc.checkRunwayForArrival(this);
        }
        
        //this.stat.setEndTime(System.nanoTime());
        //this.stat.calculateDuration();
        //Permission granted, plane acquired runway and is performing landing processes
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
                    //sleep to simulate time taken to perform each landing task
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Plane waiting for access to gates from ATC
            System.out.println("Plane " + this.id + " is waiting for available gates...");
            //Plane request ATC to check for available gates
            atc.checkGate(this);
        }
        
        //Plane acquired a gate, prepares to disembark passengers and perform at-gate tasks
        while (this.state == PlaneStates.ATGATE) {
            System.out.println("Plane " + this.id + " successfully docked at gate...");
            System.out.println("Plane " + this.id + " is getting ready to disembark its passengers...");
            System.out.println("Plane " + this.id + " is attempting to secure a fuel truck...");
            this.state = PlaneStates.PASSENGERDISEMBARKING;
        }
        
        //flags to prevent extra Threads from spawning
        boolean passengerGeneratorSpawned = false;
        boolean fuelTruckCheckingThreadSpawned = false;
        
        //Plane performs disembarking, simultaneously refuelling 
        while (this.state == PlaneStates.PASSENGERDISEMBARKING) {
            if (!fuelTruckCheckingThreadSpawned) {
                //using lambda expressions to generate daemon fueltruck thread
                Thread fuelTruckCheckingThread = new Thread(() -> {
                    while (true) {
                        //checks availibity of Fuel Truck
                        atc.checkFuelTruck(this);
                    }
                });
                fuelTruckCheckingThread.start();
                fuelTruckCheckingThreadSpawned = true;
            }

            if (!passengerGeneratorSpawned) {
                //using lambda expressions to generate daemon passengerGenerator thread
                Thread passengerGenerator = new Thread(() -> {
                    if (!this.disembarked) {
                        this.passengerCount.set(passengersToDisembark);
                        System.out.println("Passengers waiting to disembark Plane " + this.id + ": " + passengersToDisembark);
                        //Generate set amount Passenger Thread based on noOfPassenger
                        for (int i = 1; i <= passengersToDisembark; i++) {
                            Passenger passenger = new Passenger(i, this);
                            passenger.start();
                            try {
                                passenger.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            //final state check 
            if (this.disembarked && this.refueled) {
                this.state = PlaneStates.CLEANINGANDRESTOCKING;
            }
        }
        
        //start Janitor thread for cleaning, Crew thread for restocking
        while (this.state == PlaneStates.CLEANINGANDRESTOCKING) {
            try {
                Janitor jan = new Janitor("Janitor", this);
                Crew crew = new Crew("Crew Member", this);
                jan.start();
                crew.start();
                
                jan.join();
                crew.join();
                this.state = PlaneStates.PASSENGERONBOARDING;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        //Plane is ready to allow onboarding of passengers
        while (this.state == PlaneStates.PASSENGERONBOARDING) {
            this.passengerCount.set(passengersToBoard);
            System.out.println("Passengers waiting to board Plane " + this.id + ": " + passengersToBoard);
            for (int i = 1; i <= passengersToBoard; i++) {
                Passenger passenger = new Passenger(i, this);
                passenger.start();
                try {
                    passenger.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (this.passengerCount.get() == 0) {
                System.out.println("All passengers have boarded Plane " + this.id + ".");
                stat.noPassengerBoarded += passengersToBoard;
                this.state = PlaneStates.LEAVEGATE;
            }
        }
        
        //Plane finish at-gate tasks, prepares to leave
        if (this.state == PlaneStates.LEAVEGATE){
            System.out.println("Plane " + this.id + " is ready to leave gate. Asking permisson for departure..." );
        }        
        while (this.state == PlaneStates.LEAVEGATE)
        {
            //request permission from ATC to acquire runway for departure
            atc.checkRunwayForDeparture(this);
        }
        
        //Permission granted, plane acquired runway and is performing departure processes
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
                    //sleep to simulate time taken to perform departure tasks
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            atc.gate.gateSem.release();
            this.state = PlaneStates.DEPARTED;
        }
        
        //Plane leaves airport
        if (this.state == PlaneStates.DEPARTED)
        {
            System.out.println("Plane " + this.id + " has left the airport." );
            stat.planeCount += 1;
            atc.runway.runwaySem.release();
            airport.airportCapacity.decrementAndGet();
            atc.landPlaneOnRunway();
        }
        
        //display stats 
        try {
            stat.statSem.acquire();
            if (stat.statSem.availablePermits() == 0)
            {
                stat.displayStats(atc.gate);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
