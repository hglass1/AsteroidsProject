package asteroidsproject;


import processing.core.*;

/**
Name: Hunter Glass
Class: CSc 2310: Introduction to programming
Filename: Spaceship.java
Project: Asteroids
Date written: April 20, 2010

Description:
This class represents the ship that the player will control in the game.
The Spaceship's health and lifes are saved her for purposes of gameover 
and gaining extra lives. Vector math is also handled her in order to control 
the ship's velocities and movements. A debug console that adds flavor displaying
various aspect of the ship and its movement is executed here. Invulnerability for 
aspects relating to scoring and ship life is also managed.
*/

public class Spaceship {

	private PApplet parent;
	
	public int shipLives = 3;
	public int shipHealth = 100;
	public long invulnerabilityTime = 0;
	
	private PVector momentum;
	public PVector position;
	public float angle = 0.0f;
	public float thrust = 0.0f;
	

	public Spaceship(PApplet pApp, int x, int y) {
		this.parent = pApp;
		
		position = new PVector(x,y);
		momentum = new PVector(0,0);
	}
	//This controls laser firing 
	public Projectile fireLaser() {
		return new Projectile(parent, position.x, position.y, 1000,	angle);
	}
	//This is for getting movement the way we would like it,
	//smooth and spacelike 
	public void update(long elapsedTimeMillis) {
		float elapsedTimeSeconds = elapsedTimeMillis / 1000.0f;
		PVector thrustVector = new PVector(thrust * 
				(float)Math.cos(Math.toRadians(angle)),
				                           thrust * (float)Math.sin(Math.toRadians(angle)));
		
		thrustVector.mult(elapsedTimeSeconds);
		momentum.add(thrustVector);
		
		// Set maximum speed
		if (momentum.mag() > 10) {
			momentum.div( momentum.mag() / 10 );
		}
		
		position.add(momentum);

		//Keep the ship on screen
		if (position.x > parent.width)
			position.x -= parent.width;
		
		if (position.y > parent.height)
			position.y -= parent.height;
		
		if (position.x < 0)
			position.x += parent.width;

		if (position.y < 0)
			position.y += parent.height;
		
		//Set Max angle
		if (angle > 360)
			angle -= 360;
		
		if (angle < 0)
			angle += 360;

	}

	public void draw() {
		//This is mainly for debugging, but also add flavor to the program
		parent.color(0,0,0);
		parent.text(String.format("POSITION: X: %10.4f Y: %10.4f", 
				position.x, position.y), 0, 20);
		parent.text(String.format("THRUST:   T: %10.1f A: %10.1f", thrust,
				angle), 0, 40);
		parent.text(String.format("MOMENTUM: X: %10.4f Y: %10.4f M: %10.4f",
				momentum.x, momentum.y, momentum.mag()), 0 , 60);
		//Sets invulnerability color
		if(isInvulnerable()){
			parent.fill(0, 0, 255);
		}else{
			parent.fill(255, 0, 0);
		}

		// Rotate works around the origin, so we have to draw our ship at 0,0
		// to get the rotation, then translate it to where it needs to be
		parent.translate(position.x, position.y);
		parent.rotate((float) Math.toRadians(angle - 90));

		parent.triangle(-15, -15, 0, 15, 15, -15);

		// Reset all transformations
		parent.resetMatrix();
	}
	
    //Following function and method are for health invulnerability 
	//from drastically decreasing as frames are redrawn
    public void setInvulnerable(long milliseconds){
    	long futureTimestamp;
    	futureTimestamp = System.currentTimeMillis() + milliseconds;
    	
    	if(futureTimestamp > this.invulnerabilityTime)
    		this.invulnerabilityTime = futureTimestamp;
    
    }
    //For Getting Time left for invulnerability 
    public long getInvulnerableTimeRemaining() {
    	if(isInvulnerable())
    		return invulnerabilityTime - System.currentTimeMillis();
    	return 0;
    }
    //Checks if invulnerable
    public boolean isInvulnerable(){
    	if(System.currentTimeMillis() < this.invulnerabilityTime)
    		return true;
    	return false;
    }
		
}
