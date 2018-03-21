package main;

import java.util.Random;
import java.util.Stack;
import java.util.ArrayList;

public class Player {
    private int hp = 20;
    private int mana = 0;
    private int penaltyForEmptyDeck = 1;

    private ArrayList<Card> cardOnTable = new ArrayList<>(Constants.NUMBER_OF_CARDS_ON_TABLE);
    private ArrayList<Card> cardsInHand = new ArrayList<>(20);

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
        for (int i = 0; i < cardOnTable.size(); i++) {
            newPlayer.cardOnTable.add(cardOnTable.get(i).clone());
        }
        for (int i = 0; i < cardsInHand.size(); i++) {
            newPlayer.cardsInHand.add(cardsInHand.get(i).clone());
        }
        for (int i = 0; i < stack.size(); i++) {
            newPlayer.stack.add(stack.get(i).clone());
        }

        return newPlayer;
    }

    public ArrayList<Card> getCardsOnTableCopy() {
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < cardOnTable.size(); i++) {
            cards.add(cardOnTable.get(i).clone());
        }
        return cards;
    }

    public void dealDmgToChamp(int dmg) {
        hp -= dmg;
    }

    public boolean isChampDestroyed() {
        return hp <= 0;
    }

    public boolean dealDmgToCard(int indexOfCard, int dmg) {
        Card card = cardOnTable.get(indexOfCard);
        if (card.getLife() > 0) {
            card.dealDmg(dmg);
            return true;
        }
        return false;
    }

    public void clearBoard() {
        for (Card card : cardOnTable) {
            if (card.isCardDestroyed()) {
                cardOnTable.remove(card);
            }
        }
    }

    public void dealDmgToAllCards(int dmg) {
        for (int i = 0; i < cardOnTable.size(); i++) {
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
        } catch (Exception e) {
            System.out.println("put card on table problem" + e.getMessage());
        }
    }

    public void getCardFromDeck() {
        cardsInHand.add(stack.pop());
    }

    public Card getRadomCardFromDeck()
    {
    	Random generator = new Random();
    	return cardsInHand.add(stack.get(generator.nextInt(stack.size())));
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

    public ArrayList<Card> getCardOnTable() {
        return cardOnTable;
    }

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    void getCard() {
        if (getNumberOfCardsInStack() > 0) {
            getCardFromDeck();
        } else {
            dealDmgToChamp(penaltyForEmptyDeck++);
        }
    }

    public void updateMana(int round) {
        mana = Math.min(round, 10);
    }
}
