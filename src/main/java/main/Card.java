package main;

public class Card {
    private int cost;
    private int attack;
    private int life;

    // card with effect
    //-1 if magic spell not exist
    // 0 - deal 1 damage
    // 1 - restore 2 health

    // spells
    // 2 - deal 2 damage to all enemies
    // 3 - deal 3 damage
    // 4 - destroy random enemy minion
    private int numberOfMagic;
    private boolean magicCard;
    private boolean attackPossible;

    public Card(int cost, int attack, int life, int numberOfMagic, boolean magicCard, boolean attackPossible) {
        this.cost = cost;
        this.attack = attack;
        this.life = life;
        this.numberOfMagic = numberOfMagic;
        this.magicCard = magicCard;
        this.attackPossible = attackPossible;
    }

    protected Card clone() {
        return new Card(cost, attack, life, numberOfMagic, magicCard, attackPossible);
    }

    public boolean canCardAttack() {
        return attackPossible;
    }

    public void attack() {
        if (attackPossible) {
            attackPossible = false;
        }
    }

    public void dealDmg(int dmg) {
        life -= dmg;
    }

    public boolean isCardDestroyed() {
        return life <= 0;
    }

    public int getAttack() {
        return attack;
    }

    public int getManaCost() {
        return cost;
    }

    public int getNumberOfMagic() {
        return numberOfMagic;
    }

    public void enableAttack() {
        attackPossible = true;
    }

    public int getLife() {
        return life;
    }

    public boolean isMagicCard() {
        return magicCard;
    }

    @Override
    public String toString() {
        String output = "<html>";
        if(magicCard) {
            output += ("Mana: " + cost + ", ");
        } else {
            output += ("Attack: " + attack + ", Life: " + life + ", Mana: " + cost);
        }
        if(numberOfMagic != -1) {
            output += ("<br>" + getStringMagicEffect());
        }
        output += "</html>";

        return output;
    }

    private String getStringMagicEffect() {
        switch(numberOfMagic) {
            case 0:
                return "Zadaj przeciwnemu bohaterowi 1 obrażeń";
            case 1:
                return "Dodaj swojemu bohaterowi 2 punkty życia";
            case 2:
                return "Zadaj 2 obrażeń wszystkim kartom przeciwnika na planszy";
            case 3:
                return "Zadaj przeciwnemu bohaterowi 3 obrażeń";
            case 4:
                return "Zniszcz losową jednostkę przeciwnika";
            default:
                return "Nie zdefiniowano";
        }
    }
}
