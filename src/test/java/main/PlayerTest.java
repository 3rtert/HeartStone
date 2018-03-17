package main;

import main.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.Player;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;
    @BeforeEach
    void setUp() {
        player = new Player();
    }

    @Test
    void dealDmgToChampTest() {
        player.dealDmgToChamp(10);

        assertEquals(10, player.getHealth());
        assertFalse(player.isChampDestroyed());

        player.dealDmgToChamp(10);
        assertEquals(0, player.getHealth());
        assertTrue(player.isChampDestroyed());

        player.addToHealth(4);
        player.dealDmgToChamp(1);
        assertEquals(3, player.getHealth());
        assertFalse(player.isChampDestroyed());

        player.dealDmgToChamp(10);
        assertEquals(-7, player.getHealth());
        assertTrue(player.isChampDestroyed());
    }

    @Test
    void dealDmgToCardTest() {
        Card card = new Card(1,2,10,-1, false,true);
        player.addCardToDeck(card);

        assertEquals(1, player.getNumberOfCardsInStack());

        player.getCardFromDeck();

        assertEquals(0, player.getNumberOfCardsInStack());
        assertEquals(1, player.getNumberOfCardsInHand());

        player.putCardOnTable(0);

        assertEquals(0, player.getNumberOfCardsInStack());
        assertEquals(0, player.getNumberOfCardsInHand());
        assertEquals(1, player.getNumberOfCardsOnTable());

        assertEquals(10, card.getLife());
        assertTrue(player.dealDmgToCard(0, 3));
        assertEquals(7, card.getLife());

        assertFalse(player.dealDmgToCard(0, 7));
        assertEquals(0, player.getNumberOfCardsOnTable());
    }

    @Test
    void destroyRandomCardOnTableTest() {
        Card card = new Card(1,2,10,-1,false, true);
        Card card2 = new Card(2,3,15,-1, false, true);
        player.addCardToDeck(card);
        player.addCardToDeck(card2);

        player.getCardFromDeck();
        player.getCardFromDeck();

        player.putCardOnTable(0);
        player.putCardOnTable(0);

        assertEquals(0, player.getNumberOfCardsInStack());
        assertEquals(0, player.getNumberOfCardsInHand());
        assertEquals(2, player.getNumberOfCardsOnTable());

        player.destroyRandomCardOnTable();

        assertEquals(0, player.getNumberOfCardsInStack());
        assertEquals(0, player.getNumberOfCardsInHand());
        assertEquals(1, player.getNumberOfCardsOnTable());

        player.destroyRandomCardOnTable();

        assertEquals(0, player.getNumberOfCardsInStack());
        assertEquals(0, player.getNumberOfCardsInHand());
        assertEquals(0, player.getNumberOfCardsOnTable());
    }

    @Test
    void createRandomDeckTest() {
        player.createRandomDeck();
        assertEquals(20, player.getNumberOfCardsInStack());
    }


}