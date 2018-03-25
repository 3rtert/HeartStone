package main;

import moves.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MovesGeneratorTest {

    private Game game;
    @BeforeEach
    void setUp() {
        game = new Game();
        game.testInit();
    }

    @Test
    void testGetCardOnTableMoves() {

        game.getCurrentPlayer().addCardToHand(new Card(1,1,3,-1,false,true));
        game.getCurrentPlayer().addCardToHand(new Card(1,2,2,-1,false,true));
        game.getCurrentPlayer().setMana(20);

        ArrayList<Move> moves = MovesGenerator.getAllPossibleSingleCardOnTableMoves(game);
        assertEquals(2, moves.size());

        game.getCurrentPlayer().addCardToHand(new Card(30,2,2,-1,false,true));
        game.getCurrentPlayer().addCardToHand(new Card(10,2,2,-1,false,true));

        moves = MovesGenerator.getAllPossibleSingleCardOnTableMoves(game);
        assertEquals(3, moves.size());
    }

    @Test
    void testCardOnTableMovesCombinations() {
        game.getCurrentPlayer().addCardToHand(new Card(1,1,3,-1,false,true));
        game.getCurrentPlayer().setMana(5);

        ArrayList<ArrayList<Move>> allMoves = MovesGenerator.getAllPossibleCardOnTableMoves(game);

        assertEquals(1, allMoves.size());

        game.getCurrentPlayer().addCardToHand(new Card(2,1,3,-1,false,true));

        allMoves = MovesGenerator.getAllPossibleCardOnTableMoves(game);
        assertEquals(3, allMoves.size());

        game.getCurrentPlayer().addCardToHand(new Card(2,1,3,-1,false,true));

        allMoves = MovesGenerator.getAllPossibleCardOnTableMoves(game);
        assertEquals(7, allMoves.size());

        game.getCurrentPlayer().addCardToHand(new Card(2,1,3,-1,false,true));

        allMoves = MovesGenerator.getAllPossibleCardOnTableMoves(game);
        assertEquals(13, allMoves.size());

        game.getCurrentPlayer().addCardToBoard(new Card(2,1,3,-1,false,true));
        game.getCurrentPlayer().addCardToBoard(new Card(2,1,3,-1,false,true));
        game.getCurrentPlayer().addCardToBoard(new Card(2,1,3,-1,false,true));
        game.getCurrentPlayer().addCardToBoard(new Card(2,1,3,-1,false,true));
        game.getCurrentPlayer().addCardToBoard(new Card(2,1,3,-1,false,true));

        allMoves = MovesGenerator.getAllPossibleCardOnTableMoves(game);
        assertEquals(10, allMoves.size());
    }

    @Test
    void testSingleAttackCardMoves() {
        game.getCurrentPlayer().addCardToBoard(new Card(2,1,3,-1,false,true));

        ArrayList<Move> moves = MovesGenerator.getAllPossibleSingleCardAttackMoves(game);
        assertEquals(0, moves.size());

        game.getEnemyPlayer().addCardToBoard(new Card(2,1,3,-1,false,true));

        moves = MovesGenerator.getAllPossibleSingleCardAttackMoves(game);
        assertEquals(1, moves.size());

        game.getCurrentPlayer().addCardToBoard(new Card(2,1,3,-1,false,true));
        game.getEnemyPlayer().addCardToBoard(new Card(2,1,3,-1,false,true));

        moves = MovesGenerator.getAllPossibleSingleCardAttackMoves(game);
        assertEquals(4, moves.size());
    }

    @Test
    void testCombinationsOfAttacks() {
        game.getCurrentPlayer().addCardToBoard(new Card(2,1,3,-1,false,true));

        ArrayList<ArrayList<Move>> moves = MovesGenerator.getAllPossibleCardAttackMoves(game);
        assertEquals(1, moves.size());

        game.getEnemyPlayer().addCardToBoard(new Card(2,1,3,-1,false,true));

        moves = MovesGenerator.getAllPossibleCardAttackMoves(game);
        assertEquals(2, moves.size());

        game.getCurrentPlayer().addCardToBoard(new Card(2,1,3,-1,false,true));
        moves = MovesGenerator.getAllPossibleCardAttackMoves(game);

        ArrayList<Move> singleMoves = MovesGenerator.getAllPossibleSingleCardAttackMoves(game);
        assertEquals(2, singleMoves.size());

        assertEquals(4, moves.size());

        game.getEnemyPlayer().addCardToBoard(new Card(2,1,1,-1,false,true));

        moves = MovesGenerator.getAllPossibleCardAttackMoves(game);
        assertEquals(8, moves.size());
        for(ArrayList<Move> m: moves){
            assertEquals(2, m.size());
        }
    }
}
