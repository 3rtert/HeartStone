package tests;

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
    void dealDmgToChamp() {
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
}