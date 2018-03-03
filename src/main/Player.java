package main;

import java.util.Random;
import java.util.Stack;
import java.util.ArrayList;

public class Player 
{
	private int hp = 20;
	private ArrayList<Card> cardOnTable;
    private ArrayList<Card> cardsInHand;

    private Stack<Card> stack;
	
	public Player() {
        cardOnTable = new ArrayList<>(Constants.NUMBER_OF_CARDS_ON_TABLE);
        cardsInHand = new ArrayList<>(20);
        stack = new Stack<>();
    }

	public void dealDmgToChamp(int dmg)
	{
		hp -= dmg;
	}

	public boolean isChampDestroyed() {
        return hp <= 0;
    }

	public boolean dealDmgToCard(int indexOfCard, int dmg)
	{
	    Card card = cardOnTable.get(indexOfCard);
	    card.dealDmg(dmg);
        if (card.isCardDestroyed()) {
            cardOnTable.remove(indexOfCard);
            return false;
        }
        return true;
	}

    public void destroyRandomCardOnTable() {
        if(!cardOnTable.isEmpty()) {
            Random random = new Random();
            int randomNumber = random.nextInt(cardOnTable.size());
            cardOnTable.remove(randomNumber);
        }
    }
	
	public void updateCardsAttack()
	{
	    for(Card card: cardOnTable) {
	        card.enableAttack();
        }
	}
	
	public void putCardOnTable(Card card)
	{
		cardOnTable.add(card);
	}
	
	void randomCard()
	{
	    cardsInHand.add(stack.pop());
	}
	
	public void createRandomDeck()
	{
		int[] cost={10,8,6,5,3,2,2,2,2,3};
		int[] attack={11,6,6,5,3,2,2,0,0,0};
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
					stack.add(new Card(cost[nextCard],attack[nextCard],life[nextCard],numberOfMagic[nextCard]));
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

	public int getHealth() {
	    return hp;
    }

    public void addToHealth(int a) {
	    hp += a;
    }

    public Card getCardOnTable(int index){
        return cardOnTable.get(index);
    }

    public Card getCardInHand(int index) throws Exception {
        return cardsInHand.get(index);
    }

    public void destroyCardInHand(int index) {
	    cardsInHand.remove(index);
    }

    public int getNumberOfCardsInStack() {
	    return stack.size();
    }
}