package main;

import moves.*;
import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreeOfGame {

    private List trees = new ArrayList<TreeOfGame>();
    private ArrayList<Move> previousMove;
    private ArrayList<ArrayList<Move>> moves;
    private Game currentGame;
    private int wins = 0;
    private int loses = 0;
    private int simulations = 0;

    public ArrayList<Move> calculateBestMove(long maxTime, int player) {
        long endTime = System.currentTimeMillis() + maxTime;

        while (endTime > System.currentTimeMillis()) {
            mcts(player);
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

    private int mcts(int player) {
        TreeOfGame currentTree;
        if ((currentTree = selection()) == this) {
            int winner = simulate(expansion().currentGame.clone());
            int result = (winner == player) ? 1 : 0;
            wins += result;
            simulations++;
            return result;
        } else {
            int result = currentTree.mcts(player);
            wins += result;
            simulations++;
            return result;
        }
    }

    int simulate(Game tempGame) // return number of player who won
    {
        tempGame.initializeMove(false);
        ArrayList<Move> moves = MovesGenerator.getRandomMove(tempGame);
        tempGame.performMoves(moves);
        tempGame.nextRound();
        tempGame.endTour();
        return tempGame.getPlayerWin() == -1 ? simulate(tempGame) : tempGame.getPlayerWin();
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

    // we wzorze nie powinno być odwrotnie simulations???
    //TODO parametr c
    private TreeOfGame selection() {
        if (moves == null || moves.isEmpty() || trees.isEmpty()) {
            return this;
        } else {
            TreeOfGame current = null;
            double rate = 0;
            for (int i = 0; i < trees.size(); i++) {
                double tempRate = wins / simulations + Math.sqrt(2 * Math.log1p(((TreeOfGame) (trees.get(i))).simulations) / simulations);
                if (tempRate > rate) {
                    rate = tempRate;
                    current = (TreeOfGame) trees.get(i);
                }
            }
            return current;
        }
    }


    public TreeOfGame(Game game) {
        currentGame = game.clone();
    }

}
