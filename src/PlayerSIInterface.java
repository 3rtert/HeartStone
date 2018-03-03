
public interface PlayerSIInterface 
{
	void init(Game currentGame);
	String getNextMove(); // return for example: a1/2
	FullMove CalculateBextMove(int maxTime); // calculate no longer than maxTime (milliseconds)
}
