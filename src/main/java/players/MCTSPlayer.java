package players;

import java.util.ArrayList;

import main.Game;
import main.TreeOfGame;
import moves.Move;

public class MCTSPlayer implements PlayerSIInterface {

	float c_param;

	public MCTSPlayer(float c_param) {
		this.c_param = c_param;
	}

	@Override
	public ArrayList<Move> calculateNextMove(Game game, long maxTime)
	{
		TreeOfGame treeOfGame=new TreeOfGame(game);
		return treeOfGame.calculateBestMove(maxTime, game.getCurrentPLayerId());
	}

}
