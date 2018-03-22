package players;

import main.Game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import moves.AttackCardMove;
import moves.CardOnTableMove;
import moves.Move;

public class ConsolePlayer implements PlayerSIInterface {

    @Override
    public ArrayList<Move> calculateNextMove(Game game, long maxTime) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<Move> moves = new ArrayList<>();
        // u-1 - use card 1
        // a-1-2 - attack card 2 by card 1
        // h-1 - attack enemyPlayer by card 1
        // e - end tour
        while(true) {
            try {
                System.out.println("Podaj nastepny ruch: ");
                char[] action = in.readLine().toCharArray();

                String textOfCard = new String(action);

                String[] textOfCards = textOfCard.split("-");
                int firstNumber = -1;
                if(textOfCards.length > 1) {
                    firstNumber = Integer.parseInt(textOfCards[1]);
                }
                int secondNumber = -1;
                if(textOfCards.length > 2) {
                    secondNumber = Integer.parseInt(textOfCards[2]);
                }
                if (action[0] == 'u') {
                    moves.add(new CardOnTableMove(firstNumber));
                } else if (action[0] == 'a') {
                    moves.add(new AttackCardMove(firstNumber, secondNumber));
                } else if (action[0] == 'h') {
                    moves.add(new AttackCardMove(firstNumber, -1));
                } else if (action[0] == 'e') {
                    System.out.println("Koniec ruchu");
                    break;
                }
            } catch (Exception e) {
                System.out.println("Not valid entry: " + e.toString());
            }
        }
        return moves;
    }
}
