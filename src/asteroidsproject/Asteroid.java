/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroidsproject;

import processing.core.*;
import java.awt.Color;
import java.util.ArrayList;

/**
Name: Hunter Glass
Class: CSc 2310: Introduction to programming
Filename: Asteroid.java
Project: Asteroids
Date written: April 20, 2010

Description:
This class represents the Asteroids that are in the game.
Velocity, colors and positions are randomly(NOT UNIQUELY) set for the asteroids.
Bounds checking also occurs hear for the purpose of finite screen area.
Asteroid splittings/shrinking is also handled her in the event that a projectile
strikes an asteroid 
*/

public class Asteroid {

    private PApplet parent;
    //random numbers are pre casted for convenience
    float randomNumOne = (float)Math.random();
    float randomNumTwo = (float)Math.random();
    float randomNumThree = (float)Math.random();
    //Creates random colors
    Color randomColor = new Color(randomNumOne, randomNumTwo, 
             randomNumThree);
    public float locationX, locationY;
    public Velocity velocity;
    public int size;

    public Asteroid(PApplet pApp, int size) {
        this.parent = pApp;
        
        //Set up a random location for the asteroids
        locationX = (float) (Math.random() * parent.width);
        locationY = (float) (Math.random() * parent.height);

        //Set the asteroid to have a constant velocity and a random direction
        velocity = new Velocity(50, Math.random() * 360);
        
        this.size = size;
    }
    
    public void update(long elapsedTimeMillis) {
        double elapsedTimeSeconds = elapsedTimeMillis / 1000.0;

        //Add the velocity vector components to the X and Y positions
        locationX += velocity.xSpeed() * elapsedTimeSeconds;
        locationY += velocity.ySpeed() * elapsedTimeSeconds;


        /* Checks if the asteroid is within bounds.
         * Subtracting the dimension instead of setting it to 0 so that smooth
         * motion is maintained.  Will need to make sure the speed is an
         * acceptable value
         */
        if (locationX > parent.width) {
            locationX = locationX - parent.width;
        }

        if (locationY > parent.height) {
            locationY = locationY - parent.height;
        }

        if (locationX < 0) {
            locationX = locationX + parent.width;
        }

        if (locationY < 0) {
            locationY = locationY + parent.height;
        }

    }

    public void draw() {
        //Color is filled
        parent.fill(randomColor.getGreen(), randomColor.getBlue(),
                randomColor.getRed());
        //X pos, Ypos, Length, Heighth
        parent.ellipse(locationX, locationY, 20 * size, 20 * size);
    }
    
    public ArrayList<Asteroid> splitAsteroid(){
    	ArrayList<Asteroid> newAsteroids = new ArrayList<Asteroid>();
    	//Splits asteroids apart and shrinks them
    	for(int i = 0; i < 2; i++){
    		Asteroid splitResult = new Asteroid(parent, size - 1);
    		splitResult.locationX = locationX;
    		splitResult.locationY = locationY;
    		newAsteroids.add(splitResult);
    	}
    	
    	return newAsteroids;
    }
}
