package main;

import moves.AttackCardMove;
import moves.CardOnTableMove;
import moves.Move;

import java.util.ArrayList;
import java.util.List;

public class TreeOfGame {
    List trees = new ArrayList<TreeOfGame>();
    ArrayList<ArrayList<Move>> moves;
    private Game currentGame;
    int wins = 0;
    int loses = 0;

    public TreeOfGame(Game game) {
        currentGame = game.clone();
    }

    public ArrayList<ArrayList<Move>> getAllMoves() {

        ArrayList<ArrayList<Move>> output = new ArrayList<>();

        ArrayList<ArrayList<Move>> possibleMovesCardOnTable = combinationsCardOnTable();
        ArrayList<ArrayList<Move>> possibleMovesCardAttacks = getAllPossibleCardAttackMoves();
        //cross product
        for(ArrayList<Move> singleMove: possibleMovesCardOnTable) {
            for(ArrayList<Move> singleAttack: possibleMovesCardAttacks) {
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

    public ArrayList<ArrayList<Move>> combinationsCardOnTable() {

        ArrayList<Move> moves = getAllPossibleCardOnTableMoves();
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
                    if(mana < 0 || freeSpots < 0) {
                        enoughMana = false;
                        break;
                    }
                    movesList.add(move);
                }
            }
            if(enoughMana) {
                combinationList.add(movesList);
            }
        }
        return combinationList;
    }

    public ArrayList<Move> getAllPossibleCardOnTableMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        int mana = currentGame.getCurrentPlayer().getMana();

        ArrayList<Card> cardsInHand = currentGame.getCurrentPlayer().getCardsInHand();
        for (int i = 0; i < cardsInHand.size(); i++) {
            if(cardsInHand.get(i).getManaCost() <= mana) {
                moves.add(new CardOnTableMove(i));
            }
        }


        return moves;
    }

    public ArrayList<Move> getAllPossibleSingleCardAttackMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        ArrayList<Card> cardsOnTable = currentGame.getCurrentPlayer().getCardOnTable();
        ArrayList<Card> enemyCardsOnTable = currentGame.getEnemyPlayer().getCardOnTable();
        for (int i = 0; i < cardsOnTable.size(); i++) {
            moves.add(new AttackCardMove(i, -1)); // add attack on hero
            for(int j = 0; j < enemyCardsOnTable.size(); j++) {
                moves.add(new AttackCardMove(i, j)); // add attack on enemy's card
            }
        }
        return moves;
    }

    public ArrayList<ArrayList<Move>> getAllPossibleCardAttackMoves() {
        ArrayList<Move> moves = getAllPossibleSingleCardAttackMoves();

        ArrayList<ArrayList<Move>> combinationList = new ArrayList<>();

        for (long i = 1; i < Math.pow(2, moves.size()); i++) {
            ArrayList<Move> movesList = new ArrayList<>();
            for (int j = 0; j < moves.size(); j++) {
                if ((i & (long) Math.pow(2, j)) > 0) {
                    Move move = moves.get(j);
                    movesList.add(move);
                }
            }
            //TODO for each permutation
            Game gameClone = currentGame.clone();
            if(gameClone.performMoves(movesList)) {
                combinationList.add(movesList);
            }
        }
        return combinationList;

    }
}
