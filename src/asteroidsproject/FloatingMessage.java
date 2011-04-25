package asteroidsproject;
import processing.core.*;
import java.awt.Color;
/**
Name: Hunter Glass
Class: CSc 2310: Introduction to programming
Filename: FloatingMessage.java
Project: Asteroids
Date written: April 20, 2010

Description:
This class represents the game's popup system of messages that show:
when points are scored, life is lost/gained, and when hp is lost/gained
A bit of vector math is used, similarly to the method used in the other
clases, a time period for how long a message will be displayed is also set.
*/


public class FloatingMessage {
	private PApplet parent;
	
	private String messageText;
	private Color messageColor;
	private int messageSize;
	
	private PVector position;
	private PVector momentum;
	public long destroyTime;
	
	public FloatingMessage(PApplet pApp, String message, Color color, int size, 
			long timeToLive, PVector startPosition)
	{
		parent = pApp;
		
		messageText = message;
		messageColor = color;
		messageSize = size;
		//positions saved as vectors for easy adding/removal
		//Also for easy vector math
		position = new PVector();
		//Vector is essentially cloned, could not get another ByRef to work
		position.set(startPosition.array());
		
		momentum = new PVector(0,0);
		//Handles how lons message is shown
		destroyTime = System.currentTimeMillis() + timeToLive;
	}
	
	public void setMomentum(PVector momentum)
	{
		this.momentum = momentum;
	}
	//momentum added to the vector position
	public void update(long timeSinceLastUpdate) {
		position.add(momentum);
	}
	
	public void draw() {
		//Aesthetics of the message are handled
		parent.textSize(messageSize);
		parent.fill(messageColor.getRed(), messageColor.getGreen(), 
				messageColor.getBlue(), messageColor.getAlpha());
		parent.text(messageText,position.x, position.y);
	}
	
}
