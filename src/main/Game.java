package main;

import GUI.*;

import moves.Move;
import players.ConsolePlayer;
import players.PlayerSIInterface;

import java.util.ArrayList;

public class Game {
    private Player[] player = new Player[2];

    private int currentPlayer; //0 - 1
    private int enemyPlayer; //1 - 0


    private int round;
    private int playerWin = -1;

    void init(boolean withGui) {
        player[0] = new Player();
        player[1] = new Player();

        player[0].createRandomDeck();
        player[0].getCardFromDeck();
        player[0].getCardFromDeck();
        player[0].getCardFromDeck();


        player[1].createRandomDeck();
        player[1].getCardFromDeck();
        player[1].getCardFromDeck();
        player[1].getCardFromDeck();
        player[1].getCardFromDeck();

        currentPlayer = 0;
        enemyPlayer = 1;
        round = 0;

        if (withGui) {
            GUIInterface gui = new GUI();
            gui.setNumberOfCardsInDeck(PlayerNumber.ONE, 18);
            gui.setNumberOfCardsInDeck(PlayerNumber.TWO, 17);
            gui.setAmountOfMana(PlayerNumber.ONE, 1);
            gui.setAmountOfMana(PlayerNumber.TWO, 1);
            gui.setNumberOfLifePoints(PlayerNumber.ONE, 20);
            gui.setNumberOfLifePoints(PlayerNumber.TWO, 20);

            gui.addCardToPlayersHand(PlayerNumber.ONE, 2, 1, 10, 10, 10, 1);
            gui.addCardToPlayersHand(PlayerNumber.TWO, 1, 2, 100, 100, 100, 100);
        }
    }

    public Game clone()
    {
        Game newGame=new Game();
        newGame.player[0]=player[0].clone();
        newGame.player[1]=player[1].clone();
        newGame.currentPlayer=currentPlayer;
        newGame.enemyPlayer=enemyPlayer;
        newGame.round=round;

        return newGame;
    }
    void start() {
        while (playerWin == -1) {
            move();
        }
        System.out.println("Wygral gracz numer: " + playerWin);
    }

    void move() {
        if (currentPlayer == 1) {
            nextRound();
        }
        updateMana();
        getCard();
        player[currentPlayer].updateCardsAttack();

        PlayerSIInterface playerSI = new ConsolePlayer();
        ArrayList<Move> moves = playerSI.calculateNextMove(100);
        for(Move move: moves) {
            move.perform(player[currentPlayer], player[enemyPlayer]);
            if (player[enemyPlayer].isChampDestroyed()) {
                playerWin = currentPlayer;
                break;
            }
        }

        endTour();
    }

    void getCard() {
        if (player[currentPlayer].getNumberOfCardsInStack() > 0)
            player[currentPlayer].getCardFromDeck();
        else
            player[currentPlayer].dealDmgToChamp(1);
    }

    public void updateMana() {
        player[currentPlayer].setMana(Math.min(round, 10));
    }

    void nextRound() {
        round += 1;
    }

    void endTour() {
        currentPlayer = currentPlayer == 0 ? 1 : 0;
        enemyPlayer = enemyPlayer == 0 ? 1 : 0;
    }

}
