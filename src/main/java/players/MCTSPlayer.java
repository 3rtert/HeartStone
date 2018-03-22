package players;

import java.util.ArrayList;

import main.Game;
import main.TreeOfGame;
import moves.Move;

public class MCTSPlayer implements PlayerSIInterface {
	@Override
	public ArrayList<Move> calculateNextMove(Game game, long maxTime)
	{
		TreeOfGame treeOfGame=new TreeOfGame(game);
		return treeOfGame.calculateBestMove(maxTime, game.getCurrentPLayerId());
	}

}
