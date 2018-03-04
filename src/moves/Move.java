package moves;

import main.Player;

public interface Move {
    void perform(Player makingMovePlayer, Player enemyPlayer);
    String toString();
    int getMoveCost(Player player);
}
