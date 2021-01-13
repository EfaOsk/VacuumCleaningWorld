import java.util.Collection;

public class MyAgent implements Agent
{
	boolean isTurnedOn = false, isFinished= false, isStarted = false;
	int posY = 0, posX = 0, ori = 0;
	int minY = 0;
    public String nextAction(Collection<String> percepts) {
		// System.out.print("perceiving:");
		// for(String percept:percepts) {
		// 	System.out.print("'" + percept + "', ");
		// }
		// System.out.println("");
		// String[] actions = { "TURN_ON", "TURN_OFF", "TURN_RIGHT", "TURN_LEFT", "GO", "SUCK" };
		System.out.print(String.format("X: %d Y: %d (ori: %d)\n", posX, posY, ori));
		if (!isFinished){
			if (!isTurnedOn) {
				isTurnedOn = true;
				return "TURN_ON";
			} else if (!isStarted) {
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
			} else if (percepts.contains("DIRT")){
				return "SUCK";
			} else if (percepts.contains("BUMP")) {
				goBack(); 
				if (ori == 0){ return turnRight(); }
				else if (ori == 1){ 
					isFinished = true;
					return goHome(); } // GO home (But not nesseraly)
				else if (ori == 2){ return turnLeft(); }
				else { 	
					isFinished = true;
					return goHome(); } // GO home
			} else if (ori == 1){
				if (posY == minY){	
					if (posX%2 == 0){ return turnLeft();
					} else { return go(); }
				}
				if (posX%2 == 0){ return go(); } 
				else {return turnRight(); }
			} else {
				return go();
			}
		} else {
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
}
