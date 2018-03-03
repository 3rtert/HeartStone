package players;

import java.util.ArrayList;
import java.util.List;
import main.Game;

public class TreeOfGame 
{
	List trees = new ArrayList<TreeOfGame>();
	Game currentGame;
	int wins=0;
	int loses=0;
	
	void init(Game game)
	{
		currentGame=game.clone();
	}
	ArrayList<FullMove> getAllMoves()
	{
		return null;
	}
}
