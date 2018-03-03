package moves;

import main.Player;

public interface Move {
    public void perform(Player makingMovePlayer, Player enemyPlayer);
}
