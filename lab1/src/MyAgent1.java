import java.util.Collection;


public class MyAgent1 implements Agent
{
    int forward, right = 0; // position relative to starting position
    int orientation, lastOrientation = 0;
    boolean isTurnedOn, isCleaning  = false;
    int isChanging = 0; // 0 for not changing, 1 for first turn , 2 for go ,0 again after next turn then go home. 

    public String nextAction(Collection<String> percepts) {
		System.out.print("perceiving:");
		for(String percept:percepts) {
			System.out.print("'" + percept + "', ");
		}
        System.out.println("");
        //String[] actions = { "TURN_ON", "TURN_OFF", "TURN_RIGHT", "TURN_LEFT", "GO", "SUCK" };
        if(!isTurnedOn) {
            isTurnedOn = true;
            return "TURN_ON"; 
        } else if (!isCleaning) {
            return findCorner(percepts);
        } else if (isChanging == 1 ) {
            isChanging = 2;
            return goForward();
        } else if (isChanging == 2) {
            isChanging = 0;
            return turn();
        }
        else if (percepts.contains("DIRT")) {
            return "SUCK";
        } else if (percepts.contains("BUMP")) {
            isChanging = 1;
            undoForward();
            return turn();
        } else {
            return goForward();
        }
    }

    public String findCorner(Collection<String> percepts) {
        if (percepts.contains("BUMP") && orientation != lastOrientation) {
            isCleaning = !isCleaning;
            return turnLeft();
        } else if (percepts.contains("BUMP")) {
            return turnLeft();
        } else {
            return goForward();
        }
    }
    
    public String goForward() {
        switch (orientation) {
            case 0:
                forward += 1;
                break;
            case 1:
                right += 1;
                break;
            case 2:
                forward -= 1;
                break;
            case 3:
                right -= 1;
                break;
        }
        return "GO";
    }

    public void undoForward() {
        switch (orientation) {
            case 0:
                forward -= 1;
                break;
            case 1:
                right -= 1;
                break;
            case 2:
                forward += 1;
                break;
            case 3:
                right += 1;
                break;
        }
    }
    
    
    public String turnLeft() {
        lastOrientation = orientation;
        orientation = (orientation + 1) % 4;
        return "TURN_LEFT";
    }

    public String turnRight() {
        lastOrientation = orientation;
        orientation = (orientation - 1) % 4;
        return "TURN_RIGHT";
    }

    public String turn() {
        if (orientation == 0 ||  lastOrientation == 0 ) {
            return turnRight();
        }
        return turnLeft();
    }
}