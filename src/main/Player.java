package main;

import java.util.Random;
import java.util.Stack;
import java.util.ArrayList;

public class Player {
    private int hp = 20;
    private int mana;
    private ArrayList<Card> cardOnTable;
    private ArrayList<Card> cardsInHand;

    private Stack<Card> stack;

    public Player() {
        cardOnTable = new ArrayList<>(Constants.NUMBER_OF_CARDS_ON_TABLE);
        cardsInHand = new ArrayList<>(20);
        stack = new Stack<>();
    }

    protected Player clone() {
        /*Player newPlayer=new Player();
        newPlayer.hp=hp;
        for(int i=0;i<cardOnTable.size();i++)
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

        return newPlayer;*/
        return null;
    }

    public void dealDmgToChamp(int dmg) {
        hp -= dmg;
    }

    public boolean isChampDestroyed() {
        return hp <= 0;
    }

    public boolean dealDmgToCard(int indexOfCard, int dmg) {
        Card card = cardOnTable.get(indexOfCard);
        card.dealDmg(dmg);
        if (card.isCardDestroyed()) {
            cardOnTable.remove(indexOfCard);
            return false;
        }
        return true;
    }

    public void dealDmgToAllCards(int dmg) {
        for(int i = 0; i < cardOnTable.size(); i++) {
            dealDmgToCard(i, dmg);
        }
    }

    public void destroyRandomCardOnTable() {
        if (!cardOnTable.isEmpty()) {
            Random random = new Random();
            int randomNumber = random.nextInt(cardOnTable.size());
            cardOnTable.remove(randomNumber);
        }
    }

    public void updateCardsAttack() {
        for (Card card : cardOnTable) {
            card.enableAttack();
        }
    }

    public void putCardOnTable(int index) {
        try {
            Card card = getCardInHand(index);
            cardOnTable.add(card);
            cardsInHand.remove(index);
        } catch(Exception e) {
            System.out.println("put card on table problem" + e.getMessage());
        }
    }

    public void getCardFromDeck() {
        cardsInHand.add(stack.pop());
    }

    //change to collection.shuffle??
    public void createRandomDeck() {
        int[] cost = {10, 8, 6, 5, 3, 2, 2, 2, 2, 3};
        int[] attack = {11, 6, 6, 5, 3, 2, 2, 0, 0, 0};
        int[] life = {11, 4, 6, 5, 4, 1, 2, 0, 0, 0};

        // 0 - deal 1 damage
        // 1 - restore 2 health

        // 2 - deal 2 damage to all enemies
        // 3 - deal 3 damage
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

    public int getHealth() {
        return hp;
    }

    public void addToHealth(int a) {
        hp += a;
    }

    public Card getCardOnTable(int index) {
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

    public int getNumberOfCardsInHand() {
        return cardsInHand.size();
    }

    public int getNumberOfCardsOnTable() {
        return cardOnTable.size();
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
}
