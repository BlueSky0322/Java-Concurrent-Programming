/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

/**
 *
 * @author Ryan Ng
 */

public class Passenger extends Thread {
    
    private boolean movementState = true;
    private int threadCount;
    
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
    
    //function to disembark passengers
    void disembark(Plane plane) {
        try {
            System.out.println("Passenger " + threadCount + ": I am disembarking from Plane " + plane.id + ".");
            //sleep to simulate passengers getting off the plane at different intervals
            Thread.sleep((int) (Math.random() * 1000));
            plane.passengerCount.decrementAndGet();
            this.movementState = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    //function to board passengers
    void board(Plane plane) {
        try {
            System.out.println("Passenger " + threadCount + ": I am boarding Plane " + plane.id + ".");
            //sleep to simulate passengers boarding the plane at different intervals
            Thread.sleep((int) (Math.random() * 1000));
            plane.passengerCount.decrementAndGet();
            this.movementState = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
