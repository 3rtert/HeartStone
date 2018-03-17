package moves;

import main.Player;

import java.util.ArrayList;

public interface Move {
    boolean perform(Player makingMovePlayer, Player enemyPlayer);
    String toString();
    int getMoveCost(Player player);
    Move getEnemyMoves();
}
