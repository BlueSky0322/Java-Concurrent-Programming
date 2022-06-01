/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

/**
 *
 * @author Ng Lum Thyn TP061914
 */

public class Main { 
    public static void main(String[] args) {
        FuelTruck fuelTruck = new FuelTruck();
        Runway runway = new Runway();
        Gate gate = new Gate();
        Airport airport = new Airport();
        Statistics stat = new Statistics();
        
        //start ATC thread
        ATC atc = new ATC(fuelTruck, runway, gate, airport, stat);
        atc.run();
        
        //generating 6 plane threads using for loop
        for (int i = 1; i <= 6; i++) {
            try {
                Plane plane = new Plane(i, airport, atc, stat);
                plane.start();
                //sleep to simulate plans coming in 1,2 or 3 second intervals.
                Plane.sleep((int) (Math.random() * 2001) + 1000);
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
