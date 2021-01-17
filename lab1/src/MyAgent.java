/*
	T-622-ARTI Gervigreind
	Valcume Cleaning World
	Code for Agent.
	Authors: 
		Eva Ósk Gunnarsdóttir 				(evag18@ru.is)
		Hólmfríður Magnea Hákonardóttir 	(holmfridurh17@ru.is)
	Date: 1.17.2021
*/

import java.util.Collection;

public class MyAgent implements Agent
{
	boolean isTurnedOn = false, isFinished= false, isStarted = false;
	int posY = 0, posX = 0, ori = 0;
	int minY = 0;
    public String nextAction(Collection<String> percepts) {
		System.out.print("perceiving:");
		for(String percept:percepts) {
			System.out.print("'" + percept + "', ");
		}
		System.out.println("");
		// System.out.print(String.format("X: %d Y: %d (ori: %d)\n", posX, posY, ori));
		if (!isFinished){
			if (!isTurnedOn) {
				isTurnedOn = true;
				return "TURN_ON";
			} else if (!isStarted) {					// Find the lower left corner
				return goTOStartPos(percepts);
			} else if (percepts.contains("DIRT")){		//  If you see durt, clean it up!
				return "SUCK";
			} else if (percepts.contains("BUMP")) {  	// you hit a wall.
				goBack(); 								// Take a step back
				return continu();
			} else if (ori == 1){
				if (posY == minY){						// here we are saving a BUMB, why did I not do that on the way up?
					if (posX%2 == 0){ return turnLeft();
					} else { return go(); }
				}
				if (posX%2 == 0){ return go(); } 
				else {return turnRight(); }
			} else {									// If nothing is stoping you, just take a step forward
				return go();
			}
		} else {										// We finnished cleaning, we are going home
			return goHome();
		}
	}
	public String go() {
		switch(ori) {
			case 0:
				posY++;
				break;
			case 1:
				posX++;
				break;
			case 2:
				posY--;
				break;
			case 3:
				posX--;
				break;
		}
		return "GO";
	}
	public void goBack() {
        switch (ori) {
            case 0:
				posY--;
				break;
			case 1:
				posX--;
				break;
			case 2:
				posY++;
				break;
			case 3:
				posX++;
				break;
		}
    }
	public String turnRight() {
		ori = (ori+1)%4;
		return "TURN_RIGHT";
	}
	public String turnLeft() {
		ori = (ori-1+4)%4;
		return "TURN_LEFT";
	}
	public String goHome() {
		if ((posX == 0) && (posY == 0)){
			isStarted = false; // ready to start again
			ori = 0;
			return "TURN_OFF";
		} else {
			if (posY > 0) {
				if (ori == 0){ return turnLeft(); }
				else if (ori == 1){ return turnRight(); }
				else if (ori == 2){ return go(); }
				else {
					if (posX > 0) { return go(); }
					return turnLeft();
				}
			} else {
				if (ori == 0){ return go(); }
				else if (ori == 1){ return turnLeft(); }
				else if (ori == 2){
					return turnRight();
				} else {
					if (posX >0) { return go(); }
					return turnRight();
				}
			}
		}
	}
	public String goTOStartPos(Collection<String> percepts) {
		if (ori == 0) { return turnLeft(); }
		else if (ori == 3) {
			if (percepts.contains("BUMP")) { goBack(); return turnLeft(); }
			return go();
		} else if (ori == 2) {
			if (percepts.contains("BUMP")) { 
				goBack();
				minY = posY;
				return turnLeft(); }
			return go();
		} else { 
			isStarted = true;
			return turnLeft();
		}
	}
	public String continu() {
		// step forward and turn away from the wall, or quict
		if (ori == 0){ return turnRight(); }
		else if (ori == 1){ 
			isFinished = true;
			return goHome(); } // GO home
		else if (ori == 2){ return turnLeft(); }
		else { 	
			isFinished = true;
			return goHome(); } // GO home
	}
}
