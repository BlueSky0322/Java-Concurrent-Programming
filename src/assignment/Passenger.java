/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ryan Ng
 */
public class Passenger extends Thread {
    boolean movementState = true;
    
    int passengerNo;
    int passengersToBoard;
    Plane plane;
    PlaneStates state;
    PassengerRange pr;
    AtomicInteger passengerCount = new AtomicInteger();

    Passenger(int passengerNo, Plane plane) {
        this.passengerNo = passengerNo;
        this.passengersToBoard = (int) Math.floor(Math.random() * (PassengerRange.MAX.getValue() - PassengerRange.MIN.getValue() + 1) + PassengerRange.MIN.getValue());
        this.plane = plane;
    }

    public void run() {
        while (this.plane.state == PlaneStates.PASSENGERDISEMBARKING && movementState) {
            //System.out.println("Passengers waiting to disembark Plane " + plane.id + ": " + plane.noOfPassenger);
            disembark(this.passengerNo, this.plane);
            if (this.passengerCount.get() == 0)
            {
                System.out.println("All passengers have disembarked Plane " + plane.id + ".");
                plane.state = PlaneStates.PASSENGERONBOARDING;
                return;
            }
        }

        while (plane.state == PlaneStates.PASSENGERONBOARDING) {
            System.out.println("Passengers waiting to board Plane " + plane.id + ": " + this.passengersToBoard);
            this.passengerCount.set(this.passengersToBoard);
            board(plane);
            if (this.passengerCount.get() == 0) {
                System.out.println("All passengers have boarded Plane " + plane.id + ".");
                plane.state = PlaneStates.LEAVEGATE;
            }
        }
    }

    void disembark(int passengerNo, Plane plane) {
        try {
                Thread.sleep((long) (Math.random() * 1000));
                System.out.println("Passenger " + passengerNo + ": I am disembarking from Plane " + plane.id + ".");
                this.passengerCount.decrementAndGet();
                this.movementState = false;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    void board(Plane plane) {
        for (int i = 1; i <= this.passengersToBoard; i++) {
            System.out.println("Thread - Passenger " + i + ": I am boarding Plane " + plane.id + ".");
            this.passengerCount.decrementAndGet();
        }
    }

}
