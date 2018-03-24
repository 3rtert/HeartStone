package main;

import java.util.Random;
import java.util.Stack;
import java.util.ArrayList;

public class Player {

    private int hp = Constants.PLAYER_HP;
    private int mana = 0;
    private int penaltyForEmptyDeck = 1;

    private Card[] cardsOnTable = new Card[Constants.NUMBER_OF_CARDS_ON_TABLE];
    private int numberOfCardsOnTable = 0;

    private Card[] cardsInHand = new Card[Constants.MAXIMUM_NUMBER_OF_CARDS_IN_HAND];
    private int numberOfCardsInHand = 0;

    private ArrayList<Card> magicCardsUsed = new ArrayList<>();

    private Stack<Card> stack = new Stack<>();

    public void init(boolean starting) {
        createRandomDeck();//createTestDeck();

        getCardFromDeck();
        getCardFromDeck();
        getCardFromDeck();

        if (!starting) {
            getCardFromDeck();
        }
    }

    protected Player clone() {

        Player newPlayer = new Player();

        newPlayer.hp = hp;
        newPlayer.mana = mana;
        newPlayer.penaltyForEmptyDeck = penaltyForEmptyDeck;

        newPlayer.cardsOnTable = cardsOnTable.clone();

        newPlayer.numberOfCardsInHand = numberOfCardsInHand;
        newPlayer.numberOfCardsOnTable = numberOfCardsOnTable;

        newPlayer.cardsInHand = cardsInHand.clone();

        for (int i = 0; i < stack.size(); i++) {
            newPlayer.stack.add(stack.get(i).clone());
        }

        for (int i = 0; i < magicCardsUsed.size(); i++) {
            newPlayer.magicCardsUsed.add(magicCardsUsed.get(i).clone());
        }
        return newPlayer;
    }

    public void useMagicCard(int index) {
        magicCardsUsed.add(cardsInHand[index]);
        destroyCardInHand(index);
    }

    public void clearAllMagicCards() {
        magicCardsUsed.clear();
    }

    public ArrayList<Card> getCardsOnTableCopy() {
        ArrayList<Card> cards = new ArrayList<>();
        for (int ind = 0; ind < cardsOnTable.length; ind++) {
           if(cardsOnTable[ind] != null) {
               cards.add(cardsOnTable[ind]);
           }
        }
        return cards;
    }

    public void dealDmgToChamp(int dmg) {
        hp -= dmg;
    }

    public boolean isChampDestroyed() {
        return hp <= 0;
    }

    public void dealDmgToCard(int indexOfCard, int dmg) {
        cardsOnTable[indexOfCard].dealDmg(dmg);
        if (cardsOnTable[indexOfCard].getLife() <= 0) {
            destroyCardOnTable(indexOfCard);
        }
    }

    public void dealDmgToAllCards(int dmg) {
        for (int i = 0; i < cardsOnTable.length; i++) {
            if(cardsOnTable[i] != null) {
                dealDmgToCard(i, dmg);
            }
        }
    }

    public void destroyRandomCardOnTable() {
        if (numberOfCardsOnTable != 0) {
            Random random = new Random();
            int randomNumber = random.nextInt(numberOfCardsOnTable);
            for(int i = 0; i < cardsOnTable.length; i++) {

                if(cardsOnTable[i] != null) {
                    randomNumber--;
                }
                if(randomNumber < 0) {
                    cardsOnTable[i] = null;
                    break;
                }
            }
            numberOfCardsOnTable--;
        }
    }

    public void updateCardsAttack() {
        for (Card card : cardsOnTable) {
            if(card != null) {
                card.enableAttack();
            }
        }
    }

    public void putCardOnTable(int index) {
        try {
            Card card = getCardInHand(index);
            numberOfCardsOnTable++;
            for(int i = 0; i<cardsOnTable.length; i++) {
                if(cardsOnTable[i] == null) {
                    cardsOnTable[i] = card;
                    break;
                }
            }

            destroyCardInHand(index);

        } catch (Exception e) {
            System.out.println("put card on table problem" + e.getMessage());
        }
    }

    public void getCardFromDeck() {
        addCardToHand(stack.pop());
    }

    public void addCardToHand(Card card) {
        if(numberOfCardsInHand < 20) {
            numberOfCardsInHand++;
            for(int i = 0; i<cardsInHand.length; i++) {
                if(cardsInHand[i] == null) {
                    cardsInHand[i] = card;
                    break;
                }
            }
        } else {
            System.out.println("To many cards in hand");
        }
    }

    public Card getRandomCard() {
        return null;
    }

    public Card getRandomCardFromDeck()
    {
    	Random generator = new Random();
    	Card card = stack.get(generator.nextInt(stack.size()));
    	addCardToHand(card);
    	return card;
    }
    
    //change to collection.shuffle??
    public void createRandomDeck() {
        int[] cost = {10, 8, 6, 5, 3, 2, 2, 2, 2, 3};
        int[] attack = {11, 6, 6, 5, 3, 2, 2, 0, 0, 0};
        int[] life = {11, 4, 6, 5, 4, 1, 2, 0, 0, 0};

        // 0 - deal 1 damage to champ
        // 1 - restore 2 health

        // 2 - deal 2 damage to all enemies on board
        // 3 - deal 3 damage to champ
        // 4 - destroy random enemy minion

        int[] numberOfMagic = {-1, -1, -1, -1, -1, 0, 1, 2, 3, 4};
        boolean[] magicCards = {false, false, false, false, false, false, false, true, true, true};
        int[] cards = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            int nextCard = random.nextInt(10);
            boolean done = false;
            while (!done) {
                if (cards[nextCard] > 0) {
                    cards[nextCard] -= 1;
                    stack.add(new Card(cost[nextCard], attack[nextCard], life[nextCard], numberOfMagic[nextCard], magicCards[nextCard], true));
                    done = true;
                } else {
                    nextCard++;
                    if (nextCard == 10)
                        nextCard = 0;
                }
            }
        }
    }

    public void createTestDeck() {
        //int[] cost = {10, 8, 6, 5, 3, 2, 2, 2, 2, 3};
        //int[] attack = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] life = {2, 1, 2, 2, 1, 1, 1, 2, 1, 1};

        int[] numberOfMagic = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        boolean[] magicCards = {false, false, false, false, false, false, false, false, false, false};
        int[] cards = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
        Random random = new Random();
        for (int i = 0; i < 20; i++) {

            stack.add(new Card(1, 1, life[i % 10], -1, false, true));

        }
    }

    public int getHealth() {
        return hp;
    }

    public void addToHealth(int a) {
        hp += a;
    }

    public Card getCardOnTable(int index) {
        return cardsOnTable[index];
    }

    public Card getCardInHand(int index) throws Exception {
        if(cardsInHand[index] != null) {
            return cardsInHand[index];
        } else {
            throw new Exception("No card with this index");
        }

    }

    public void destroyCardInHand(int index) {
        cardsInHand[index] = null;
        numberOfCardsInHand--;
    }

    private void destroyCardOnTable(int index) {
        cardsOnTable[index] = null;
        numberOfCardsOnTable--;
    }
    public int getNumberOfCardsInStack() {
        return stack.size();
    }

    public int getNumberOfCardsInHand() {
        return numberOfCardsInHand;
    }

    public int getNumberOfCardsOnTable() {
        return numberOfCardsOnTable;
    }

    //for testing
    public void addCardToDeck(Card card) {
        stack.add(card);
    }

    public void setMana(int m) {
        mana = m;
    }

    public int getMana() {
        return mana;
    }

    public Card[] getCardsOnTable() {
        return cardsOnTable;
    }

    public Card[] getCardsInHand() {
        return cardsInHand;
    }

    void getCard() {
        if (getNumberOfCardsInStack() > 0) {
            getCardFromDeck();
        } else {
            dealDmgToChamp(penaltyForEmptyDeck++);
        }
    }

    public ArrayList<Card> getMagicCardsUsed() {
        return magicCardsUsed;
    }

    public void updateMana(int round) {
        mana = Math.min(round, 10);
    }
}
