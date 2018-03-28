package main;

import GUI.GUI;
import GUI.GUIInterface;

public class GameController {

    private boolean withGui;
    private int moveMaxTime = 100;
    private float c_param = 1;
    private GUIInterface gui;
    private int simulateBestOf_param=1;

    private Game game;

    public GameController(boolean withGui) {
        this.withGui = withGui;
        if(withGui) {
            this.gui = new GUI();
        }
    }

    public void init() {
        this.game = new Game();
        game.init();
    }

    public void setPlayersAI(String player1, String player2) {
        game.setAIPlayers(player1, player2, c_param, simulateBestOf_param);
    }

    private void playerMove() {
        game.initializeMove(false);

        if (withGui) {
            gui.refresh(game);
        }

        game.move(moveMaxTime);
        game.endTour();
    }

    public int startGame() {
        while (!game.didGameEnded()) 
        {
            game.nextRound();
            playerMove();
            playerMove();
        }
        return game.getPlayerWin();
    }

    public void setMoveMaxTime(int moveMaxTime) {
        this.moveMaxTime = moveMaxTime;
    }

    public void setC_param(float c_param) {
        this.c_param = c_param;
    }
}
