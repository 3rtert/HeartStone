package main;

import GUI.GUI;
import GUI.GUIInterface;

public class GameController {

    private boolean withGui;
    private GUIInterface gui;

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
        game.setAIPlayers(player1, player2);
    }

    private void playerMove() {
        game.initializeMove();

        if (withGui) {
            game.refreshGui(gui);
        }

        game.move();
        game.endTour();
    }

    public int startGame() {
        while (!game.didGameEnded()) 
        {
            playerMove();
            game.nextRound();
        }
        return game.getPlayerWin();
    }
}
