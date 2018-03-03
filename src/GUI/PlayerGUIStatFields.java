package GUI;

import javax.swing.*;
import java.util.ArrayList;

public class PlayerGUIStatFields {

    public static final int NR_OF_CARDS_IN_HAND = 3;
    public static final int NR_OF_BATTLE_FIELDS = 7;

    public static final int NR_CARDS_DECK_INDEX = 6;
    public static final int AMOUNT_OF_MANA_INDEX = 5;
    public static final int NAME_INDEX = 4;
    public static final int LIFE_POINT_NUMBER_INDEX = 3;

    private ArrayList<JLabel> fields = new ArrayList<>();
    private ArrayList<JLabel> battleFields = new ArrayList<>();

    public PlayerGUIStatFields() {
        for (int i = 0; i < NR_OF_CARDS_IN_HAND + 4; i++) {
            fields.add(new JLabel("" + 0));
        }

        for (int i = 0; i < NR_OF_BATTLE_FIELDS; i++) {
            battleFields.add(new JLabel("Empty"));
        }
    }

    public ArrayList<JLabel> getFields() {
        return fields;
    }

    public ArrayList<JLabel> getBattleFields() {
        return battleFields;
    }

    public void setPlayerName(String name) {
        fields.get(NAME_INDEX).setText("Player " + name);
    }

    public void setNrOfCardsInDeck(Integer number) {
        fields.get(NR_CARDS_DECK_INDEX).setText("Deck: " + number);
    }

    public void setAmountOfMana(Integer amount) {
        fields.get(AMOUNT_OF_MANA_INDEX).setText("Mana: " + amount);
    }

    public void setNumberOfLifePoints(Integer number) {
        fields.get(LIFE_POINT_NUMBER_INDEX).setText("Life: " + number);
    }

    public void addCardToHand(int index, int cardNumber, int dmg, int life, int manaExpense, int magicNumber) {
        setCardText(fields.get(index), cardNumber, dmg, life, manaExpense, magicNumber);
    }

    public void addCardToBattleField(int index, int cardNumber, int dmg, int life, int manaExpense, int magicNumber) {
        setCardText(battleFields.get(index), cardNumber, dmg, life, manaExpense, magicNumber);
    }

    private void setCardText(JLabel card, int cardNumber, int dmg, int life, int manaExpense, int magicNumber) {
        card.setText("<html>" + "Card number: " + cardNumber + "<br> Attack: " + dmg +
                "<br> Life: " + life + "<br> Mana: " + manaExpense + "<br> Magic effect: " + magicNumber + "</html>");
    }
}