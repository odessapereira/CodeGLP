package unit;

import data.cards.*;
import data.players.Hand;
import engine.CombinaisonProcess;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CombinaisonTest {

    private Hand hand ;
    private CombinaisonProcess cp;

    @Before
    public void setup() {
        hand = new Hand();
    }

    @Test
    public void testBombIsValidAndValue() {
        ArrayList<Card> cards = new ArrayList<>(Arrays.asList(
                new Card(7, "s", "src/images/7s.gif"),
                new Card(7, "h", "src/images/7h.gif"),
                new Card(7, "d", "src/images/7d.gif"),
                new Card(7, "c", "src/images/7c.gif")
        ));
        Combinaison bomb = new Bomb(cards);
        assertTrue(bomb.isValid());
        assertEquals(7, bomb.getValue());
    }

    @Test
    public void testDoubleIsValidAndValue() {
        ArrayList<Card> cards = new ArrayList<>(Arrays.asList(
                new Card(3, "c", "src/images/3c.gif"),
                new Card(3, "h", "src/images/3h.gif")
        ));
        Combinaison doubleComb = new DoubleCard(cards);
        assertTrue(doubleComb.isValid());
        assertEquals(3, doubleComb.getValue());
    }

    @Test
    public void testSerieIsValidAndValue() {
        ArrayList<Card> cards = new ArrayList<>(Arrays.asList(
                new Card(4, "c", "src/images/4c.gif"),
                new Card(5, "h", "src/images/5h.gif"),
                new Card(6, "c", "src/images/6c.gif")
        ));
        Combinaison serie = new Serie(cards);
        assertTrue(serie.isValid());
        assertEquals(5, serie.getValue()); //getValue return the second number of a serie
    }

    @Test
    public void testEmptyCombinationIsInvalid() {
        Combinaison emptyBomb = new Bomb(new ArrayList<>());
        assertFalse(emptyBomb.isValid());
    }

    @Test
    public void testHasDoubleJokerTrue() {
        hand.addCard(new Card(0, "joker1","src/images/joker1.jpeg"));
        hand.addCard(new Card(0, "joker2","src/images/joker2.jpeg"));
        cp=new CombinaisonProcess(hand);
        assertTrue(cp.hasDoubleJoker());
    }

    @Test
    public void testHasDoubleJokerFalse() {
        hand.addCard(new Card(0, "joker1","src/images/joker1.jpeg"));
        cp=new CombinaisonProcess(hand);
        assertFalse(cp.hasDoubleJoker());
    }

    @Test
    public void testGetDoubleJoker() {
        hand.addCard(new Card(0, "joker1","src/images/joker1.jpeg"));
        hand.addCard(new Card(0, "joker2","src/images/joker1.jpeg"));
        cp=new CombinaisonProcess(hand);

        ArrayList<Card> jokers = cp.getDoubleJoker();
        assertEquals(2, jokers.size());
        assertTrue(jokers.get(0).isJoker() && jokers.get(1).isJoker());
    }

    @Test
    public void testIsDoubleJokerTrue() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(0, "joker1","src/images/joker1.jpeg"));
        cards.add(new Card(0, "joker2","src/images/joker2.jpeg"));
        cp=new CombinaisonProcess(hand);
        assertTrue(cp.isDoubleJoker(cards));
    }

    @Test
    public void testIsDoubleJokerFalse() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(0, "joker1","src/images/joker1.jpeg"));
        cards.add(new Card(3, "h","src/images/3h.gif"));
        cp=new CombinaisonProcess(hand);
        assertFalse(cp.isDoubleJoker(cards));
    }

    @Test
    public void testGetHighestCard() {
        hand.addCard(new Card(3, "h","src/images/3h.gif"));
        hand.addCard(new Card(7, "s","src/images/7s.gif"));
        hand.addCard(new Card(0, "joker1","src/images/joker1.jpeg"));
        hand.addCard(new Card(10, "c","src/images/10c.gif"));
        cp=new CombinaisonProcess(hand);
        Card highest = cp.getHighestCard();
        assertEquals(10, highest.getNumber());
    }

    @Test
    public void testGetNextHigherCardExists() {
        hand.addCard(new Card(5, "h","src/images/5h.gif"));
        hand.addCard(new Card(6, "c","src/images/6c.gif"));
        hand.addCard(new Card(0, "joker1","src/images/joker1.jpeg"));
        Card last = new Card(5, "d","src/images/5d.gif");
        cp=new CombinaisonProcess(hand);
        Card next = cp.getNextHigherCard(last);
        assertNotNull(next);
        assertEquals(6, next.getNumber());
    }

    @Test
    public void testGetNextHigherCardNotExists() {
        hand.addCard(new Card(3, "h","src/images/3h.gif"));
        cp=new CombinaisonProcess(hand);
        Card next = cp.getNextHigherCard(new Card(10, "c","src/images/10c.gif"));
        assertNull(next);
    }

    @Test
    public void testHasSpecialCardTrue() {
        hand.addCard(new Card(20, "s","src/images/20s.gif"));
        cp=new CombinaisonProcess(hand);
        assertTrue(cp.hasSpecialCard());
    }

    @Test
    public void testHasSpecialCardFalse() {
        hand.addCard(new Card(5, "h","src/images/5h.gif"));
        cp=new CombinaisonProcess(hand);
        assertFalse(cp.hasSpecialCard());
    }

    @Test
    public void testGetSpecialCard() {
        hand.addCard(new Card(20, "s","src/images/20s.gif"));
        hand.addCard(new Card(5, "h","src/images/5h.gif"));
        cp=new CombinaisonProcess(hand);
        Card special = cp.getSpecialCard();
        assertNotNull(special);
        assertEquals(20, special.getNumber());
    }

    @Test
    public void testHasSimpleTrue() {
        hand.addCard(new Card(3, "h","src/images/3h.gif"));
        hand.addCard(new Card(0, "joker1","src/images/joker1.jpeg"));
        cp=new CombinaisonProcess(hand);
        assertTrue(cp.hasSimple());
    }

    @Test
    public void testHasSimpleFalse() {
        hand.addCard(new Card(0, "joker1","src/images/joker1.jpeg"));
        hand.addCard(new Card(20, "s","src/images/20s.gif"));
        cp=new CombinaisonProcess(hand);
        assertFalse(cp.hasSimple());
    }

    @Test
    public void testGetLowestCard() {
        hand.addCard(new Card(4, "c","src/images/4c.gif"));
        hand.addCard(new Card(3, "s","src/images/3s.gif"));
        hand.addCard(new Card(0, "joker1","src/images/joker1.jpeg"));
        cp=new CombinaisonProcess(hand);
        Card lowest = cp.getLowestCard();
        assertNotNull(lowest);
        assertEquals(3, lowest.getNumber());
    }

    @Test
    public void testCountSpecialCards() {
        hand.addCard(new Card(20, "h","src/images/20h.gif"));
        hand.addCard(new Card(20, "s","src/images/20s.gif"));
        hand.addCard(new Card(5, "c","src/images/5c.gif"));
        cp=new CombinaisonProcess(hand);
        assertEquals(2, cp.countSpecialCards());
    }

    @Test
    public void testCountSpecialCards_WithTwoSpecialCards() {
        hand.addCard(new Card(20, "c", "src/images/20c.gif")); // Spéciale
        hand.addCard(new Card(3, "h", "src/images/3h.gif"));  // Non spéciale
        hand.addCard(new Card(20, "d", "src/images/20d.gif")); // Spéciale
        hand.addCard(new Card(7, "s", "src/images/7s.gif"));  // Non spéciale

        CombinaisonProcess cp = new CombinaisonProcess(hand);

        assertEquals(2, cp.countSpecialCards());
    }

    @Test
    public void testCountSpecialCards_WithNoSpecialCard() {
        hand.addCard(new Card(5, "c", "src/images/5c.gif"));
        hand.addCard(new Card(8, "d", "src/images/8d.gif"));

        CombinaisonProcess cp = new CombinaisonProcess(hand);

        assertEquals(0, cp.countSpecialCards());
    }

    @Test
    public void testFindWinningCombination_WithWinningDouble() {
        Hand hand = new Hand();
        hand.addCard(new Card(3, "d", "src/images/3d.gif"));
        hand.addCard(new Card(3, "h", "src/images/3h.gif"));


        CombinaisonProcess cp = new CombinaisonProcess(hand);

        ArrayList<Card> result = cp.findWinningCombination();

        assertNotNull(result);
        assertEquals(3, result.get(0).getNumber());
        assertEquals(3, result.get(1).getNumber());
    }

    @Test
    public void testFindWinningCombination_WithWinningBomb() {
        Hand hand = new Hand();
        hand.addCard(new Card(3, "d", "src/images/3d.gif"));
        hand.addCard(new Card(3, "h", "src/images/3h.gif"));
        hand.addCard(new Card(0, "joker1", "src/images/joker1.jpeg"));



        CombinaisonProcess cp = new CombinaisonProcess(hand);

        ArrayList<Card> result = cp.findWinningCombination();

        assertNotNull(result);
    }

    @Test
    public void testFindWinningCombination_WithNoValidCombination() {
        Hand hand = new Hand();
        hand.addCard(new Card(3, "d", "src/images/3d.gif"));
        hand.addCard(new Card(4, "c", "src/images/4c.gif"));
        hand.addCard(new Card(7, "h", "src/images/7h.gif"));

        CombinaisonProcess cp = new CombinaisonProcess(hand);

        ArrayList<Card> result = cp.findWinningCombination();

        assertNull(result); // Aucune combinaison ne vide la main
    }

    @Test
    public void testGetMiddleCard_WithSeveralSimples() {
        Hand hand = new Hand();
        hand.addCard(new Card(3, "h", "src/images/3h.gif"));
        hand.addCard(new Card(5, "d", "src/images/5d.gif"));
        hand.addCard(new Card(7, "s", "src/images/7s.gif"));
        hand.addCard(new Card(20, "c", "src/images/20c.gif")); // Carte spéciale

        CombinaisonProcess cp = new CombinaisonProcess(hand);

        Card middle = cp.getMiddleCard();

        assertNotNull(middle);
        assertEquals(5, middle.getNumber());
    }

    @Test
    public void testGetMiddleCard_OnlySpecialCards() {
        Hand hand = new Hand();
        hand.addCard(new Card(20, "c", "src/images/20c.gif"));
        hand.addCard(new Card(20, "d", "src/images/20d.gif"));

        CombinaisonProcess cp = new CombinaisonProcess(hand);

        Card middle = cp.getMiddleCard();

        assertNotNull(middle);
        assertTrue(middle.getNumber() == 20); // getLowestCard() dans ce cas
    }




}
