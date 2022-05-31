/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.concurrent.Semaphore;


/**
 *
 * @author Ryan Ng
 */

//Gate as a semaphore of size 2
public class Gate {
    Semaphore gateSem = new Semaphore(2);
}
