package players;

import main.Game;
import main.MovesGenerator;
import moves.Move;

import java.util.ArrayList;
import java.util.Random;

public class RandomPlayer implements PlayerSIInterface {

    @Override
    public ArrayList<Move> calculateNextMove(Game currentGame, long maxTime) {

        ArrayList<ArrayList<Move>> allMoves = MovesGenerator.getAllMoves(currentGame);
        Random random = new Random();
        return allMoves.get(random.nextInt(allMoves.size()));

    }
}
