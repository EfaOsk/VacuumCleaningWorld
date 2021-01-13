import java.util.Collection;

public class MyAgent implements Agent
{
	boolean isTurnedOn = false;
	int posY = 0, posX = 0, ori = 0;
    public String nextAction(Collection<String> percepts) {
		System.out.print("perceiving:");
		for(String percept:percepts) {
			System.out.print("'" + percept + "', ");
		}
		System.out.println("");
		String[] actions = { "TURN_ON", "TURN_OFF", "TURN_RIGHT", "TURN_LEFT", "GO", "SUCK" };
		
		if (!isTurnedOn) {
			isTurnedOn = true;
			return "TURN_ON";
		} else if (percepts.contains("DIRT")){
			return "SUCK";
		} else if (percepts.contains("BUMP")) {
			if (ori == 0){ 	return "TURN_RIGHT"; }
			else if (ori == 1){ 	return "TURN_OFF"; } // GO home (But not nesseraly)
			else if (ori == 2){ 	return "TURN_LEFT"; }
			else { 	return "TURN_OFF"; } // GO home
		} else if (ori == 1){
			return "TURN_RIGHT";
		} else if (ori == 3){
			return "TURN_LEFT";
		} else {
			return go();
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
}
