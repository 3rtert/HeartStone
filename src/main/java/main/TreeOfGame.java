package main;

import moves.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreeOfGame {
    List trees = new ArrayList<TreeOfGame>();
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

    public ArrayList<Move> getRandomMove() {
        ArrayList<Move> toPerformMoves = new ArrayList<>();

        ArrayList<Move> moves = getAllPossibleCardOnTableMoves();
        Random random = new Random();
        int cardsInHand = currentGame.getCurrentPlayer().getNumberOfCardsInHand();
        int numberOfMoves = random.nextInt(cardsInHand + 1);

        for(int i = 0; i < numberOfMoves; i++) {
            int cardIndex = random.nextInt(moves.size());
            Move move = moves.get(cardIndex);
            toPerformMoves.add(move);
            moves.remove(move);
        }

        int cardsOnTable = currentGame.getCurrentPlayer().getNumberOfCardsOnTable();
        int numberOfAttack = random.nextInt(cardsOnTable + 1);

        ArrayList<Card> myCards = currentGame.getCurrentPlayer().getCardsOnTableCopy();
        ArrayList<Card> enemyCards = currentGame.getEnemyPlayer().getCardsOnTableCopy();

        int numberOfMyDestroyedCards = 0;
        int numberOfEnemyDestroyedCards = 0;

        for(int i = 0; i < numberOfAttack; i++) {
            int myCardIndex = random.nextInt(myCards.size());
            int typeOfAttack = random.nextInt(2);

            if(typeOfAttack == 0) {
                toPerformMoves.add(new AttackCardMove(myCardIndex + i, -1));
                myCards.remove(myCardIndex);
            } else {
                int enemyCardIndex = random.nextInt(enemyCards.size());
                Card attackedCard = enemyCards.get(enemyCardIndex);
                Card card = myCards.get(myCardIndex);
                attackedCard.dealDmg(card.getAttack());
                if(attackedCard.isCardDestroyed()) {
                    enemyCards.remove(attackedCard);
                    numberOfEnemyDestroyedCards++;
                } else {
                    card.dealDmg(attackedCard.getAttack());
                    if(card.isCardDestroyed()) {
                        myCards.remove(card);
                        numberOfMyDestroyedCards++;
                    }
                }
                toPerformMoves.add(new AttackCardMove(myCardIndex + numberOfMyDestroyedCards,
                        enemyCardIndex + numberOfEnemyDestroyedCards));
            }
        }

        return toPerformMoves;
    }
}
