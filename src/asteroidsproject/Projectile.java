/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroidsproject;

import processing.core.*;

/**
Name: Hunter Glass
Class: CSc 2310: Introduction to programming
Filename: Projectile.java
Project: Asteroids
Date written: April 20, 2010

Description:
This class represents the projectile velocity that the player will shoot in the game.
Vector math is done to get correct values for X and Y location, then an ellipse laser
is drawn on screen using those locations
*/

public class Projectile {

    private PApplet parent;
    public float locationX, locationY;
    public Velocity velocity;
    public float distanceCovered;

    public Projectile(PApplet parent, float startX, float startY,
            double speed, double angle) {
        this.parent = parent;
        locationX = startX;
        locationY = startY;
        velocity = new Velocity(speed, angle);
    }

    public void update(long elapsedTimeMillis) {
        double elapsedTimeSeconds = elapsedTimeMillis / 1000.0;

        //Add the velocity vector components to the X and Y positions
        locationX += velocity.xSpeed() * elapsedTimeSeconds;
        locationY += velocity.ySpeed() * elapsedTimeSeconds;

        distanceCovered += velocity.speed * elapsedTimeSeconds;
    }

    public void draw() {
        parent.fill(0, 0, 255);
        parent.ellipse(locationX, locationY, 10, 10);
    }
}
