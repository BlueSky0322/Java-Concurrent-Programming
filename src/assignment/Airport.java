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

    //LinkedList<Plane> listOfPlanes;

    public Airport() {
        
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
