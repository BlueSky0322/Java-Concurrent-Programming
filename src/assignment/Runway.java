/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import java.util.concurrent.Semaphore;

/**
 *
 * @author Ng Lum Thyn TP061914
 */

//Runway as a semaphore of size 1
public class Runway {
    Semaphore runwaySem = new Semaphore(1);
}
