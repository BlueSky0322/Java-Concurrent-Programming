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

//enum class to hold constant variables of MAX and MIN passengers.
public enum PassengerRange {
    MIN(10),
    MAX(15);
    
    private final int noOfPassengers;

    private PassengerRange(int val) {
        noOfPassengers = val;
    }
    
    public int getValue() { return noOfPassengers; }
}
