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
    
    int threadCount;
    int passengersToBoard;
    Plane plane;
    PlaneStates state;
    PassengerRange pr;
    

    Passenger(int passengerNo, Plane plane) {
        this.threadCount = passengerNo;
        this.passengersToBoard = (int) Math.floor(Math.random() * (PassengerRange.MAX.getValue() - PassengerRange.MIN.getValue() + 1) + PassengerRange.MIN.getValue());
        this.plane = plane;
    }

    public void run() {
        while (this.plane.state == PlaneStates.PASSENGERDISEMBARKING && movementState) 
        {
            disembark(this.plane);
        }
//        while (plane.state == PlaneStates.PASSENGERONBOARDING) {
//            System.out.println("Passengers waiting to board Plane " + plane.id + ": " + this.passengersToBoard);
//            this.passengerCount.set(this.passengersToBoard);
//            board(plane);
//            if (this.passengerCount.get() == 0) {
//                System.out.println("All passengers have boarded Plane " + plane.id + ".");
//                plane.state = PlaneStates.LEAVEGATE;
//            }
//        }
    }

    void disembark(Plane plane) {
        try {
            System.out.println("Passenger " + threadCount + ": I am disembarking from Plane " + plane.id + ".");
            Thread.sleep((long) (Math.random() * 1000));
            plane.passengerCount.decrementAndGet();
            this.movementState = false;
        } catch (InterruptedException ex) {
            Logger.getLogger(Passenger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    void board(Plane plane) {
//        for (int i = 1; i <= this.passengersToBoard; i++) {
//            System.out.println("Thread - Passenger " + i + ": I am boarding Plane " + plane.id + ".");
//            this.passengerCount.decrementAndGet();
//        }
//    }

}
