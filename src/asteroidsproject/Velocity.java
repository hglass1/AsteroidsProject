/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroidsproject;



/**
Name: Hunter Glass
Class: CSc 2310: Introduction to programming
Filename: Velocity.java
Project: Asteroids
Date written: April 20, 2010

Description:
This class represents the ship's velocity that the player will control in the game.
X and Y speed and angle is taken her so that the ships velocity has both a magnitude
and a direction
*/

public class Velocity {

    public double speed;
    public double angle;
    
    public Velocity(double speed, double angle) {
        this.speed = speed;
        this.angle = angle;
    }
    //calculates speed for the x portion
    public double xSpeed() {

        return speed * Math.cos(Math.toRadians(angle));
    }
    //calculates speed for the y portion
    public double ySpeed() {

        return speed * Math.sin(Math.toRadians(angle));
    }
}
