import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.border.Border;
import java.util.Map;
import java.util.HashMap;

public class GUI extends JFrame implements GUIInterface {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;

    Map<PlayerNumber, PlayerGUIStatFields> players = new HashMap<>();

    public GUI() {
        super("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocation(50,50);
        setLayout(new GridLayout(4, 7));


        PlayerGUIStatFields player1Stats = new PlayerGUIStatFields();
        player1Stats.setPlayerName("G1");
        addFieldsToGrid(player1Stats.getFields());
        addFieldsToGrid(player1Stats.getBattleFields());
        players.put(PlayerNumber.ONE, player1Stats);

        PlayerGUIStatFields player2Stats = new PlayerGUIStatFields();
        addFieldsToGrid(player2Stats.getBattleFields());
        addFieldsToGrid(player2Stats.getFields());
        player2Stats.setPlayerName("G2");
        players.put(PlayerNumber.TWO, player2Stats);

        setVisible(true);
    }

    private void addFieldsToGrid(ArrayList<JLabel> fields) {
        for (JLabel field : fields ) {
            Border border = BorderFactory.createLineBorder(Color.BLACK);
            field.setBorder(border);
            field.setHorizontalAlignment(JLabel.CENTER);
            field.setVerticalAlignment(JLabel.CENTER);
            add(field, SwingConstants.CENTER);
        }
    }

    @Override
    public void setNumberOfCardsInDeck(PlayerNumber player, int number) {
        players.get(player).setNrOfCardsInDeck(number);
    }

    @Override
    public void setAmountOfMana(PlayerNumber player, int amount) {
        players.get(player).setAmountOfMana(amount);
    }

    @Override
    public void addCardToPlayersHand(PlayerNumber player, int index, int cardNumber, int dmg, int life, int manaExpense, int magicNumber) {
        players.get(player).addCardToHand(index, cardNumber, dmg, life, manaExpense, magicNumber);
    }

    @Override
    public void setNumberOfLifePoints(PlayerNumber player, int number) {
        players.get(player).setNumberOfLifePoints(number);
    }

    @Override
    public void addCardToBattleField(PlayerNumber player, int index, int cardNumber, int dmg, int life, int manaExpense, int magicNumber) {
        players.get(player).addCardToBattleField(index, cardNumber, dmg, life, manaExpense, magicNumber);
    }
}