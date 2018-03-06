package moves;

import main.Card;
import main.Player;

import java.util.ArrayList;

public class AttackCardMove implements Move {

    private int indexOfAttackingCard;
    private int indexOfAttackedCard;
    private Move enemyMove;

    @Override
    public Move getEnemyMoves() {
        return enemyMove;
    }

    public AttackCardMove(int indexOfAttackingCard, int indexOfAttackedCard) {
        this.indexOfAttackingCard = indexOfAttackingCard;
        this.indexOfAttackedCard = indexOfAttackedCard;
    }

    @Override
    public boolean perform(Player makingMovePlayer, Player enemyPlayer) {
        try {
            Card myCard = makingMovePlayer.getCardOnTable(indexOfAttackingCard);
            if (!myCard.canCardAttack() || enemyPlayer.getHealth() <= 0) {
                return false;
            }
            myCard.attack();
            if (indexOfAttackedCard == -1) {
                enemyPlayer.dealDmgToChamp(myCard.getAttack());
            } else {
                Card enemyCard = enemyPlayer.getCardOnTable(indexOfAttackedCard);
                if (!enemyPlayer.dealDmgToCard(indexOfAttackedCard, myCard.getAttack())) {
                    return false;
                }
                if (enemyCard.getLife() > 0) {
                    makingMovePlayer.dealDmgToCard(indexOfAttackingCard, enemyCard.getAttack());
                    enemyMove = new AttackCardMove(indexOfAttackedCard, indexOfAttackingCard);
                }
            }

        } catch (Exception e) {
            System.out.println("Problem with attack function: " + e.getMessage());
        }
        return true;
    }

    @Override
    public String toString() {
        if (indexOfAttackedCard == -1) {
            return "Attack hero with card " + indexOfAttackingCard;
        } else {
            return "Attack card " + indexOfAttackedCard + " with card " + indexOfAttackingCard;
        }
    }

    @Override
    public int getMoveCost(Player player) {
        return 0;
    }
}
