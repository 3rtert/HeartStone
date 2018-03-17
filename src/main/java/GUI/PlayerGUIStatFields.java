package GUI;

import main.Card;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PlayerGUIStatFields {

    public static final int NR_OF_CARDS_IN_HAND = 20;
    public static final int NR_OF_BATTLE_FIELDS = 7;

    public static final int NR_CARDS_DECK_INDEX = 9;
    public static final int AMOUNT_OF_MANA_INDEX = 0;
    public static final int NAME_INDEX = 5;
    public static final int MOVE_NOTIFICATION = 6;
    public static final int LIFE_POINT_NUMBER_INDEX = 4;

    private ArrayList<JLabel> cardsInHand = new ArrayList<>();

    private ArrayList<JLabel> fields = new ArrayList<>();
    private ArrayList<JLabel> battleFields = new ArrayList<>();

    public PlayerGUIStatFields() {
        for (int i = 0; i < 10; i++) {
            fields.add(new JLabel(""));
        }
        fields.get(NR_CARDS_DECK_INDEX).setBackground(Color.orange);
        fields.get(NAME_INDEX).setBackground(Color.RED);
        fields.get(AMOUNT_OF_MANA_INDEX).setBackground(Color.BLUE);
        fields.get(LIFE_POINT_NUMBER_INDEX).setBackground(Color.GREEN);
        for (int i = 0; i < NR_OF_BATTLE_FIELDS ; i++) {
            JLabel field = new JLabel("");
            field.setBackground(Color.lightGray);
            battleFields.add(field);
        }
        for (int i = 0; i < 10 - NR_OF_BATTLE_FIELDS; i++) {
            JLabel field = new JLabel("");
            field.setBackground(Color.lightGray);
            battleFields.add(field);
        }

        for(int i = 0; i < NR_OF_CARDS_IN_HAND; i++) {
            JLabel field = new JLabel("");
            field.setBackground(Color.CYAN);
            cardsInHand.add(field);
        }
    }

    public ArrayList<JLabel> getHandFields() {
        return cardsInHand;
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

    public void addCardToHand(int index, Card card) {
        cardsInHand.get(index).setText(card.toString());
    }

    public void addCardToBattleField(int index, Card card) {
        battleFields.get(index).setText(card.toString());
    }

    public void clearCards() {
        for(JLabel label: cardsInHand) {
            label.setText("");
        }

        for(JLabel label: battleFields) {
            label.setText("");
        }
    }

    public void moveNotification(boolean set) {
        fields.get(MOVE_NOTIFICATION).setText(set ? "Your move" : "");
    }
}