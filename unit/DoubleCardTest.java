package unit;
import static org.junit.jupiter.api.Assertions.*;

import data.cards.Card;
import data.cards.DoubleCard;
import data.players.Hand;
import engine.CombinaisonProcess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class DoubleCardTest {

    private Hand hand;
    private CombinaisonProcess cp;

    @BeforeEach
    void setUp() {
        // Crée une main de cartes pour les tests
        hand = new Hand();
        hand.addCard(new Card(3, "h","src/images/3h.gif"));
        hand.addCard(new Card(3, "d","src/images/3d.gif"));
        hand.addCard(new Card(4, "s","src/images/4s.gif"));
        hand.addCard(new Card(5, "c","src/images/5c.gif"));
        hand.addCard(new Card(0, "joker1","src/images/joker1.jpeg"));// Joker
        cp=new CombinaisonProcess(hand);
    }

    @Test
    void testHasDouble() {
        // Cas où un double est présent (3-3)
        assertTrue(cp.hasDouble(), "La main devrait contenir un double.");

        //cas double avec joker
        hand.removeCard(new Card(3, "d","src/images/3d.gif"));
        assertTrue(cp.hasDouble(), "double avec le joker");
    }

    @Test
    void testGetNextDouble() {
        // Un double existant (3, 3) est joué
        Card card1 = new Card(3, "h","src/images/3h.gif");
        Card card2 = new Card(3, "s","src/images/3s.gif");

        ArrayList doubleC = new ArrayList<>();
        doubleC.add(card1);
        doubleC.add(card2);
        DoubleCard lastDouble = new DoubleCard(doubleC);

        // Vérifier le double suivant (4, joker)
        DoubleCard nextDouble = cp.getNextDouble(lastDouble);
        assertNotNull(nextDouble, "Le double suivant devrait être trouvé.");
        assertEquals(4, nextDouble.getValue(), "Le double suivant doit être 4.");
    }

    @Test
    void testGetNextDoubleWithJoker() {
        // Vérifier le double suivant avec un joker (4, joker)

        Card card1 = new Card(3, "h","src/images/3h.gif");
        Card card2 = new Card(3, "d","src/images/3d.gif");
        ArrayList doubleC = new ArrayList<>();
        doubleC.add(card1);
        doubleC.add(card2);

        DoubleCard lastDouble = new DoubleCard(doubleC);
        DoubleCard nextDouble = cp.getNextDouble(lastDouble);
        assertNotNull(nextDouble, "Le double suivant avec joker devrait être trouvé.");
        assertEquals(4, nextDouble.getValue(), "Le double suivant doit être 4 avec joker.");
    }

    @Test
    void testGetBestDouble() {
        // Le meilleur double est le 5-5(joker)
        DoubleCard bestDouble = cp.getBestDouble();
        assertNotNull(bestDouble, "Le meilleur double devrait être trouvé.");
        assertEquals(5, bestDouble.getValue(), "Le meilleur double doit être 5 et joker.");
    }


    @Test
    void testGetLowestDouble() {
        // Le plus petit double est 3, 3
        DoubleCard lowestDouble = cp.getLowestDouble();
        assertNotNull(lowestDouble, "Le plus petit double devrait être trouvé.");
        assertEquals(3, lowestDouble.getValue(), "Le plus petit double doit être 3.");
    }

    @Test
    void testGetLowestDoubleWithJoker() {
        // Cas où le plus bas double est avec un joker (3, joker)
        hand.addCard(new Card(0, "joker1","src/images/joker1.jpeg"));
        DoubleCard lowestDouble = cp.getLowestDouble();
        assertNotNull(lowestDouble, "Le plus petit double avec joker devrait être trouvé.");
        assertEquals(3, lowestDouble.getValue(), "Le plus petit double avec joker doit être 3.");
    }
}
