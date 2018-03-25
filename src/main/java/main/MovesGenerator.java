package main;

import moves.AttackCardMove;
import moves.CardOnTableMove;
import moves.Move;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class MovesGenerator {

    public static ArrayList<ArrayList<Move>> getAllMoves(Game game) {

        ArrayList<ArrayList<Move>> output = new ArrayList<>();

        ArrayList<ArrayList<Move>> possibleMovesCardOnTable = getAllPossibleCardOnTableMoves(game);
        ArrayList<ArrayList<Move>> possibleMovesCardAttacks = getAllPossibleCardAttackMoves(game);
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
        return output;
    }

    public static ArrayList<ArrayList<Move>> getAllPossibleCardOnTableMoves(Game game) {

        ArrayList<Move> moves = getAllPossibleSingleCardOnTableMoves(game);
        ArrayList<ArrayList<Move>> combinationList = new ArrayList<>();

        for (long i = 1; i < Math.pow(2, moves.size()); i++) {
            ArrayList<Move> movesList = new ArrayList<>();
            int mana = game.getCurrentPlayer().getMana();
            int freeSpots = Constants.NUMBER_OF_CARDS_ON_TABLE - game.getCurrentPlayer().getNumberOfCardsOnTable();
            boolean enoughMana = true;
            for (int j = 0; j < moves.size(); j++) {
                if ((i & (long) Math.pow(2, j)) > 0) {
                    Move move = moves.get(j);
                    mana -= move.getMoveCost(game.getCurrentPlayer());
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

    public static ArrayList<Move> getAllPossibleSingleCardOnTableMoves(Game game) {
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

    public static ArrayList<Move> getAllPossibleSingleCardAttackMoves(Game game) {
        ArrayList<Move> moves = new ArrayList<>();
        Card[] cardsOnTable = game.getCurrentPlayer().getCardsOnTable();
        Card[] enemyCardsOnTable = game.getEnemyPlayer().getCardsOnTable();
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

    public static ArrayList<ArrayList<Move>> getAllPossibleCardAttackMoves(Game game) {
        ArrayList<Move> moves = getAllPossibleSingleCardAttackMoves(game);

        ArrayList<ArrayList<Move>> combinationList = new ArrayList<>();

        for (long i = 1; i < Math.pow(2, moves.size()); i++) {
            ArrayList<Move> movesList = new ArrayList<>();
            for (int j = 0; j < moves.size(); j++) {
                if ((i & (long) Math.pow(2, j)) > 0) {
                    Move move = moves.get(j);
                    movesList.add(move);
                }
            }
            if(game.getCurrentPlayer().getNumberOfCardsOnTable() < movesList.size()) {
                continue;
            }
            Game gameClone = game.clone();
            if (gameClone.performMoves(movesList)) {
                Card[] cardsOnTable = gameClone.getCurrentPlayer().getCardsOnTable();
                for (int ind = 0; ind < cardsOnTable.length; ind++) {
                    if(cardsOnTable[ind] != null && cardsOnTable[ind].canCardAttack()) {
                        movesList.add(new AttackCardMove(ind, -1)); // add attack on hero
                    }
                }

                combinationList.add(movesList);
            }
        }
        if(game.getCurrentPlayer().getNumberOfCardsOnTable() > 0) {
            Card[] cardsOnTable = game.getCurrentPlayer().getCardsOnTable();
            ArrayList<Move> onlyHeroAttackMoves = new ArrayList<>();
            for (int ind = 0; ind < cardsOnTable.length; ind++) {
                if(cardsOnTable[ind] != null) {
                    onlyHeroAttackMoves.add(new AttackCardMove(ind, -1)); // add attack on hero
                }
            }

            combinationList.add(onlyHeroAttackMoves);
        }

        return combinationList;

    }

    public static ArrayList<Move> getRandomMove(Game game) {
        ArrayList<Move> toPerformMoves = new ArrayList<>();

        ArrayList<Move> moves = getAllPossibleSingleCardOnTableMoves(game);
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

        Game tempGame = game.clone();
        Card[] myCards = tempGame.getCurrentPlayer().getCardsOnTable();

        for(int i = 0; i<myCards.length; i++) {
            int typeOfAttack = random.nextInt(2);
            if (typeOfAttack == 0) {
                toPerformMoves.add(new AttackCardMove(i, -1));
            } else {
                int enemyCardIndex = random.nextInt(tempGame.getEnemyPlayer().getNumberOfCardsOnTable());
                Move move = new AttackCardMove(i, tempGame.getEnemyPlayer().findIndexOfCard(enemyCardIndex));
                toPerformMoves.add(move);
                move.perform(tempGame.getCurrentPlayer(), tempGame.getEnemyPlayer());
            }
        }
        return toPerformMoves;
    }

    public static void printArrayOfCombinationMoves(ArrayList<ArrayList<Move>> allMoves) {
        for(ArrayList<Move> moves: allMoves){
            System.out.print("Move: ");
            for(Move move: moves) {
                System.out.print(move + ",");
            }
            System.out.println("-----");
        }
    }

    public static void printArrayOfMoves(ArrayList<Move> moves) {
        System.out.print("Print single move: ");
        for(Move move: moves) {
            System.out.print(move + ",");
        }
        System.out.println("-----");
    }
}
