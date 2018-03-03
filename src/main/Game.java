package main;

import GUI.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game {
    private Player[] player = new Player[2];

    private int currentPlayer; //0 - 1
    private int enemyPlayer; //1 - 0


    int round = 0;
    int leftMana;
    private int playerWin = -1;

    void init(boolean withGui) {
        player[0] = new Player();
        player[1] = new Player();

        player[0].createRandomDeck();
        player[0].randomCard();
        player[0].randomCard();
        player[0].randomCard();

        player[1].createRandomDeck();
        player[1].randomCard();
        player[1].randomCard();
        player[1].randomCard();
        player[1].randomCard();

        currentPlayer = 0;
        enemyPlayer = 1;
        if (withGui) {
            GUIInterface gui = new GUI();
            gui.setNumberOfCardsInDeck(PlayerNumber.ONE, 18);
            gui.setNumberOfCardsInDeck(PlayerNumber.TWO, 17);
            gui.setAmountOfMana(PlayerNumber.ONE, 1);
            gui.setAmountOfMana(PlayerNumber.TWO, 1);
            gui.setNumberOfLifePoints(PlayerNumber.ONE, 20);
            gui.setNumberOfLifePoints(PlayerNumber.TWO, 20);

            gui.addCardToPlayersHand(PlayerNumber.ONE, 2, 1, 10, 10, 10, 1);
            gui.addCardToPlayersHand(PlayerNumber.TWO, 1, 2, 100, 100, 100, 100);
        }
    }

    void start() throws IOException {
        while (playerWin == -1) {
            move();
        }
        System.out.println("Wygral gracz numer: " + playerWin);
    }

    void move() throws IOException {
        if (currentPlayer == 1) {
            nextRound();
        }
        updateMana();
        getCard();
        player[currentPlayer].updateCardsAttack();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Podaj nastepny ruch: ");
        // u1 - use card 1
        // a1-2 - attack card 2 by card 1
        // h1 - attack enemyPlayer by card 1
        // e - end tour

        char[] action = in.readLine().toCharArray();
        String textOfCard = "";
        for (int i = 1; i < action.length; i++) {
            textOfCard += action[i];
        }

        String[] textOfCards = textOfCard.split("-");
        int[] numberOfCard = new int[textOfCard.length()];

        if (!textOfCards[0].equals("")) {
            for (int i = 0; i < textOfCards.length; i++) {
                numberOfCard[i] = Integer.parseInt(textOfCards[i]);
            }
        }
        if (action[0] == 'u') {
            useCard(numberOfCard[0]);
        } else if (action[0] == 'a') {
            attack(numberOfCard[0], numberOfCard[1]);
        } else if (action[0] == 'h') {
            attack(numberOfCard[0], -1);
        } else if (action[0] == 'e') {
            endTour();
        }
        if (player[enemyPlayer].isChampDestroyed()) {
            playerWin = currentPlayer;
        }
    }

    public void attack(int indexOfMyCard, int indexOfEnemyCard) // -1 - enemyPlayer champion
    {
        try {
            Card myCard = player[currentPlayer].getCardOnTable(indexOfMyCard);
            if (myCard.canCardAttack()) {
                myCard.attack();
                if (indexOfEnemyCard == -1) {
                    dealDmgToChamp(enemyPlayer, myCard.getAttack());
                } else {
                    Card enemyCard = player[enemyPlayer].getCardOnTable(indexOfEnemyCard);
                    if (player[enemyPlayer].dealDmgToCard(indexOfEnemyCard, myCard.getAttack())) {
                        player[currentPlayer].dealDmgToCard(indexOfMyCard, enemyCard.getAttack());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Problem with attack function: " + e.getMessage());
        }
    }

    void useMagic(int numberOfMagic) {
        switch (numberOfMagic) {
            //deal 1 damage to champion
            case 0:
                dealDmgToChamp(enemyPlayer, 1);
                break;
            //restore 2 health
            case 1:
                player[currentPlayer].addToHealth(2);
                break;
            //deal 2 damage to all enemyPlayer's cards
            case 2:
                for (int i = 0; i < 7; i++)
                    player[enemyPlayer].dealDmgToCard(i, 2);
                break;
            //deal 3 damage to champion
            case 3:
                dealDmgToChamp(enemyPlayer, 3);
                break;
            //destroy random enemyPlayer minion
            case 4:
                player[enemyPlayer].destroyRandomCardOnTable();
                break;
        }
    }

    private void useCard(int indexOfCard) {
        try {
            Card card = player[currentPlayer].getCardInHand(indexOfCard);
            if (leftMana >= card.getManaCost()) {
                if (card.getNumberOfMagic() != -1) {
                    useMagic(card.getNumberOfMagic());
                    player[currentPlayer].destroyCardInHand(indexOfCard);
                }
            }
        } catch (Exception e) {
            System.out.println("Problem with use card function: " + e.getMessage());
        }
    }

    void getCard() {
        if (player[currentPlayer].getNumberOfCardsInStack() > 0)
            player[currentPlayer].randomCard();
        else
            dealDmgToChamp(currentPlayer, 1);
    }

    void updateMana() {
        leftMana = Math.min(round, 10);
    }

    void nextRound() {
        round += 1;
    }

    void endTour() {
        currentPlayer = currentPlayer == 0 ? 1 : 0;
        enemyPlayer = enemyPlayer == 0 ? 1 : 0;
    }

    private void dealDmgToChamp(int indexOfPLayer, int dmg) {
        player[indexOfPLayer].dealDmgToChamp(dmg);
        if (player[indexOfPLayer].isChampDestroyed())
            playerWin = indexOfPLayer == 0 ? 1 : 0;
    }

}
