package main;

import moves.*;
import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreeOfGame {

    private List<TreeOfGame> trees = new ArrayList<TreeOfGame>();
    private ArrayList<Move> previousMove;
    private ArrayList<ArrayList<Move>> moves;
    private Game currentGame;
    private int wins = 0;
    private int simulations = 0;

    public ArrayList<Move> calculateBestMove(long maxTime, int player, float c_param, int simulateBestOf_param) {
        long endTime = System.currentTimeMillis() + maxTime;

        while (endTime > System.currentTimeMillis()) {
            mcts(player, c_param, simulateBestOf_param);
        }

        return getBestMove();
    }

    private ArrayList<Move> getBestMove() {
        ArrayList<Move> bestMove = null;
        double rate = 0;
        for (int i = 0; i < trees.size(); i++) {
            double tempRate = wins / simulations;
            if (tempRate > rate) {
                rate = tempRate;
                bestMove = ((TreeOfGame) trees.get(i)).previousMove;
            }
        }
        return bestMove;
    }

    private int mcts(int player, float c_param, int simulateBestOf) {
        TreeOfGame currentTree;
        if ((currentTree = selection(c_param)) == this) {
            int winner = simulate(expansion().currentGame.clone(), simulateBestOf);
            int result = (winner == player) ? 1 : 0;
            wins += result;
            simulations++;
            return result;
        } else {
            int result = currentTree.mcts(player, c_param, simulateBestOf);
            wins += result;
            simulations++;
            return result;
        }
    }

    int simulate(Game tempGame)
    {
    	return simulate(tempGame,1);
    }
    
    int simulate(Game tempGame, int bestOf) // return number of player who won
    {
        tempGame.initializeMove(false);
        ArrayList<Move> moves = MovesGenerator.getRandomMove(tempGame);
        for(int i = 1; i<bestOf; i++)
        {
        	ArrayList<Move> tempMoves = MovesGenerator.getRandomMove(tempGame);
        	if(tempMoves.size()>moves.size())
        	{
        		moves=tempMoves;
        	}
        	
        }
        tempGame.performMoves(moves);
        tempGame.nextRound();
        tempGame.endTour();
        return tempGame.getPlayerWin() == -1 ? simulate(tempGame,bestOf) : tempGame.getPlayerWin();
    }

    private TreeOfGame expansion() {
        currentGame.initializeMove(true);
        if (moves == null) {
            MovesGenerator.getAllMoves(currentGame);
        }
        ArrayList<Move> currentMove = (ArrayList<Move>) moves.remove(0);

        TreeOfGame newLeaf = new TreeOfGame(currentGame);
        newLeaf.previousMove = currentMove;
        newLeaf.currentGame.performMoves(currentMove);

        trees.add(newLeaf);

        return newLeaf;
    }

    private TreeOfGame selection()
    {
    	return selection(2);
    }
    private TreeOfGame selection(float c) {
        if (moves == null ||  trees.isEmpty()) {
            return this;
        } else {
            TreeOfGame current = null;
            double rate = 0;
            for (int i = 0; i < trees.size(); i++) {
            	double ri = trees.get(i).wins / trees.get(i).simulations;
                double tempRate = ri + c * Math.sqrt(Math.log1p(simulations) / trees.get(i).simulations);
                if (tempRate > rate) {
                    rate = tempRate;
                    current = trees.get(i);
                }
            }
            return current;
        }
    }


    public TreeOfGame(Game game) {
        currentGame = game.clone();
    }

}
