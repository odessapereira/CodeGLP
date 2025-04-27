package unit;

import data.cards.Card;
import data.cards.Serie;
import data.players.Hand;
import engine.CombinaisonProcess;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class SerieTest {
    private Hand hand ;
    private CombinaisonProcess cp;

    @Before
    public void setup() {
        hand = new Hand();
    }

    @Test
    public void testHasSerie_WithValidSerie() {
        hand.addCard(new Card(3, "c", "src/images/3c.gif"));
        hand.addCard(new Card(4, "d", "src/images/4d.gif"));
        hand.addCard(new Card(5, "h", "src/images/5h.gif"));

        cp=new CombinaisonProcess(hand);
        assertTrue(cp.hasSerie());
    }

    @Test
    public void testHasSerie_WithJokerGap() {
        hand.addCard(new Card(3, "d", "src/images/3d.gif"));
        hand.addCard(new Card(5, "h", "src/images/5h.gif"));
        hand.addCard(new Card(0, "joker1", "src/images/joker1.gif")); // joker comble le 4

        cp=new CombinaisonProcess(hand);
        assertTrue(cp.hasSerie());
    }

    @Test
    public void testHasSerie_InsufficientCards() {
        hand.addCard(new Card(7, "7c", "src/images/7c.gif"));
        hand.addCard(new Card(8, "8c", "src/images/8d.gif"));

        cp=new CombinaisonProcess(hand);
        assertFalse(cp.hasSerie());
    }

    @Test
    public void testGetBestSerie_ReturnsLongest() {
        hand.addCard(new Card(3, "c", "src/images/3c.gif"));
        hand.addCard(new Card(4, "d", "src/images/4d.gif"));
        hand.addCard(new Card(5, "h", "src/images/5h.gif"));
        hand.addCard(new Card(6, "h", "src/images/6h.gif"));


        cp=new CombinaisonProcess(hand);
        Serie serie = cp.getBestSerie();
        assertNotNull(serie);
        assertEquals(4, serie.getCards().size());
        assertTrue(serie.isValid());
    }

    @Test
    public void testGetShortestLowestSerie_ReturnsShortestLowest() {
        hand.addCard(new Card(3, "c", "src/images/3c.gif"));
        hand.addCard(new Card(4, "d", "src/images/4d.gif"));
        hand.addCard(new Card(5, "h", "src/images/5h.gif"));
        hand.addCard(new Card(6, "h", "src/images/6h.gif"));// Ne pas prendre ça

        cp=new CombinaisonProcess(hand);
        Serie serie = cp.getShortestLowestSerie();
        assertNotNull(serie);
        assertEquals(3, serie.getCards().size());
        assertEquals(3, serie.getCards().get(0).getNumber());
    }

    @Test
    public void testGetNextSerie_WithHigherSerie() {
        hand.addCard(new Card(10, "d", "src/images/10d.gif"));
        hand.addCard(new Card(11, "h", "src/images/11h.gif"));
        hand.addCard(new Card(12, "h", "src/images/12h.gif"));// Ne pas prendre



        cp=new CombinaisonProcess(hand);

        ArrayList<Card> liste = new ArrayList<>();
        Card card1 = new Card(9, "c", "src/images/9c.gif");
        Card card2 = new Card(10, "d", "src/images/10d.gif");
        Card card3 =   new Card(11, "s", "src/images/11s.gif");

        liste.add(card1);
        liste.add(card2);
        liste.add(card3);

        Serie last = new Serie(liste);

        Serie next = cp.getNextSerie(last);
        assertTrue(next.isValid());
        assertEquals(10, next.getCards().get(0).getNumber());
    }

    @Test
    public void testGetNextSerie_WithJoker() {
        hand.addCard(new Card(6, "c", "src/images/5c.gif"));
        hand.addCard(new Card(0, "joker1", "src/images/joker1.jpeg")); // Joker remplace 6
        hand.addCard(new Card(7, "d", "src/images/7d.gif"));
        hand.addCard(new Card(8, "h", "src/images/8h.jpeg"));


        cp=new CombinaisonProcess(hand);

        ArrayList<Card> liste = new ArrayList<>();
        Card card1 = new Card(4, "c", "src/images/4c.gif");
        Card card2 = new Card(5, "d", "src/images/5d.gif");
        Card card3 =   new Card(6, "s", "src/images/6s.gif");

        liste.add(card1);
        liste.add(card2);
        liste.add(card3);

        Serie last = new Serie(liste);

        Serie next = cp.getNextSerie(last);
        assertTrue(next.isValid());
        assertTrue(next.getCards().stream().anyMatch(Card::isJoker));
    }

}
