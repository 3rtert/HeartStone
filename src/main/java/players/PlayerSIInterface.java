package players;

import main.Game;
import moves.Move;
import java.util.ArrayList;

public interface PlayerSIInterface
{
	ArrayList<Move> calculateNextMove(Game currentGame, long maxTime); // calculate no longer than maxTime (milliseconds)
}
