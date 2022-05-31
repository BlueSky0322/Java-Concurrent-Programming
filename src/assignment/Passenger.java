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
    
    Plane plane;
    PlaneStates state;
    PassengerRange pr;
    

    Passenger(int passengerNo, Plane plane) {
        this.threadCount = passengerNo;
        this.plane = plane;
    }

    public void run() {
        while (this.plane.state == PlaneStates.PASSENGERDISEMBARKING && movementState) 
        {
            disembark(this.plane);
        }
        while (plane.state == PlaneStates.PASSENGERONBOARDING && movementState) {
            board(this.plane);
        }
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

    void board(Plane plane) {
        try {
            System.out.println("Passenger " + threadCount + ": I am boarding Plane " + plane.id + ".");
            Thread.sleep((long) (Math.random() * 1000));
            plane.passengerCount.decrementAndGet();
            this.movementState = false;
        } catch (InterruptedException ex) {
            Logger.getLogger(Passenger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
