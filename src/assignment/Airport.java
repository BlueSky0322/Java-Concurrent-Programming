/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.concurrent.atomic.AtomicInteger;


/**
 *
 * @author Ng Lum Thyn TP061914
 */

//Airport class to hold airport max capacity and AtomicInteger for counting capacity
public class Airport {
    final int MAX_CAPACITY = 2;
    //AtomicInteger to keep count of airport capacity
    AtomicInteger airportCapacity = new AtomicInteger(0);
}

