package players;

import main.Game;
import moves.Move;
import java.util.ArrayList;

public interface PlayerSIInterface
{
	void init(Game currentGame, int numberOfIntelligence); 
	//numberOfIntelligence:
	//0 - consolePLayer (no inteligance)
	//1 - intelligence player (WIP)
	//2 - attack player
	//3 - deff player
	//4 - ...
	
	String getNextMove(); // return for example: a1/2
	ArrayList<Move> calculateNextMove(int maxTime); // calculate no longer than maxTime (milliseconds)
}
