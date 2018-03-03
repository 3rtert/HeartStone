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
    public void init(Game currentGame) {

    }

    @Override
    public String getNextMove() {
        return null;
    }

    @Override
    public ArrayList<Move> calculateNextMove(int maxTime) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<Move> moves = new ArrayList<>();
        // u1 - use card 1
        // a1-2 - attack card 2 by card 1
        // h1 - attack enemyPlayer by card 1
        // e - end tour
        while(true) {
            try {
                System.out.println("Podaj nastepny ruch: ");
                char[] action = in.readLine().toCharArray();

                String textOfCard = new String(action);

                String[] textOfCards = textOfCard.split("-");
                int[] numberOfCard = new int[textOfCard.length()];

                if (!textOfCards[0].equals("")) {
                    for (int i = 0; i < textOfCards.length; i++) {
                        numberOfCard[i] = Integer.parseInt(textOfCards[i]);
                    }
                }
                if (action[0] == 'u') {
                    moves.add(new CardOnTableMove(numberOfCard[0]));
                } else if (action[0] == 'a') {
                    moves.add(new AttackCardMove(numberOfCard[0], numberOfCard[1]));
                } else if (action[0] == 'h') {
                    moves.add(new AttackCardMove(numberOfCard[0], -1));
                } else if (action[0] == 'e') {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Not valid entry: " + e.getMessage());
            }
        }
        return moves;
    }
}
