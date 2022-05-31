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
        FuelTruck fuelTruck = new FuelTruck();
        Runway runway = new Runway();
        Gate gate = new Gate();
        Airport airport = new Airport();
        ATC atc = new ATC(fuelTruck, runway, gate, airport);
        atc.run();
        
        for (int i = 1; i <= 6; i++) {
            try {
                Plane plane = new Plane(i, airport, atc);
                plane.start();
                Plane.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        
        try {
            Thread.sleep(60000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
