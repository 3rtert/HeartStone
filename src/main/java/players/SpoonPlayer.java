package players;

import main.*;

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
		ArrayList<ArrayList<Move>> allMoves = MovesGenerator.getAllMoves(currentGame);
        System.out.println("Possible moves: " + allMoves.size());
		int bestMoveScore=0;
		ArrayList<Move> bestMove=new ArrayList<Move>();
		for(ArrayList<Move> move : allMoves)
		{
		    System.out.print("Move:");
		    for(Move singleMove : move) {
                System.out.print(singleMove);
            }

			int currentMoveScore = evaluate(currentGame, move);
		    System.out.print(" Score: " + currentMoveScore);
			System.out.println("");

			if(currentMoveScore>bestMoveScore || bestMoveScore == 0)
			{
				bestMoveScore=currentMoveScore;
				bestMove=move;
			}
		}
        System.out.print("Best move in function:");
        for(Move singleMove : bestMove) {
            System.out.print(singleMove);
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
		    if(card != null) {
                rate -= card.getLife() + card.getAttack();
            }
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
		    if(card != null) {
                rate += card.getLife() + card.getAttack();
            }
		}
		return rate;
	}
}
