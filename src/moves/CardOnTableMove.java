package moves;

import main.Card;
import main.Player;

public class CardOnTableMove implements Move {

    private int cardIndex;

    public CardOnTableMove(int cardIndex) {
        this.cardIndex = cardIndex;
    }

    @Override
    public void perform(Player makingMovePlayer, Player enemyPlayer) {
        try {
            Card card = makingMovePlayer.getCardInHand(cardIndex);
            if (makingMovePlayer.getMana() >= card.getManaCost()) {
                if(card.isMagicCard()) {
                    useMagic(makingMovePlayer, enemyPlayer, card.getNumberOfMagic());
                } else {
                    makingMovePlayer.putCardOnTable(cardIndex);
                    if (card.getNumberOfMagic() != -1) {
                        useMagic(makingMovePlayer, enemyPlayer, card.getNumberOfMagic());
                    }
                }

            }
        } catch (Exception e) {
            System.out.println("Problem with use card function: " + e.getMessage());
        }
    }

    private void useMagic(Player makingMovePlayer, Player enemyPlayer, int numberOfMagic) {
        switch (numberOfMagic) {
            //deal 1 damage to enemy champion
            case 0:
                enemyPlayer.dealDmgToChamp(1);
                break;
            //restore 2 health
            case 1:
                makingMovePlayer.addToHealth(2);
                break;
            //deal 2 damage to all enemyPlayer's cards
            case 2:
                enemyPlayer.dealDmgToAllCards(2);
                break;
            //deal 3 damage to champion
            case 3:
                enemyPlayer.dealDmgToChamp(3);
                break;
            //destroy random enemyPlayer minion
            case 4:
                enemyPlayer.destroyRandomCardOnTable();
                break;
        }
    }
}
