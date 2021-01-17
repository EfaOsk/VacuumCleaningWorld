import java.util.Collection;


public class OptAgent {
    boolean isTurnedOn = false, isFinished= false, isStarted = false;
	int posY = 0, posX = 0, ori = 0;

	int X1, Y1, X2, Y2 = 0;
    public String nextAction(Collection<String> percepts) {
		
		System.out.print("posX: " + posX + " posY: " + posY + " X1: " + X1 + " Y1: " + Y1 + " X2: " + X2 + " Y2: " + Y2 + " ori: " + ori);
		System.out.println("");
		if (!isFinished){
			if (!isTurnedOn) {
				isTurnedOn = true;
				return "TURN_ON";
			} else if (percepts.contains("DIRT")){		//  If you see durt, fkn pick it up!
				return "SUCK";
			} else if (!isStarted) {					// Find the lower left corner
				return start(percepts);
			}  else {									// If nothing is stoping you, just take a step forward
				return goOn();
			}
		} else {										// We finnished cleaning, we are going home
			return goHome();
		}
	}
	/**
	 * Updates the position of the vacuum and returns the command to go forward
	 * 
	 * @return String
	 */
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
	/**
	 * Reverts the position of the vacuum incase of a BUMP is hit.
	 */
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
	/**
	 * Updates the orientation and returns the command to go Right
	 * 
	 * @return String
	 */
	public String turnRight() {
		ori = (ori+1)%4;
		return "TURN_RIGHT";
	}
	/**
	 * Updates the orientation and returns the command to go Left
	 * 
	 * @return String
	 */
	public String turnLeft() {
		ori = (ori-1+4)%4;
		return "TURN_LEFT";
	}
	/**
	 * Returns the vacuum home and resets all initial values.
	 * 
	 * @return String
	 */
	public String goHome() {
		if ((posX == 0) && (posY == 0)){
			resetValues();
			return "TURN_OFF";
		}
		switch (ori) {
			case 0:
				if (posY < 0) { return go(); }
				else if (posX < 0) { return turnRight(); }
				else{ return turnLeft(); } 
			case 1:
				if (posX < 0 ) {return go();}
				else if (posY > 0 ){ return turnRight(); }
				else { return turnLeft(); }
			case 2:
				if(posY > 0) { return go(); }
				else if (posX > 0) { return turnRight(); }
				else { return turnLeft(); }
			case 3:
				if (posX > 0) { return go(); }
				else if (posY < 0 ) { return turnRight(); }
				else { return turnLeft(); }
		}
		return go(); // function needs to return string...
	}

	/**
	 * Finds the next corner and finds the wall edges.
	 * 
	 * @param percepts
	 * @return
	 */
	public String start(Collection<String> percepts) {
		if (percepts.contains("BUMP")) {
			goBack();
			switch (ori) {
				case 1:
					X2 = posX;
					Y2 = posY+1;
					isStarted = true;
					break;
				case 3:
					X1 = posX+1;
					Y1 = posY;
					break;
			}
			return turnLeft();
		}
		return go();

	}
	/**
	 * Cleans in circle until the X's and Y's positions are the same 
	 * then ends the cleaning cicle and starts going home.
	 * 
	 * @return String
	 */
	public String goOn() {
		if (posX == X2 && posX == X1 && posY == Y2 && posY== Y1) {
			isFinished = true;
			return goHome();
		}
		if(ori == 0 && posY == Y1) {
			X2--;
			return turnLeft();
		} else if (ori == 3 && posX == X1) {
			Y1--;
			return turnLeft();
		} else if (ori == 2 && posY == Y2) {
			X1++;
			return turnLeft();
		} else if (ori == 1 && posX == X2) {
			Y2++;
			return turnLeft();
		}
		return go();
	}

	/**
	 * For reseting the initial values for new game.
	 */
	public void resetValues() {
		isStarted = false; 
		isTurnedOn = false;
		isFinished = false;
		X1 = 0;
		X2 = 0;
		Y1 = 0;
		Y2 = 0;
		ori = 0;
	}
}
