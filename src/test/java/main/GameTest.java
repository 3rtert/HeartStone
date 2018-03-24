package main;

import moves.AttackCardMove;
import moves.CardOnTableMove;
import moves.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    void basicTest() {
        game.init();
        game.nextRound();

        game.initializeMove(false);

        assertEquals(0, game.getCurrentPLayerId());
        assertEquals(4, game.getCurrentPlayer().getNumberOfCardsInHand());
        assertEquals(1, game.getCurrentPlayer().getMana());
    }

    @Test
    void cardOnTableTest() {
        game.testInit();

        game.getCurrentPlayer().addCardToHand(new Card(1,1,3,-1,false,true));
        game.getCurrentPlayer().addCardToHand(new Card(1,2,2,-1,false,true));
        game.getCurrentPlayer().setMana(20);

        ArrayList<Move> moves = new ArrayList<>();
        moves.add(new CardOnTableMove(1));
        game.performMoves(moves);

        assertEquals(1, game.getCurrentPlayer().getNumberOfCardsInHand());
        assertEquals(1, game.getCurrentPlayer().getNumberOfCardsOnTable());
        assertEquals(2, game.getCurrentPlayer().getCardOnTable(0).getLife());
        assertEquals(19, game.getCurrentPlayer().getMana());

        ArrayList<Move> newMoves = new ArrayList<>();
        newMoves.add(new CardOnTableMove(0));
        game.performMoves(newMoves);

        assertEquals(0, game.getCurrentPlayer().getNumberOfCardsInHand());
        assertEquals(2, game.getCurrentPlayer().getNumberOfCardsOnTable());
        assertEquals(18, game.getCurrentPlayer().getMana());

        game.getEnemyPlayer().addCardToHand(new Card(1,2,2,-1,false,true));
        game.getEnemyPlayer().putCardOnTable(0);


        ArrayList<Move> attackMoves = new ArrayList<>();
        attackMoves.add(new AttackCardMove(0, -1));
        attackMoves.add(new AttackCardMove(1, 0));

        game.performMoves(attackMoves);

        assertEquals(18, game.getEnemyPlayer().getHealth());
        assertEquals(1, game.getEnemyPlayer().getCardOnTable(0).getLife());
        assertFalse(game.getCurrentPlayer().getCardOnTable(0).canCardAttack());


        game.getCurrentPlayer().getCardOnTable(0).enableAttack();
        attackMoves = new ArrayList<>();
        attackMoves.add(new AttackCardMove(0, 0));
        game.performMoves(attackMoves);


        assertNull(game.getEnemyPlayer().getCardOnTable(0));
    }

    @Test
    void magicCardsTest() {
        game.testInit();

        game.getCurrentPlayer().addCardToHand(new Card(1,1,1,0,false,true));
        game.getCurrentPlayer().addCardToHand(new Card(2,2,2,1,false,true));
        game.getCurrentPlayer().addCardToHand(new Card(3,2,2,2,true,true));
        game.getCurrentPlayer().addCardToHand(new Card(4,2,2,3,true,true));
        game.getCurrentPlayer().addCardToHand(new Card(5,2,2,4,true,true));
        game.getCurrentPlayer().setMana(20);

        game.getEnemyPlayer().setMana(20);
        game.getEnemyPlayer().addCardToHand(new Card(1,2,2,-1,false,true));
        game.getEnemyPlayer().putCardOnTable(0);

        assertEquals(1, game.getEnemyPlayer().getNumberOfCardsOnTable());

        ArrayList<Move> moves = new ArrayList<>();
        moves.add(new CardOnTableMove(4));
        game.performMoves(moves);

        assertEquals(0, game.getEnemyPlayer().getNumberOfCardsOnTable());

        game.getEnemyPlayer().addCardToHand(new Card(1,2,2,-1,false,true));
        game.getEnemyPlayer().putCardOnTable(0);
        game.getEnemyPlayer().addCardToHand(new Card(1,2,3,-1,false,true));
        game.getEnemyPlayer().putCardOnTable(0);

        moves = new ArrayList<>();
        moves.add(new CardOnTableMove(0));
        moves.add(new CardOnTableMove(1));
        moves.add(new CardOnTableMove(2));
        moves.add(new CardOnTableMove(3));
        game.performMoves(moves);

        assertEquals(16, game.getEnemyPlayer().getHealth());
        assertEquals(22, game.getCurrentPlayer().getHealth());
        assertEquals(1, game.getEnemyPlayer().getCardOnTable(1).getLife());
        assertNull(game.getEnemyPlayer().getCardOnTable(0));

        game.getEnemyPlayer().addCardToHand(new Card(1,2,3,-1,false,true));
        game.getEnemyPlayer().putCardOnTable(0);

        assertNotNull(game.getEnemyPlayer().getCardOnTable(0));
    }


    @Test
    void testManaAndRound() {
        game.init();

        assertEquals(0, game.getRound());

        playRounds(game, 10);

        assertEquals(10, game.getRound());
        assertEquals(10, game.getCurrentPlayer().getMana());
        assertEquals(13, game.getCurrentPlayer().getNumberOfCardsInHand());
        assertEquals(20, game.getCurrentPlayer().getHealth());

        playRounds(game, 7);
        assertEquals(17, game.getRound());
        assertEquals(10, game.getCurrentPlayer().getMana());
        assertEquals(20, game.getCurrentPlayer().getNumberOfCardsInHand());
        assertEquals(20, game.getCurrentPlayer().getHealth());

        playRounds(game, 1);
        assertEquals(18, game.getRound());
        assertEquals(20, game.getCurrentPlayer().getNumberOfCardsInHand());
        assertEquals(19, game.getCurrentPlayer().getHealth());

        playRounds(game, 2);
        assertEquals(20, game.getRound());
        assertEquals(20, game.getCurrentPlayer().getNumberOfCardsInHand());
        assertEquals(14, game.getCurrentPlayer().getHealth());
    }

    private void playRounds(Game game, int n) {
        for(int i = 0; i < n; i++) {
            game.nextRound();

            game.initializeMove(false);
            game.endTour();

            game.initializeMove(false);
            game.endTour();

        }
    }



}