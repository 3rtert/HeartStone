package players;

import main.Card;
import main.Game;
import main.Player;
import main.TreeOfGame;

import java.util.ArrayList;
import moves.Move;

public class SpoonPlayer implements PlayerSIInterface
{
	TreeOfGame treeOfGame;
	ArrayList<Move> nextMove;
	Game game;
	int numberOfIntelligence=0;
	@Override
	public void init(Game currentGame, int numberOfIntelligence) 
	{
		treeOfGame=new TreeOfGame(currentGame);
		game=currentGame.clone();
		this.numberOfIntelligence=numberOfIntelligence;
	}

	@Override
	public String getNextMove() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Move> calculateNextMove(int maxTime)
	{
		ArrayList<ArrayList<Move>> allMoves = treeOfGame.getAllMoves();
		int bestMoveScore=0;
		ArrayList<Move> bestMove=new ArrayList<Move>();
		for(ArrayList<Move> move : allMoves)
		{
			int currentMoveScore = evaluate(move);
			if(currentMoveScore>bestMoveScore)
			{
				bestMoveScore=currentMoveScore;
				bestMove=move;
			}
		}
		return bestMove;
	}
	
	private int evaluate(ArrayList<Move> fullMove)
	{
		Game tempGame=game.clone();
		tempGame.performMoves(fullMove);
		int rate=0;
		if(numberOfIntelligence==2)
			rate=100*evaluateAgressive(tempGame)+evaluateAgressive(tempGame);
		else if(numberOfIntelligence==3)
			rate=evaluateAgressive(tempGame)+evaluateAgressive(tempGame)*100;
		else if(numberOfIntelligence==4)
			rate=evaluateAgressive(tempGame)+evaluateAgressive(tempGame);
		return rate;
	}
	
	private int evaluateAgressive(Game game)
	{
		int rate=0;
		Player enemy = game.getEnemyPlayer();
		rate-=3*enemy.getHealth();
		for(Card card : enemy.getCardOnTable())
		{
			rate-=card.getLife()+card.getAttack();
		}
		return rate;
	}
	
	private int evaluateDefensive(Game game)
	{
		int rate=0;
		Player currentPlayer = game.getCurrentPlayer();
		rate+=3*currentPlayer.getHealth();
		for(Card card : currentPlayer.getCardOnTable())
		{
			rate+=card.getLife()+card.getAttack();
		}
		return rate;
	}
}
