/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ryan Ng
 */
public class Passenger implements Runnable {

    //true indicates disembarking; false indicates boarding
    boolean movementState;

    private final int minPassengers = 10;
    private final int maxPassengers = 50;

    Plane plane;
    PlaneStates state;

    Passenger(Plane plane) {
        this.plane = plane;
    }

    public void run() {
        //randomly generate number of passengers
        int noOfPassenger = (int) Math.floor(Math.random() * (maxPassengers - minPassengers + 1) + minPassengers);
        //this.movementState = true;
        if (this.plane.state == PlaneStates.PASSENGERDISEMBARKING) {
            //while (this.movementState) {
                for (int i = 1; i <= noOfPassenger; i++) {
                    System.out.println("Thread - Passenger " + i + ": I am disembarking from Plane " + this.plane.id + ".");
                    try {
                        Thread.sleep((long) (Math.random() * 1000)); // simulate passengers getting off at different intervals                
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Passenger.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //this.movementState = false;
            }
        
        if (this.plane.state == PlaneStates.PASSENGERONBOARDING) {
            //while (!this.movementState) {
                for (int i = 1; i <= noOfPassenger; i++) {
                    System.out.println("Thread - Passenger " + i + ": I am boarding Plane " + this.plane.id + ".");
                    try {
                        Thread.sleep((long) (Math.random() * 1000)); // simulate passengers getting off at different intervals                
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Passenger.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //this.movementState = false;
            }
    }

}
