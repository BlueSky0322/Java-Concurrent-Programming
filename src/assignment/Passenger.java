/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Ryan Ng
 */
public class Passenger extends Thread {

    int passengersToBoard;
    Plane plane;
    PlaneStates state;
    PassengerRange pr;
    AtomicInteger passengerCount = new AtomicInteger();

    Passenger(Plane plane) {
        this.passengersToBoard = (int) Math.floor(Math.random() * (PassengerRange.MAX.getValue() - PassengerRange.MIN.getValue() + 1) + PassengerRange.MIN.getValue());
        this.plane = plane;
    }

    @Override
    public void run() {
        while (plane.state == PlaneStates.PASSENGERDISEMBARKING) {
            System.out.println("Passengers waiting to disembark Plane " + plane.id + ": " + plane.noOfPassenger);
            this.passengerCount.set(plane.noOfPassenger);
            disembark(plane);
            if (this.passengerCount.get() == 0)
            {
                System.out.println("All passengers have disembarked Plane " + plane.id + ".");
                plane.state = PlaneStates.CLEANINGANDRESTOCKING;
            }
        }
        
        while (plane.state == PlaneStates.PASSENGERONBOARDING) {
            System.out.println("Passengers waiting to board Plane " + plane.id + ": " + this.passengersToBoard);
            this.passengerCount.set(this.passengersToBoard);
            board(plane);
            if (this.passengerCount.get() == 0)
            {
                System.out.println("All passengers have boarded Plane " + plane.id + ".");
                plane.state = PlaneStates.LEAVEGATE;
            }
        }
    }

    synchronized void disembark(Plane plane) {
        for (int i = 1; i <= plane.noOfPassenger; i++) {
            System.out.println("Thread - Passenger " + i + ": I am disembarking from Plane " + plane.id + ".");
            this.passengerCount.decrementAndGet();
        }
    }

    synchronized void board(Plane plane) {
        for (int i = 1; i <= this.passengersToBoard; i++) {
            System.out.println("Thread - Passenger " + i + ": I am boarding Plane " + plane.id + ".");
            this.passengerCount.decrementAndGet();
        }
    }

}
