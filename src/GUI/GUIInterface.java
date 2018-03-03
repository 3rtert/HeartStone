package GUI;

import main.Card;

public interface GUIInterface {
    void setNumberOfCardsInDeck(PlayerNumber player, int number);
    void setAmountOfMana(PlayerNumber player, int amount);
    void setNumberOfLifePoints(PlayerNumber player, int number);
    void addCardToPlayersHand(PlayerNumber player, int index, Card card);
    void addCardToBattleField(PlayerNumber player, int index, Card card);
    void clearCards(PlayerNumber player);
    void moveNotification(PlayerNumber player, boolean set);
}