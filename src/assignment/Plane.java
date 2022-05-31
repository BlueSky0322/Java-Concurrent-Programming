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
class Plane extends Thread {

    int id;
    PlaneStates state;
    Airport airport;
    ATC atc;
    
    Plane(int id, Airport airport, ATC atc) {
        this.id = id;
        this.airport = airport;
        this.atc = atc;
        this.state = PlaneStates.WANTTOLAND;
        System.out.println("Plane " + id + " has been generated.");
    }

    public void run() 
    { 
        while (this.state == PlaneStates.WANTTOLAND) {
            System.out.println("Plane " + this.id + " wants to land! Requesting permission from ATC...");
            atc.checkRunway(this);
            
    }
        while (this.state == PlaneStates.ONRUNWAYARRIVAL) {
            atc.checkGate(this);
        }
        while (this.state == PlaneStates.ATGATE) {
            System.out.println("Plane " + this.id + " getting ready to disembark its passengers...");
            System.out.println("Gate ternminals are opening...");
            this.state = PlaneStates.PASSENGERDISEMBARKING;
        }
        while (this.state == PlaneStates.PASSENGERDISEMBARKING){
        }
        while (this.state == PlaneStates.PASSENGERONBOARDING) {}
    }
    
    
}
