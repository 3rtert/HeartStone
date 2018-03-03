import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Game 
{
	Player[] player=new Player[2];
	
	int currentPlayer=0; //0 - 1
	int enemy=1; //1 - 0
	
	
	int round=0;
	int leftMana;
	int playerWin=-1;
	
	protected Game clone()
	{
		Game newGame=new Game();
		newGame.player[0]=player[0].clone();
		newGame.player[1]=player[1].clone();
		newGame.currentPlayer=currentPlayer;
		newGame.enemy=enemy;
		newGame.round=round;
		newGame.leftMana=leftMana;
		
		return newGame;
	}
	
	void init()
	{
		player[0]=new Player();
		player[1]=new Player();
		
		player[0].randomCard();
		player[0].randomCard();
		
		player[1].randomCard();
		player[1].randomCard();
		player[1].randomCard();
		
		currentPlayer=0;
		
		GUIInterface gui = new GUI();
        gui.setNumberOfCardsInDeck(PlayerNumber.ONE, 18);
        gui.setNumberOfCardsInDeck(PlayerNumber.TWO, 17);
        gui.setAmountOfMana(PlayerNumber.ONE, 1);
        gui.setAmountOfMana(PlayerNumber.TWO, 1);
        gui.setNumberOfLifePoints(PlayerNumber.ONE, 20);
        gui.setNumberOfLifePoints(PlayerNumber.TWO, 20);
        
        gui.addCardToPlayersHand(PlayerNumber.ONE,2,1,10,10,10,1);
        gui.addCardToPlayersHand(PlayerNumber.TWO,1,2,100,100,100,100);
	}
	
	void start() throws IOException
	{
		while(playerWin==-1)
			move();
		System.out.println("Wygral gracz numer: "+playerWin);
	}
	
	void move() throws IOException
	{
		if(currentPlayer==1)
			nextRound();
		updateMana();
		getCard();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		player[currentPlayer].updateCardsAttak();
		System.out.println("Podaj nastepny ruch: ");
		// u1 - use card 1
		// a1/2 - attak card 2 by card 1
		// h1 - attak enemy by card 1
		// e - end tour
		
		char[] action=in.readLine().toCharArray();
		String textOfCard="";
		for(int i=1;i<action.length;i++)
		{
			textOfCard+=action[i];
		}
		
		String[] textOfCards=textOfCard.split("/");
		int[] numberOfCard= new int[textOfCard.length()];
		
		if(!textOfCards[0].equals(""))
		{
			for(int i=0;i<textOfCards.length;i++)
			{
				numberOfCard[i]=Integer.parseInt(textOfCards[i]);
			}
		}
		if(action[0]=='u')
		{
			useCard(numberOfCard[0]);
		}
		else if(action[0]=='a')
		{
			attak(numberOfCard[0],numberOfCard[1]);
		}
		else if(action[0]=='h')
		{
			attak(numberOfCard[0],-1);
		}
		else if(action[0]=='e')
		{
			endTour();
		}
		if(player[enemy].hp<=0)
			playerWin=currentPlayer;
	}
	
	boolean attak(int numberOfMyCard, int numberOfEnemyCard) // -1 - enemy champion
	{
		boolean done=false;
		
		if(player[currentPlayer].cardCanAttak(numberOfMyCard) && (numberOfEnemyCard==-1 || player[enemy].cardCanBeAttaked(numberOfEnemyCard)))
		{
			if(numberOfEnemyCard==-1)
			{
				dealDmgToChamp(enemy, player[currentPlayer].cardOnTable[numberOfMyCard].attak);
			}
			else
			{
				player[currentPlayer].cardAttak(numberOfMyCard);
				player[enemy].dealDmgToCard(numberOfEnemyCard, player[currentPlayer].cardOnTable[numberOfMyCard].attak);
				if(player[enemy].cardOnTable[numberOfEnemyCard].life>0)
					player[currentPlayer].dealDmgToCard(numberOfMyCard, player[enemy].cardOnTable[numberOfEnemyCard].attak);
			}
		}
		return done;
	}
	
	void useMagic(int numberOfMagic)
	{
		switch(numberOfMagic)
		{
		case 0:
			dealDmgToChamp(enemy,1);
			break;
		case 1:
			player[currentPlayer].hp+=2;
			break;
		case 2:
			for(int i=0;i<7;i++)
				player[enemy].dealDmgToCard(i, 2);
			break;
		case 3:
			dealDmgToChamp(enemy, 3);
			break;
		case 4:
			boolean exist=false;
			for(int i=0;i<7&&!exist;i++)
			{
				if(player[enemy].cardOnTable[i]!=null)
					exist=true;
			}
			if(exist)
			{
				Random random = new Random();
				random.nextInt(7);
				boolean done=false;
				for(int i=0;i<7&&!done;i++)
				{
					if(player[enemy].cardOnTable[i]!=null)
					{
						player[enemy].cardOnTable[i]=null;
						done=true;
					}
				}
			}
			break;
		}
	}
	
	boolean useCard(int numberOfCard)
	{
		boolean done=false;
		if(done = player[currentPlayer].cardsInHand[numberOfCard]!=null && leftMana>=player[currentPlayer].cardsInHand[numberOfCard].cost)
		{
			Card card=player[currentPlayer].cardsInHand[numberOfCard];
			if(card.numberOfMagic!=-1)
				useMagic(card.numberOfMagic);
			player[currentPlayer].cardsInHand[numberOfCard]=null;
		}
		return done;
	}
	
	void getCard()
	{
		if(player[currentPlayer].cardsOnStack()>0)
			player[currentPlayer].randomCard();
		else
			dealDmgToChamp(currentPlayer,1);
	}
	void updateMana()
	{
		leftMana=Math.min(round, 10);
	}
	void nextRound()
	{
		round+=1;
	}
	void endTour()
	{
		currentPlayer=currentPlayer==0?1:0;
		enemy=enemy==0?1:0;
	}
	void dealDmgToChamp(int indexOfPLayer, int dmg)
	{
		if(player[indexOfPLayer].dealDmgToChamp(dmg))
			playerWin=indexOfPLayer==0?1:0;
	}
	
}
