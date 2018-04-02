package main;

import moves.Move;
import players.*;

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
        newGame.playersAI[0]=playersAI[0];
        newGame.playersAI[1]=playersAI[1];
        newGame.currentPlayer = currentPlayer;
        newGame.enemyPlayer = enemyPlayer;
        newGame.round = round;
        newGame.playerWin = playerWin;

        return newGame;
    }


    public void setAIPlayers(String player1, String player2, float c, int simulateBestOf) {
        playersAI[0] = getPlayerAIFromString(player1, c, simulateBestOf);
        playersAI[1] = getPlayerAIFromString(player2, c, simulateBestOf);
    }

    public PlayerSIInterface getPlayerAIFromString(String player, float c, int simulateBestOf) {
        switch (player) {
            case "console":
                return new ConsolePlayer();
            case "aggressive":
                return new SpoonPlayer(0);
            case "controlling":
                return new SpoonPlayer(1);
            case "mcts":
                return new MCTSPlayer(c,simulateBestOf);
            case "random":
                return new RandomPlayer();
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

        if (player[enemyPlayer].isChampDestroyed()) {
            playerWin = player[currentPlayer].getId();
        }
        player[currentPlayer].updateCardsAttack();
        player[currentPlayer].clearAllMagicCards();
    }

    public void move(int maxTime) {
        ArrayList<Move> moves = playersAI[currentPlayer].calculateNextMove(this, maxTime);
        
        /*System.out.println(currentPlayer==0?"mcts":"random");
        for(Move m : moves)
        {
        	System.out.println(m.toString());
        }*/
        
        //MovesGenerator.printArrayOfMoves(moves);
        for (Move move : moves) {
            move.perform(player[currentPlayer], player[enemyPlayer]);
            if (player[enemyPlayer].isChampDestroyed()) {
                playerWin = player[currentPlayer].getId();
                break;
            }
        }
    }

    public boolean performMoves(ArrayList<Move> moves) {
        for (Move move : moves) {
            if (!move.perform(player[currentPlayer], player[enemyPlayer])) {
                return false;
            }
            if (player[enemyPlayer].isChampDestroyed()) {
                playerWin = player[currentPlayer].getId();
                break;
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
