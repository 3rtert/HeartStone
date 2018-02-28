
public class Card 
{
	int cost=-1;
	int attak=-1;
	int life=-1;
	
	boolean attakPossible=false;
	
	int numberOfMagic=-1; // -1 if magic spell not exist
	
	// card with effect
	// 0 - deal 1 damage
	// 1 - restore 2 health
	
	// spells
	// 2 - deal 2 damage to all enemies
	// 3 - deal 3 damage
	// 4 - destroy random enemy minion
	
	Card(int c, int a, int l, int magic)
	{
		cost=c;
		attak=a;
		life=l;
		numberOfMagic=magic;
	}
}
