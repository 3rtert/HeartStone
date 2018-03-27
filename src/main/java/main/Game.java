package main;

import moves.Move;
import players.ConsolePlayer;
import players.MCTSPlayer;
import players.PlayerSIInterface;
import players.SpoonPlayer;

import java.util.ArrayList;

public class Game {

    private Player[] player = new Player[2];
    private PlayerSIInterface[] playersAI = new PlayerSIInterface[2];

    private int currentPlayer = 0; //0 - 1
    private int enemyPlayer = 1; //1 - 0

    private int round = 0;

    private int playerWin = -1;

    void init() {
        player[0] = new Player();
        player[0].init(true);
        player[1] = new Player();
        player[1].init(false);
    }

    public void testInit() {
        player[0] = new Player();
        player[1] = new Player();
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

    public void initializeMove(boolean random) {
        player[currentPlayer].updateMana(round);

        if(random) {
            player[currentPlayer].getRandomCard();
        } else {
            player[currentPlayer].getCard();
        }

        player[currentPlayer].updateCardsAttack();
        player[currentPlayer].clearAllMagicCards();
    }

    public void move(int maxTime) {
        ArrayList<Move> moves = playersAI[currentPlayer].calculateNextMove(this, maxTime);
        //MovesGenerator.printArrayOfMoves(moves);
        for (Move move : moves) {
            move.perform(player[currentPlayer], player[enemyPlayer]);
            if (player[enemyPlayer].isChampDestroyed()) {
                playerWin = currentPlayer;
                break;
            }
        }
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

    public int getCurrentPLayerId() {
    	return currentPlayer;
    }

    public Player getCurrentPlayer() {
        return player[currentPlayer];
    }

    public Player getEnemyPlayer() {
        return player[enemyPlayer];
    }

    public Player getPlayer(int index) {
        return player[index];
    }

    public boolean didGameEnded() {
        return playerWin != -1;
    }

    public int getPlayerWin() {
        return playerWin;
    }

    public int getRound() {
        return round;
    }
}
