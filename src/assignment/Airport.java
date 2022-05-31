/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ryan Ng
 */
public class Airport {

    //Airport capacity
    final int MAX_CAPACITY = 2;
    //static int currentCapacity = 0;
    AtomicInteger airportCapacity = new AtomicInteger(0);

    LinkedList<Plane> listOfPlanes;

    public Airport() {
        listOfPlanes = new LinkedList<Plane>();
    }

    public void landPlaneOnRunway() {
        Plane plane;
        synchronized (listOfPlanes) {
            System.out.println("ATC found a plane in queue.");
            plane = (Plane) ((LinkedList<?>) listOfPlanes).pollFirst();
        }
    }

    public void addPlaneToQueue(Plane p) {
        System.out.println("Plane " + p.id + " is entering the airspace");
        if (airportCapacity.get() == MAX_CAPACITY) {
            System.out.println("Plane " + p.id + " is entering the airspace.");
            synchronized (listOfPlanes) {
//            if(listOfPlanes.size() == MAX_CAPACITY)
//            {
//            	System.out.println("No space available for Plane"+ p.id);
//                try {
//                    listOfPlanes.wait();
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(Airport.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                return ;
//            }

                ((LinkedList<Plane>) listOfPlanes).offer(p);
                System.out.println("Plane " + p.id + " has been entered into waiting queue.");

                if (listOfPlanes.size() == 1) {
                    listOfPlanes.notify();
                }
            }
        }
    }

//    public void addToAirportQueue(Plane p)
//    {
//        
//        if(airportCapacity.get() != MAX_CAPACITY){
//            System.out.println("Airport Capacity: " + airportCapacity.incrementAndGet());
//            System.out.println("Airport is not full. Awaiting landing requests."); 
//            listOfPlanes.add(p);
//        }
//        else{
//            System.out.println("Airport is full! Please wait in queue.");
//        }
//    }
}
