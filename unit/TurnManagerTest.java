package unit;

import data.cards.Card;
import data.game.DiscardPile;
import data.game.GameTable;
import data.players.Hand;
import data.players.HumanPlayer;
import data.players.Player;
import engine.GameEngine;
import engine.TurnManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class TurnManagerTest {

    private TurnManager turnManager;
    private GameTable gameTable;
    private GameEngine gameEngine;

    @BeforeEach
    public void setUp() {
        List<Player> players = new ArrayList<>();
        players.add(new HumanPlayer("Nadjib",new Hand(),0));

        gameTable = new GameTable();
        gameTable.setDiscardPile(new DiscardPile());

        gameEngine = new GameEngine(); // ou un mock si nécessaire

        turnManager = new TurnManager(players, gameTable, gameEngine);
    }

    private Card createCard(int number, boolean isJoker) {
        if(!isJoker) {
            return new Card(number, "c", "src/images/" + number + "c.gif"); // adapter selon ton constructeur
        }else{
            return new Card(number, "joker1", "src/images/joker1.jpeg");
        }
    }

    @Test
    public void testNullOrEmptySelection() {
        assertFalse(turnManager.isValidMove(null));
        assertFalse(turnManager.isValidMove(new ArrayList<>()));
    }

    @Test
    public void testSingleJokerNotValid() {
        ArrayList<Card> selected = new ArrayList<>();
        selected.add(createCard(0, true));
        assertFalse(turnManager.isValidMove(selected));
    }

    @Test
    public void testDoubleJokerIsValid() {
        ArrayList<Card> selected = new ArrayList<>();
        selected.add(createCard(0, true));
        selected.add(createCard(0, true));
        assertTrue(turnManager.isValidMove(selected));
    }

    @Test
    public void testMultipleDeuxInvalid() {
        ArrayList<Card> selected = new ArrayList<>();
        selected.add(createCard(20, false));
        selected.add(createCard(20, false));
        assertFalse(turnManager.isValidMove(selected));
    }

    @Test
    public void testFirstTurnValidSimpleCard() {
        ArrayList<Card> selected = new ArrayList<>();
        selected.add(createCard(7, false));

        gameTable.getDiscardPile().setLastCombinaison(new ArrayList<>()); // vide = premier tour

        assertTrue(turnManager.isValidMove(selected));
    }

    @Test
    public void testPlayCard20OnNonBomb() {
        ArrayList<Card> last = new ArrayList<>();
        last.add(createCard(5, false));
        gameTable.getDiscardPile().setLastCombinaison(last);

        ArrayList<Card> selected = new ArrayList<>();
        selected.add(createCard(20, false)); // carte spéciale
        assertTrue(turnManager.isValidMove(selected));
    }

    @Test
    public void testSingleCardFollowUpValid() {
        ArrayList<Card> last = new ArrayList<>();
        last.add(createCard(6, false));
        gameTable.getDiscardPile().setLastCombinaison(last);

        ArrayList<Card> selected = new ArrayList<>();
        selected.add(createCard(7, false));
        assertTrue(turnManager.isValidMove(selected));
    }

    @Test
    public void testSingleCardFollowUpInvalid() {
        ArrayList<Card> last = new ArrayList<>();
        last.add(createCard(6, false));
        gameTable.getDiscardPile().setLastCombinaison(last);

        ArrayList<Card> selected = new ArrayList<>();
        selected.add(createCard(9, false));
        assertFalse(turnManager.isValidMove(selected));
    }

    @Test
    public void testBombBeatsPreviousBomb() {
        // Bombe précédente : 3x 5
        ArrayList<Card> last = new ArrayList<>();
        last.add(createCard(5, false));
        last.add(createCard(5, false));
        last.add(createCard(5, false));
        gameTable.getDiscardPile().setLastCombinaison(last);

        // Bombe actuelle : 3x 6 -> plus forte
        ArrayList<Card> selected = new ArrayList<>();
        selected.add(createCard(6, false));
        selected.add(createCard(6, false));
        selected.add(createCard(6, false));

        assertTrue(turnManager.isValidMove(selected));
    }

    @Test
    public void testDoubleFollowsPreviousDouble() {
        // Double précédent : 2x 7
        ArrayList<Card> last = new ArrayList<>();
        last.add(createCard(7, false));
        last.add(createCard(7, false));
        gameTable.getDiscardPile().setLastCombinaison(last);

        // Double suivant : 2x 8
        ArrayList<Card> selected = new ArrayList<>();
        selected.add(createCard(8, false));
        selected.add(createCard(8, false));

        assertTrue(turnManager.isValidMove(selected));
    }

    @Test
    public void testSerieFollowsPreviousSerie() {
        // Série précédente : 4, 5, 6
        ArrayList<Card> last = new ArrayList<>();
        last.add(createCard(4, false));
        last.add(createCard(5, false));
        last.add(createCard(6, false));
        gameTable.getDiscardPile().setLastCombinaison(last);

        // Nouvelle série qui commence après : 5, 6, 7
        ArrayList<Card> selected = new ArrayList<>();
        selected.add(createCard(5, false));
        selected.add(createCard(6, false));
        selected.add(createCard(7, false));

        assertTrue(turnManager.isValidMove(selected));
    }


}
