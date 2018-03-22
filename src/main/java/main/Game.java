package main;

import GUI.*;

import moves.Move;
import players.ConsolePlayer;
import players.MCTSPlayer;
import players.PlayerSIInterface;
import players.SpoonPlayer;

import java.util.ArrayList;

public class Game {

    private Player[] player = new Player[2];
    private PlayerSIInterface[] playersAI = new PlayerSIInterface[2];

    private int currentPlayer; //0 - 1
    private int enemyPlayer; //1 - 0

    private int round;

    private int playerWin = -1;

    void init() {
        player[0] = new Player();
        player[0].init(true);
        player[1] = new Player();
        player[1].init(false);

        currentPlayer = 0;
        enemyPlayer = 1;

        round = 1;
    }

    public Game clone() {
        Game newGame = new Game();
        newGame.player[0] = player[0].clone();
        newGame.player[1] = player[1].clone();
        newGame.currentPlayer = currentPlayer;
        newGame.enemyPlayer = enemyPlayer;
        newGame.round = round;
        newGame.playerWin = playerWin;

        return newGame;
    }


    public void setAIPlayers(String player1, String player2) {
        playersAI[0] = getPlayerAIFromString(player1);
        playersAI[1] = getPlayerAIFromString(player2);
    }

    public PlayerSIInterface getPlayerAIFromString(String player) {
        switch (player) {
            case "console":
                return new ConsolePlayer();
            case "aggressive":
                return new SpoonPlayer(2);
            case "defensive":
                return new SpoonPlayer(3);
            case "mixed":
                return new SpoonPlayer(4);
            case "mcts":
                return new MCTSPlayer();
            default:
                return null;
        }
    }

    public void initializeMove() {
        player[currentPlayer].updateMana(round);
        player[currentPlayer].getCard();
        player[currentPlayer].updateCardsAttack();
        player[currentPlayer].clearAllMagicCards();
    }
    public void initializeMove(boolean random) {
        player[currentPlayer].updateMana(round);
        player[currentPlayer].getRandomCard();
        player[currentPlayer].updateCardsAttack();
    }

    public void move() {
        ArrayList<Move> moves = playersAI[currentPlayer].calculateNextMove(this, 100);
        for (Move move : moves) {
            move.perform(player[currentPlayer], player[enemyPlayer]);
            if (player[enemyPlayer].isChampDestroyed()) {
                playerWin = currentPlayer;
                break;
            }
        }
        clearArrays();
    }
    public void clearArrays() {
        player[currentPlayer].clearBoard();
        player[enemyPlayer].clearBoard();
    }
    public boolean performMoves(ArrayList<Move> moves) {
        for (Move move : moves) {
            if (!move.perform(player[currentPlayer], player[enemyPlayer])) {
                return false;
            }
        }
        return true;
    }

    public void nextRound() {
        round += 1;
    }

    public void endTour() {
        currentPlayer = currentPlayer == 0 ? 1 : 0;
        enemyPlayer = enemyPlayer == 0 ? 1 : 0;
    }

    public void refreshGui(GUIInterface gui) {
        refreshGuiForPlayer(gui, PlayerNumber.ONE, 0);
        refreshGuiForPlayer(gui, PlayerNumber.TWO, 1);
    }

    private void refreshGuiForPlayer(GUIInterface gui, PlayerNumber pl, int index) {
        gui.setNumberOfCardsInDeck(pl, player[index].getNumberOfCardsInStack());
        gui.setAmountOfMana(pl, player[index].getMana());
        gui.setNumberOfLifePoints(pl, player[index].getHealth());

        gui.clearCards(pl);
        ArrayList<Card> cards = player[index].getCardsInHand();
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            gui.addCardToPlayersHand(pl, i, card);
        }

        cards = player[index].getCardOnTable();
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            gui.addCardToBattleField(pl, i, card);
        }
        gui.moveNotification(pl, index == currentPlayer);
    }

    public int getCurrentPLayerId()
    {
    	return currentPlayer;
    }
    public Player getCurrentPlayer() {
        return player[currentPlayer];
    }

    public Player getEnemyPlayer() {
        return player[enemyPlayer];
    }

    public boolean didGameEnded() {
        return playerWin != -1;
    }

    public int getPlayerWin() {
        return playerWin;
    }
}
