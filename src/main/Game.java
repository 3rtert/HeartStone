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

        //player[0].createRandomDeck();
        player[0].createTestDeck();
        player[0].getCardFromDeck();
        player[0].getCardFromDeck();
        player[0].getCardFromDeck();

        player[1].createTestDeck();

        //player[1].createRandomDeck();
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
            possibleMoves();
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
    private void possibleMoves() {
        TreeOfGame tree = new TreeOfGame(this);
        ArrayList<ArrayList<Move>> moves = tree.getAllMoves();
        System.out.println("Number of possible moves: " + moves.size());
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
        player[currentPlayer].clearBoard();
        player[enemyPlayer].clearBoard();
        endTour();
    }

    public boolean performMoves(ArrayList<Move> moves) {
        for(Move move: moves) {
            if(!move.perform(player[currentPlayer], player[enemyPlayer])) {
                return false;
            }
        }
        return true;
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

    public Player getCurrentPlayer() {
        return player[currentPlayer];
    }

    public Player getEnemyPlayer() {
        return player[enemyPlayer];
    }
}
