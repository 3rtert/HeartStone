package main;

import GUI.*;

import java.awt.EventQueue;

public class Test {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUIInterface gui = new GUI();
                gui.setNumberOfCardsInDeck(PlayerNumber.ONE, 4);
                gui.setNumberOfCardsInDeck(PlayerNumber.TWO, 6);
                gui.setAmountOfMana(PlayerNumber.TWO, 30);
                gui.setNumberOfLifePoints(PlayerNumber.ONE, 100);
                gui.addCardToPlayersHand(PlayerNumber.ONE,2,1,10,10,10,1);
                gui.addCardToPlayersHand(PlayerNumber.TWO,1,2,100,100,100,100);
                gui.addCardToBattleField(PlayerNumber.TWO,6,2,100,100,100,100);
                gui.addCardToBattleField(PlayerNumber.ONE,6,2,100,100,100,100);
            }
        });
    }
}