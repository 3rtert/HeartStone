package players;

import main.Card;
import main.Game;
import main.Player;
import main.TreeOfGame;

import java.util.ArrayList;
import moves.Move;

public class SpoonPlayer implements PlayerSIInterface
{
    //numberOfIntelligence:
    //0 - consolePLayer (no inteligance)
    //1 - intelligence player (WIP)
    //2 - attack player
    //3 - deff player
    //4 - ...
	private int numberOfIntelligence;

    public SpoonPlayer(int numberOfIntelligence) {
        this.numberOfIntelligence = numberOfIntelligence;
    }

	@Override
	public ArrayList<Move> calculateNextMove(Game currentGame, long maxTime)
	{
		TreeOfGame treeOfGame=new TreeOfGame(currentGame);
		ArrayList<ArrayList<Move>> allMoves = treeOfGame.getAllMoves();

		int bestMoveScore=0;
		ArrayList<Move> bestMove=new ArrayList<Move>();
		for(ArrayList<Move> move : allMoves)
		{
		    System.out.print("Move:");
		    for(Move singleMove : move) {
                System.out.print(singleMove);
            }
            System.out.println("");
			int currentMoveScore = evaluate(currentGame, move);
			if(currentMoveScore>bestMoveScore)
			{
				bestMoveScore=currentMoveScore;
				bestMove=move;
			}
		}
		return bestMove;
	}
	
	private int evaluate(Game game, ArrayList<Move> fullMove)
	{
		Game tempGame=game.clone();
		tempGame.initializeMove(false);
		tempGame.performMoves(fullMove);

		int rate=0;
		if(numberOfIntelligence==2)
			rate=100*evaluateAggressive(tempGame)+evaluateDefensive(tempGame);
		else if(numberOfIntelligence==3)
			rate=evaluateAggressive(tempGame)+evaluateDefensive(tempGame)*100;
		else if(numberOfIntelligence==4)
			rate=evaluateAggressive(tempGame)+evaluateDefensive(tempGame);
		return rate;
	}
	
	private int evaluateAggressive(Game game)
	{
		int rate=0;
		Player enemy = game.getEnemyPlayer();
		rate-=3*enemy.getHealth();
		for(Card card : enemy.getCardsOnTable())
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
		for(Card card : currentPlayer.getCardsOnTable())
		{
			rate+=card.getLife()+card.getAttack();
		}
		return rate;
	}
}
