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
public class Main {

    public static void main(String[] args) {
        Runway runway = new Runway();
        Gate gate = new Gate();
        Airport airport = new Airport();
        ATC atc = new ATC(runway, gate, airport);
        
        for (int i = 1; i <= 6; i++) {
            try {
                Plane plane = new Plane(i, airport, atc);
                plane.start();
                Plane.sleep((long) (Math.random() * 30));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Planes in queue now: " + airport.listOfPlanes.size());
//        ATC atc = new ATC(runway, gate, airport);
//        Thread atcThread = new Thread(atc);
//        atcThread.start();

    }
}
