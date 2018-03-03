package players;

import main.Game;
import moves.Move;
import java.util.ArrayList;

public interface PlayerSIInterface
{
	void init(Game currentGame);
	String getNextMove(); // return for example: a1/2
	ArrayList<Move> calculateNextMove(int maxTime); // calculate no longer than maxTime (milliseconds)
}
