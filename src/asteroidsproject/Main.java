/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroidsproject;

import processing.core.*;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
Name: Hunter Glass
Class: CSc 2310: Introduction to programming
Filename: Main.java
Project: Asteroids
Date written: April 20, 2010

Description:
This class is the controller of the entire project, drawing her is done
by the PApplet of processing, parameters are passed to functions set in
other classes, and ArrayLists are created for use of Projectiles, Asteroids, 
and messages. A few warnings had to be supressed, I assume this is an issue with
Eclipse. This class handles gameovers, score display, asteroid, ship life, ship health,
and laser management. Intro page, and start page are also handled here.
Bounds checking occurs her both for asteroids and lasers. If there is a 
collision between a projectile(laser) and an asteroid, a split occurs. Level progression
is handled here, where if all asteroids are destroyed, a new more difficult, level
is created. Floating messages are finally displayed here. Ability to pause and resume game
are established. Keystrokes are read using KeyEvents. All args are sent to the
proessing PApplet.

With help and ideas from http://processing.org/
AND
http://www.blindfish.co.uk/code/processing/asteroids02/

*/

@SuppressWarnings("serial")
public class Main extends PApplet {
	boolean displaySplashScreen;
	boolean displayGameOverScreen;
	boolean gamePaused;
	long playerScore;
	int level;
	long invulnerabilityRemaining = 0;

	long lastUpdateTime;
	// Generic Integer Array list created to help with what keys are being held,
	// They are awesome!

	private ArrayList<Integer> keysBeingHeld = new ArrayList<Integer>();

	// Everything flying around the screen
	private Spaceship mySpaceShip;
	private ArrayList<Asteroid> asteroids;
	private ArrayList<Projectile> lasers;
	private ArrayList<FloatingMessage> messages;

	// Eclipse was complaining, suppressed
	@SuppressWarnings("deprecation")
	@Override
	public void setup() {
		// Screen size is scaled depending on user's screen size
		size(screen.width, screen.height);
		smooth();
		playerScore = 0;
		// OBJ, X POS, YPOS
		// new SpaceShip model is instantiated and given params
		mySpaceShip = new Spaceship(this, 2, 20);
		// Asteroids,lasers, and Messages are created
		asteroids = new ArrayList<Asteroid>();
		lasers = new ArrayList<Projectile>();
		messages = new ArrayList<FloatingMessage>();
		displaySplashScreen = true;
		
		startLevel(1);
		pause();
	}
	
	public void startLevel(int level){
		this.level = level;
		//Clear all objects
		asteroids.clear();
		lasers.clear();
		
		int asteroidsToSpawn = 2 + (level * 2);
		
		for (int i = 0; i < asteroidsToSpawn; i++) {
			asteroids.add(new Asteroid(this, 3));
		}
		
		mySpaceShip.setInvulnerable(2000);
		
		lastUpdateTime = System.currentTimeMillis();
	}
	
	public void update(){
		long timeSinceLastUpdate = System.currentTimeMillis() - lastUpdateTime;
		processMovementKeys(timeSinceLastUpdate);
		
		mySpaceShip.update(timeSinceLastUpdate);
		
		for (Asteroid curAsteroid : asteroids) {
			curAsteroid.update(timeSinceLastUpdate);
		}
		for (Projectile laser : lasers) {
			laser.update(timeSinceLastUpdate);
		}
		
		ArrayList<FloatingMessage> destroyMessages = 
								new ArrayList<FloatingMessage>();
		for (FloatingMessage message : messages) {
			message.update(timeSinceLastUpdate);
			if(message.destroyTime < System.currentTimeMillis())
				destroyMessages.add(message);
		}
		messages.removeAll(destroyMessages);

		boundsCheck();
		
		if(isLevelClear()){

			startLevel(level+1);
			FloatingMessage levelMessage = new FloatingMessage(this, 
					"LEVEL UP!", Color.YELLOW, 30, 3000, mySpaceShip.position);
				levelMessage.setMomentum(new PVector(0,0.2f));
				messages.add(levelMessage);
		}
		
		if(mySpaceShip.shipLives < 1){
			pause();
			displayGameOverScreen = true;
		}
	}
	
	@Override
	public void draw() {
		//Call update procedure to prepare new positions on everything
		if(!gamePaused){
			update();
		}

		background(128);
		
		//Draw all objects
		mySpaceShip.draw();
		for (Asteroid curAsteroid : asteroids) {
			curAsteroid.draw();
		}

		for (Projectile laser : lasers) {
			laser.draw();
		}
		
		for (FloatingMessage message : messages) {
			message.draw();
		}

		drawUserInterface();
		
		// Finished drawing, store this time as the last draw
		lastUpdateTime = System.currentTimeMillis();
		
	}
	
	public void drawUserInterface(){
		
		// displays player score, hp, and lives on screen
		fill(0,0,0);
		textSize(30);
		textAlign(RIGHT);
		text(String.format("Score: %d  Level:  %d", playerScore, level),
				screenWidth, screenHeight - 30);
		
		textAlign(LEFT);
		text(String.format("Health: %d  Lives: %d", 
				mySpaceShip.shipHealth, mySpaceShip.shipLives),
				0, screenHeight - 30);
		textSize(15);
		
		if(gamePaused){
			int midX = width / 2;
			int midY = height / 2;
			fill(0, 0, 0, 128);
			rect(0, 0, width, height);
			fill(255, 255, 255, 255);
			textSize(50);
			textAlign(CENTER);
			text("PAUSED", midX, midY - 30);
			textSize(30);
			text("Press P to Un-pause", midX, midY + 30);
			textAlign(LEFT);
			textSize(15);
		}
		
		if(displaySplashScreen) {
			int midX = width / 2;
			int midY = height / 2;
			fill(0, 0, 0, 220);
			rect(0, 0, width, height);
			fill(255, 255, 255, 255);
			textSize(48);
			textAlign(CENTER);
			text("A S T E R O I D S !", midX, midY - 120);
			textSize(20);
			text("Key Bindings:", midX, midY - 80);
			text("Space - Fire", midX, midY - 60);
			text("Up - Accelerate", midX, midY - 40);
			text("Left/Right - Rotate", midX, midY - 20);
			text("P - Pause / Unpause", midX, midY);
			text("ESC - Exit game", midX, midY + 20);
			text("Ship turns blue when invulnerable", midX, midY + 60);
			textSize(30);
			text("Press any key to begin!", midX, midY + 100);
			textAlign(LEFT);
			textSize(15);
		}
		
		if(displayGameOverScreen) {
			int midX = width / 2;
			int midY = height / 2;
			fill(0, 0, 0, 220);
			rect(0, 0, width, height);
			fill(255, 255, 255, 255);
			textSize(48);
			textAlign(CENTER);
			text("G A M E   O V E R", midX, midY - 120);
			textSize(30);
			text("Score: " + playerScore, midX, midY - 80);
			text("Level: " + level, midX, midY - 50);
			text("Press ESC to exit.", midX, midY + 100);
			text("Press any other key to restart.", midX, midY + 130);
		}
	}

	private void processMovementKeys(long elapsedTimeMillis) {
		float elapsedTimeSeconds = elapsedTimeMillis / 1000.0f;
		
		//No movement will take place when game is paused
		if(gamePaused)
			return;

		//Ship rotation
		if (isKeyHeldDown(KeyEvent.VK_LEFT)) {
			mySpaceShip.angle -= (elapsedTimeSeconds * 360.0f);
		}
		if (isKeyHeldDown(KeyEvent.VK_RIGHT)) {
			mySpaceShip.angle += (elapsedTimeSeconds * 360.0f);
		}
		
		//Ship acceleration
		if (isKeyHeldDown(KeyEvent.VK_UP)) {
			mySpaceShip.thrust = 10;
		}else{
			mySpaceShip.thrust = 0;
		}

	}

	private void boundsCheck() {
		// Array lists are created for the destruction of lasers and asteroids
		// And for the recreation of destroyed asteroids
		ArrayList<Projectile> lasersToKill = new ArrayList<Projectile>();
		ArrayList<Asteroid> asteroidsToKill = new ArrayList<Asteroid>();
		ArrayList<Asteroid> asteroidsToCreate = new ArrayList<Asteroid>();
		// For each laser that is a lasers
		for (Projectile laser : lasers) {
			// Checks the distance of the lasers,
			// remove if out of screen bounds > 2000
			if (laser.distanceCovered > 2000) {
				lasersToKill.add(laser);
			} else {
				for (Asteroid asteroid : asteroids) {

					if (Math.abs(asteroid.locationX - laser.locationX) < 25
							&& Math.abs(asteroid.locationY - laser.locationY) < 25) {
						// Have to store these to kill later since I can't
						// modify the array list while looping through it
						lasersToKill.add(laser);
						// Splits asteroids into smaller asteroids upon laser
						// hit
						if (asteroid.size > 1)
							asteroidsToCreate.addAll(asteroid.splitAsteroid());

						// Destroys asteroid, gain points.
						PVector messageLoc = new PVector();
						messageLoc.x = asteroid.locationX;
						messageLoc.y = asteroid.locationY;
						FloatingMessage newMessage = new FloatingMessage(this, 
								"+25", Color.GREEN, 16, 1500, messageLoc);
						newMessage.setMomentum(new PVector(0,-0.2f));
						messages.add(newMessage);
						
						playerScore += 25;
						//bonus life at 2500 score points, message displays
						if((playerScore) == 2500){
							mySpaceShip.shipLives +=1;
							FloatingMessage newMessageTwo = new FloatingMessage
							(this,"+1 Life", Color.GREEN, 16, 1500, messageLoc);
						}
						//At 250 score points, get 25 bonus health, 
						//message displays
						if((playerScore)== 250){
							mySpaceShip.shipHealth += 25;
							FloatingMessage newMessageThree = new FloatingMessage
							(this,"+25 HP", Color.GREEN, 16, 1500, messageLoc);
						}
						asteroidsToKill.add(asteroid);
						break;
					}
				}
			}
		}

		int shipBounds = 15;
		int asteroidBounds;
		for (Asteroid asteroid : asteroids) {
			asteroidBounds = asteroid.size * 20 / 2;
			if (Math.abs(asteroid.locationX - mySpaceShip.position.x) < 
					(shipBounds + asteroidBounds)&& Math.abs(asteroid.locationY 
							- mySpaceShip.position.y) < (shipBounds + asteroidBounds)
					&& !mySpaceShip.isInvulnerable()) {
				// The ship collided with an asteroid, lose health, lives, etc
				// and lives as needed, also invulnerability time is set
				mySpaceShip.setInvulnerable(3000);
				mySpaceShip.shipHealth -= 50;
				
				if (mySpaceShip.shipHealth <= 0) {
					mySpaceShip.shipHealth = 100;
					mySpaceShip.shipLives -= 1;
					FloatingMessage lifeMessage = new FloatingMessage(this, 
						"-1 LIFE", Color.RED, 30, 1500, mySpaceShip.position);
					lifeMessage.setMomentum(new PVector(0,0.2f));
					messages.add(lifeMessage);
				}else{
					FloatingMessage hpMessage = new FloatingMessage(this, 
						"-50 HP", Color.RED, 20, 1500, mySpaceShip.position);
					hpMessage.setMomentum(new PVector(0,0.2f));
					messages.add(hpMessage);
				}
			}

		}

		asteroids.removeAll(asteroidsToKill);
		asteroids.addAll(asteroidsToCreate);
		lasers.removeAll(lasersToKill);
	}
	
	public boolean isLevelClear(){
		if(asteroids.size() <= 0){
			//+300 points on level completeion
			playerScore += 300;
			return true;
		}
		return false;
	}
	//Will pause game
	public void pause() {
		gamePaused = true;
		invulnerabilityRemaining = mySpaceShip.getInvulnerableTimeRemaining();
	}
	//Will resume game
	public void unpause() {
		mySpaceShip.setInvulnerable(invulnerabilityRemaining);
		lastUpdateTime = System.currentTimeMillis() - 1;
		gamePaused = false;
	}

	/**
	 * Key press detection Stores an arraylist of keycodes that are being held
	 * down check if the code exists in the array to know if it is held
	 */
	@Override
	public void keyPressed() {
		// Only grabs keys that we want
		int keyCode = keyEvent.getKeyCode();
		if (!keysBeingHeld.contains(keyCode)) {
			keysBeingHeld.add(keyCode);
		}
		
		//Any key pressed while splash screen is active will start game
		if(displayGameOverScreen) {
			displayGameOverScreen = false;
			setup();
			return;
		}
		
		if(displaySplashScreen) {
			unpause();
			displaySplashScreen = false;
		}
		
		if(gamePaused){
			if (keyCode == KeyEvent.VK_P){
				unpause();
			}
		}else{
			if (keyCode == KeyEvent.VK_P){
				pause();
			}
			if (keyCode == KeyEvent.VK_SPACE) {
				// Fire the laser, only 3 on screen at one time
				if (lasers.size() < 3) {
					lasers.add(mySpaceShip.fireLaser());
				}
			}
		}
		
	}

	@Override
	public void keyReleased() {
		// If key is let go, it is removed from the Arraylist
		int keyCode = keyEvent.getKeyCode();
		if (keysBeingHeld.contains(keyCode)) {
			keysBeingHeld.remove(keysBeingHeld.indexOf((keyCode)));
		}
	}

	// Function searches for if a keycode is contained
	public boolean isKeyHeldDown(int keyCode) {
		return keysBeingHeld.contains(keyCode);
	}

	public static void main(String[] args) {

		PApplet.main(new String[] { "--present", "asteroidsproject.Main" });

	}

}
