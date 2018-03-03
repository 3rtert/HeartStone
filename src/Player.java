import java.util.Random;

public class Player 
{
	int hp=20;
	Card[] cardOnTable;
	Card[] cardsInHand;
	
	Card[] stack;
	int indexOfNextCard=0;
	
	protected Player clone()
	{
		Player newPlayer=new Player();
		newPlayer.hp=hp;
		newPlayer.indexOfNextCard=indexOfNextCard;
		for(int i=0;i<cardOnTable.length;i++)
		{
			if(cardOnTable[i]!=null)
				newPlayer.cardOnTable[i]=cardOnTable[i].clone();
		}
		for(int i=0;i<cardsInHand.length;i++)
		{
			if(cardsInHand[i]!=null)
				newPlayer.cardsInHand[i]=cardsInHand[i].clone();
		}
		for(int i=0;i<stack.length;i++)
		{
			if(stack[i]!=null)
				newPlayer.stack[i]=stack[i].clone();
		}
		
		return newPlayer;
	}
	
	Player()
	{
		cardOnTable=new Card[7];
		cardsInHand=new Card[20];
		stack=new Card[20];
		randomCards();
	}
	
	boolean dealDmgToChamp(int dmg)
	{
		hp-=dmg;
		return hp<=0;
	}
	void cardAttak(int index)
	{
		if(cardOnTable[index]!=null && cardOnTable[index].attakPossible)
			cardOnTable[index].attakPossible=false;
	}
	void dealDmgToCard(int indexOfCard, int dmg)
	{
		if(cardOnTable[indexOfCard]!=null)
		{
			cardOnTable[indexOfCard].life-=dmg;
			if(cardOnTable[indexOfCard].life<=0)
				cardOnTable[indexOfCard]=null;
		}
	}
	int cardsOnStack()
	{
		return 20-indexOfNextCard;
	}
	
	boolean cardCanAttak(int numberOfCard)
	{
		return cardOnTable[numberOfCard]!=null && cardOnTable[numberOfCard].attakPossible;
	}
	
	boolean cardCanBeAttaked(int numberOfCard)
	{
		return cardOnTable[numberOfCard]!=null;
	}
	
	void updateCardsAttak()
	{
		for(int i=0;i<7;i++)
		{
			if(cardOnTable[i]!=null)
				cardOnTable[i].attakPossible=true;
		}
	}
	
	boolean putCardOnTable(Card card)
	{
		boolean done=false;
		for(int i=0;i<7 && !done;i++)
		{
			if(cardOnTable[i]==null)
			{
				cardOnTable[i]=card;
				done=true;
			}
		}
		return done;
	}
	
	void randomCard()
	{
		boolean done=false;
		for(int i=0;i<cardsInHand.length && !done;i++)
		{
			if(cardsInHand[i]==null)
			{
				cardsInHand[i]=stack[indexOfNextCard++];
				done=true;
			}
		}	
	}
	
	void randomCards()
	{
		int[] cost={10,8,6,5,3,2,2,2,2,3};
		int[] attak={11,6,6,5,3,2,2,0,0,0};
		int[] life={11,4,6,5,4,1,2,0,0,0};
		
		// 0 - deal 1 damage
		// 1 - restore 2 health
		
		// 2 - deal 2 damage to all enemies
		// 3 - deal 3 damage
		// 4 - destroy random enemy minion
		
		int[] numberOfMagic={-1,-1,-1,-1,-1,0,1,2,3,4};
		
		int[] cards= {2,2,2,2,2,2,2,2,2,2};
		Random random = new Random();
		for(int i=0;i<20;i++)
		{
			int nextCard=random.nextInt(10);
			boolean done=false;
			while(!done)
			{
				if(cards[nextCard]>0)
				{
					cards[nextCard]-=1;
					stack[i]=new Card(cost[nextCard],attak[nextCard],life[nextCard],numberOfMagic[nextCard]);
					done=true;
				}
				else
				{
					nextCard++;
					if(nextCard==10)
						nextCard=0;
				}
			}
		}
	}
}
