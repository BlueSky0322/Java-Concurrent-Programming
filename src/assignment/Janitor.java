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

public class Janitor extends Thread {
    
    Plane plane;
    private String threadName;

    Janitor(String name, Plane plane) {
        this.plane = plane;
        threadName = name;
        System.out.println("Plane " + plane.id + ": " + threadName + " will perform cleaning duties.");
    }
    
    public void run() {
        try {
            //various tasks being performed
            for (int i = 1; i < 4; i++) {
                switch (i) {
                    case 1:
                        System.out.println("Plane " + plane.id + ": " + threadName + " is cleaning seats...");
                        break;
                    case 2:
                        System.out.println("Plane " + plane.id + ": " + threadName + " is sweeping floor...");
                        break;
                    case 3:
                        System.out.println("Plane " + plane.id + ": " + threadName + " empting trash...");
                        break;
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread " + threadName + "interrupted.");
        }
        System.out.println("Plane " + plane.id + ": " +threadName + " has finished cleaning.");
    }
}
