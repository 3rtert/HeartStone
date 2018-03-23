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
        ArrayList<Move> moves = getRandomMove(tempGame);
        tempGame.performMoves(moves);
        tempGame.nextRound();
        tempGame.endTour();
        return tempGame.getPlayerWin() == -1 ? simulate(tempGame) : tempGame.getPlayerWin();
    }

    private TreeOfGame expansion() {
        currentGame.initializeMove(true);
        if (moves == null)
            getAllMoves();
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

    public ArrayList<ArrayList<Move>> getAllMoves() {

        ArrayList<ArrayList<Move>> output = new ArrayList<>();

        ArrayList<ArrayList<Move>> possibleMovesCardOnTable = combinationsCardOnTable();
        ArrayList<ArrayList<Move>> possibleMovesCardAttacks = getAllPossibleCardAttackMoves();
        //cross product
        for (ArrayList<Move> singleMove : possibleMovesCardOnTable) {
            for (ArrayList<Move> singleAttack : possibleMovesCardAttacks) {
                ArrayList<Move> combined = new ArrayList<>();
                combined.addAll(singleMove);
                combined.addAll(singleAttack);
                output.add(combined);
            }
        }
        output.addAll(possibleMovesCardAttacks);
        output.addAll(possibleMovesCardOnTable);
        moves = output;
        return output;
    }

    private ArrayList<ArrayList<Move>> combinationsCardOnTable() {

        ArrayList<Move> moves = getAllPossibleCardOnTableMoves(currentGame);
        ArrayList<ArrayList<Move>> combinationList = new ArrayList<>();

        for (long i = 1; i < Math.pow(2, moves.size()); i++) {
            ArrayList<Move> movesList = new ArrayList<>();
            int mana = currentGame.getCurrentPlayer().getMana();
            int freeSpots = Constants.NUMBER_OF_CARDS_ON_TABLE - currentGame.getCurrentPlayer().getNumberOfCardsOnTable();
            boolean enoughMana = true;
            for (int j = 0; j < moves.size(); j++) {
                if ((i & (long) Math.pow(2, j)) > 0) {
                    Move move = moves.get(j);
                    mana -= move.getMoveCost(currentGame.getCurrentPlayer());
                    freeSpots--;
                    if (mana < 0 || freeSpots < 0) {
                        enoughMana = false;
                        break;
                    }
                    movesList.add(move);
                }
            }
            if (enoughMana) {
                combinationList.add(movesList);
            }
        }
        return combinationList;
    }

    private ArrayList<Move> getAllPossibleCardOnTableMoves(Game game) {
        ArrayList<Move> moves = new ArrayList<>();
        int mana = game.getCurrentPlayer().getMana();

         Card[] cardsInHand = game.getCurrentPlayer().getCardsInHand();
          for (int i = 0; i < cardsInHand.length; i++) {
             if(cardsInHand[i] != null && cardsInHand[i].getManaCost() <= mana) {
                  moves.add(new CardOnTableMove(i));
             }
          }
        return moves;
    }

    private ArrayList<Move> getAllPossibleSingleCardAttackMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        Card[] cardsOnTable = currentGame.getCurrentPlayer().getCardsOnTable();
        Card[] enemyCardsOnTable = currentGame.getEnemyPlayer().getCardsOnTable();
        for (int i = 0; i < cardsOnTable.length; i++) {
            if(cardsOnTable[i] == null) {
                continue;
            }
            for (int j = 0; j < enemyCardsOnTable.length; j++) {
                if(enemyCardsOnTable[j] == null) {
                    continue;
                }
                moves.add(new AttackCardMove(i, j)); // add attack on enemy's card
            }
        }
        return moves;
    }

    private ArrayList<ArrayList<Move>> getAllPossibleCardAttackMoves() {
        ArrayList<Move> moves = getAllPossibleSingleCardAttackMoves();

        ArrayList<ArrayList<Move>> combinationList = new ArrayList<>();

        Card[] cardsOnTable = currentGame.getCurrentPlayer().getCardsOnTable();

        for (long i = 1; i < Math.pow(2, moves.size()); i++) {
            ArrayList<Move> movesList = new ArrayList<>();
            for (int j = 0; j < moves.size(); j++) {
                if ((i & (long) Math.pow(2, j)) > 0) {
                    Move move = moves.get(j);
                    movesList.add(move);
                }
            }

            Game gameClone = currentGame.clone();
            if (gameClone.performMoves(movesList)) {

                for (int ind = 0; ind < cardsOnTable.length; ind++) {
                    if(cardsOnTable[ind] != null && cardsOnTable[ind].canCardAttack()) {
                        movesList.add(new AttackCardMove(ind, -1)); // add attack on hero
                    }
                }

                combinationList.add(movesList);
            }
        }

        ArrayList<Move> onlyHeroAttackMoves = new ArrayList<>();
        for (int ind = 0; ind < cardsOnTable.length; ind++) {
            onlyHeroAttackMoves.add(new AttackCardMove(ind, -1)); // add attack on hero
        }
        combinationList.add(onlyHeroAttackMoves);

        return combinationList;

    }

    private ArrayList<Move> getRandomMove(Game game) {
        ArrayList<Move> toPerformMoves = new ArrayList<>();

        ArrayList<Move> moves = getAllPossibleCardOnTableMoves(game);
        Random random = new Random();
        int cardsInHand = game.getCurrentPlayer().getNumberOfCardsInHand();
        int numberOfMoves = random.nextInt(cardsInHand + 1);

        int mana = game.getCurrentPlayer().getMana();
        int usedMana = 0;

        for (int i = 0; i < numberOfMoves; i++) {
            int moveIndex = random.nextInt(moves.size());
            Move move = moves.get(moveIndex);

            usedMana += move.getMoveCost(game.getCurrentPlayer());
            if(usedMana <= mana) {
                toPerformMoves.add(move);
                moves.remove(move);
            }
        }

        /*int cardsOnTable = game.getCurrentPlayer().getNumberOfCardsOnTable();
        int numberOfAttack = random.nextInt(cardsOnTable + 1);


        ArrayList<Card> myCards = game.getCurrentPlayer().getCardsOnTableCopy();
        ArrayList<Card> enemyCards = game.getEnemyPlayer().getCardsOnTableCopy();

        int numberOfMyDestroyedCards = 0;
        int numberOfEnemyDestroyedCards = 0;

        for (int i = 0; i < numberOfAttack; i++) {
            int myCardIndex = random.nextInt(myCards.size());
            int typeOfAttack = random.nextInt(2);

            if (typeOfAttack == 0) {
                toPerformMoves.add(new AttackCardMove(myCardIndex + i, -1));
                myCards.remove(myCardIndex);
            } else {
                int enemyCardIndex = random.nextInt(enemyCards.size());
                Card attackedCard = enemyCards.get(enemyCardIndex);
                Card card = myCards.get(myCardIndex);
                attackedCard.dealDmg(card.getAttack());
                if (attackedCard.isCardDestroyed()) {
                    enemyCards.remove(attackedCard);
                    numberOfEnemyDestroyedCards++;
                } else {
                    card.dealDmg(attackedCard.getAttack());
                    if (card.isCardDestroyed()) {
                        myCards.remove(card);
                        numberOfMyDestroyedCards++;
                    }
                }
                toPerformMoves.add(new AttackCardMove(myCardIndex + numberOfMyDestroyedCards,
                        enemyCardIndex + numberOfEnemyDestroyedCards));
            }
        }*/

        return toPerformMoves;
    }
}
