package players;

import java.util.ArrayList;

import main.Game;
import main.TreeOfGame;
import moves.Move;

public class MCTSPlayer implements PlayerSIInterface
{
	TreeOfGame treeOfGame;
	ArrayList<Move> nextMove;
	Game game;
	int numberOfIntelligence=1;
	@Override
	public void init(Game currentGame, int numberOfIntelligence) {
		treeOfGame=new TreeOfGame(currentGame);
		game=currentGame.clone();
		this.numberOfIntelligence=numberOfIntelligence;
	}

	@Override
	public String getNextMove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Move> calculateNextMove(long maxTime) 
	{
		return treeOfGame.calculateBestMove(maxTime, game.getCurrentPLayerId());
	}

}
