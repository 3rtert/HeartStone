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
    private boolean withGui;
    private GUIInterface gui;

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
        this.withGui = withGui;
        if (this.withGui) {
            gui = new GUI();
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
            initializeMove();
            if (this.withGui) {
                refreshGui();
            }
            move();
        }
        System.out.println("Wygral gracz numer: " + playerWin);
    }
    private void initializeMove() {
        if (currentPlayer == 0) {
            nextRound();
        }
        player[currentPlayer].updateMana(round);
        player[currentPlayer].getCard();
        player[currentPlayer].updateCardsAttack();
    }

    void move() {
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

    void nextRound() {
        round += 1;
    }

    void endTour() {
        currentPlayer = currentPlayer == 0 ? 1 : 0;
        enemyPlayer = enemyPlayer == 0 ? 1 : 0;
    }

    private void refreshGui() {
        refreshGuiForPlayer(PlayerNumber.ONE, 0);
        refreshGuiForPlayer(PlayerNumber.TWO, 1);
    }

    private void refreshGuiForPlayer(PlayerNumber pl, int index) {
        gui.setNumberOfCardsInDeck(pl, player[index].getNumberOfCardsInStack());
        gui.setAmountOfMana(pl, player[index].getMana());
        gui.setNumberOfLifePoints(pl, player[index].getHealth());

        gui.clearCards(pl);
        ArrayList<Card> cards = player[index].getCardsInHand();
        for(int i = 0; i< cards.size(); i++) {
            Card card = cards.get(i);
            gui.addCardToPlayersHand(pl, i, card);
        }

        cards = player[index].getCardOnTable();
        for(int i = 0; i< cards.size(); i++) {
            Card card = cards.get(i);
            gui.addCardToBattleField(pl, i, card);
        }
        gui.moveNotification(pl, index == currentPlayer);
    }
}
