public interface GUIInterface {
    void setNumberOfCardsInDeck(PlayerNumber player, int number);
    void setAmountOfMana(PlayerNumber player, int amount);
    void setNumberOfLifePoints(PlayerNumber player, int number);
    void addCardToPlayersHand(PlayerNumber player, int index, int cardNumber, int dmg, int life, int manaExpense, int magicNumber);
    void addCardToBattleField(PlayerNumber player, int index, int cardNumber, int dmg, int life, int manaExpense, int magicNumber);
}