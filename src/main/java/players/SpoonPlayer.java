package players;

import main.*;

import java.util.ArrayList;

import moves.Move;

public class SpoonPlayer implements PlayerSIInterface {
    //0 - aggressive
    //1 - controlling
    private int numberOfIntelligence;

    public SpoonPlayer(int numberOfIntelligence) {
        this.numberOfIntelligence = numberOfIntelligence;
    }

    @Override
    public ArrayList<Move> calculateNextMove(Game currentGame, long maxTime) {
        ArrayList<ArrayList<Move>> allMoves = MovesGenerator.getAllMoves(currentGame);

        int bestMoveScore = 0;
        ArrayList<Move> bestMove = new ArrayList<>();
        for (ArrayList<Move> move : allMoves) {
            int currentMoveScore = evaluate(currentGame, move);

            if (currentMoveScore > bestMoveScore || bestMoveScore == 0) {
                bestMoveScore = currentMoveScore;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int evaluate(Game game, ArrayList<Move> fullMove) {
        Game tempGame = game.clone();
        tempGame.performMoves(fullMove);

        return numberOfIntelligence == 0 ? evaluateAggressive(tempGame) : evaluateControlling(tempGame);
    }

    private int evaluateAggressive(Game game) {
        int rate = 0;

        rate -= 1000 * game.getEnemyPlayer().getHealth(); //enemy life

        rate += 10 * game.getCurrentPlayer().getHealth(); //current life

        rate += evaluateStateOfBoard(game);

        return rate;
    }

    private int evaluateControlling(Game game) {
        int rate = 0;

        rate -= game.getEnemyPlayer().getHealth(); //enemy life

        rate += 10 * game.getCurrentPlayer().getHealth();

        rate += 1000 * evaluateStateOfBoard(game);

        return rate;
    }

    private int evaluateStateOfBoard(Game game) {
        int rate = 0;
        int emptyEnemy = 0;
        //minus for enemy cards on board
        for (Card card : game.getEnemyPlayer().getCardsOnTable()) {
            if (card != null) {
                rate -= (100 * card.getLife() + card.getAttack());
            } else {
                emptyEnemy++;
            }
        }
        //plus enemies killed
        rate += 100 * emptyEnemy;

        //plus new cards on board
        for (Card card : game.getCurrentPlayer().getCardsOnTable()) {
            if (card != null) {
                rate += (card.getLife() + card.getAttack());
            }
        }
        return rate;
    }
}
