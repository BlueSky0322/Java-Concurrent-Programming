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
    
    Plane(int id, Airport airport) {
        this.id = id;
        this.airport = airport;
        this.state = PlaneStates.WANTTOLAND;
        System.out.println("Plane " + id + " has been generated.");
    }

    public void run() 
    { 
        reachAirport();
        while (this.state == PlaneStates.WANTTOLAND) {
            System.out.println("Plane " + this.id + " wants to land! Requesting permission from ATC...");
    }
    }
    
    
    private synchronized void reachAirport()
    {
        airport.addPlaneToQueue(this); 
    }
}
