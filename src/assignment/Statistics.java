/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Ng Lum Thyn TP061914
 */

public class Statistics {

    private long startTime;
    private long endTime;
    
    int planeCount = 0;
    int noPassengerBoarded = 0;
    long total = 0;
    long avg;

    ArrayList<Long> duration;
    Semaphore statSem = new Semaphore(6);

    public Statistics() {
        this.duration = new ArrayList<Long>();
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    void calculateDuration() {
        this.duration.add(endTime - startTime);
    }

    void displayStats(Gate gate) {
        System.out.println("\nFinal check on Gates...");
        if (gate.gateSem.availablePermits() == 2) {
            System.out.println("Available Gates: " + gate.gateSem.availablePermits());
            System.out.println("Gates are indeed empty.");
        } else {
            System.out.println("Simulation failed.");
        }

        System.out.println("\n----============== STATISTICS OF SIMULATION ==============----");

        System.out.println("Maximum waiting time of planes: " + (Collections.max(duration) / 1000000000) + " (seconds)");
        System.out.println("Minimum waiting time of planes: " + (Collections.min(duration) / 1000000000) + " (seconds)");

        duration.forEach((i) -> {
            total += i;
        });
        avg = total / (duration.size() + 1);
        System.out.println("Average waiting time of planes: " + (avg / 1000000000) + " (seconds)");

        System.out.println("\nNumber of planes served: " + planeCount);
        System.out.println("Number of passengers boarded: " + noPassengerBoarded);
    }
}
