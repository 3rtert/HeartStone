package moves;

import main.Card;
import main.Player;

public class AttackCardMove implements Move {

    private int indexOfAttackingCard;
    private int indexOfAttackedCard;

    public AttackCardMove(int indexOfAttackingCard, int indexOfAttackedCard) {
        this.indexOfAttackingCard = indexOfAttackingCard;
        this.indexOfAttackedCard = indexOfAttackedCard;
    }

    @Override
    public void perform(Player makingMovePlayer, Player enemyPlayer) {
        try {
            Card myCard = makingMovePlayer.getCardOnTable(indexOfAttackingCard);
            if (myCard.canCardAttack()) {
                myCard.attack();
                if (indexOfAttackedCard == -1) {
                    enemyPlayer.dealDmgToChamp(myCard.getAttack());
                } else {

                    Card enemyCard = enemyPlayer.getCardOnTable(indexOfAttackedCard);
                    if (enemyPlayer.dealDmgToCard(indexOfAttackedCard, myCard.getAttack())) {
                        makingMovePlayer.dealDmgToCard(indexOfAttackingCard, enemyCard.getAttack());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Problem with attack function: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        if(indexOfAttackedCard == -1) {
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
