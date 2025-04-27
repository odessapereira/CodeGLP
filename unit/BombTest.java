package unit;


import data.cards.Bomb;
import data.cards.Card;
import data.players.Hand;
import engine.CombinaisonProcess;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class BombTest {

    private Hand hand ;
    private CombinaisonProcess cp;

    @Before
    public void setup() {
        hand = new Hand();
    }

    @Test
    public void testHasBomb_WithValidBomb() {
        hand.addCard(new Card(5, "c", "src/images/5c.gif"));
        hand.addCard(new Card(5, "h", "src/images/5h.gif"));
        hand.addCard(new Card(5, "s", "src/images/5s.gif"));

        cp=new CombinaisonProcess(hand);
        assertTrue(cp.hasBomb());
    }

    @Test
    public void testHasBomb_NoBomb() {
        hand.addCard(new Card(3, "c", "src/images/3c.gif"));
        hand.addCard(new Card(4, "h", "src/images/4h.gif"));
        hand.addCard(new Card(5, "s", "src/images/5s.gif"));

        cp=new CombinaisonProcess(hand);
        assertFalse(cp.hasBomb());
    }

    @Test
    public void testGetBestBomb_ReturnsValidBomb() {
        hand.addCard(new Card(7, "c", "src/images/7c.gif"));
        hand.addCard(new Card(7, "h", "src/images/7h.gif"));
        hand.addCard(new Card(7, "s", "src/images/7s.gif"));
        hand.addCard(new Card(7, "d", "src/images/7d.gif"));
        hand.addCard(new Card(5, "c", "src/images/5c.gif"));
        hand.addCard(new Card(5, "h", "src/images/5h.gif"));
        hand.addCard(new Card(5, "s", "src/images/5s.gif"));

        cp=new CombinaisonProcess(hand);
        Bomb bomb = cp.getBestBomb();
        assertTrue(bomb.isValid());
        assertEquals(4, bomb.getCards().size());
    }


    @Test
    public void testGetWeakestBombWithout2() {
        hand.addCard(new Card(4, "s", "src/images/4s.gif"));
        hand.addCard(new Card(4, "h", "src/images/4h.gif"));
        hand.addCard(new Card(4, "c", "src/images/4c.gif"));
        hand.addCard(new Card(20, "h", "src/images/20h.gif")); // Ne doit pas être pris

        cp=new CombinaisonProcess(hand);
        Bomb bomb = cp.getWeakestBombWithout2();
        assertTrue(bomb.isValid());
        boolean contains2 = false;
        for (Card c : bomb.getCards()) {
            if (c.getNumber() == 2) {
                contains2 = true;
                break;
            }
        }
        assertFalse(contains2);
    }

    @Test
    public void testGetNextBomb_ShouldReturnStronger() {
        hand.addCard(new Card(6, "c", "src/images/6c.gif"));
        hand.addCard(new Card(6, "s", "src/images/6s.gif"));
        hand.addCard(new Card(6, "d", "src/images/6d.gif"));
        hand.addCard(new Card(6, "h", "src/images/6h.gif"));


        cp=new CombinaisonProcess(hand);


        ArrayList<Card> liste = new ArrayList<>();
        Card card1 = new Card(5, "c", "src/images/5c.gif");
        Card card2 = new Card(5, "d", "src/images/5d.gif");
        Card card3 =   new Card(5, "s", "src/images/5s.gif");


        liste.add(card1);
        liste.add(card2);
        liste.add(card3);

        Bomb last = new Bomb(liste);

        Bomb next = cp.getNextBomb(last);
        assertTrue(next.isValid());
        assertTrue(next.getValue() > last.getValue());
    }

    @Test
    public void testNotBombOf2() {
        hand.addCard(new Card(20, "s", "src/images/20s.gif")); // Ne doit pas être pris
        hand.addCard(new Card(20, "d", "src/images/20d.gif")); // Ne doit pas être pris
        hand.addCard(new Card(20, "h", "src/images/20h.gif")); // Ne doit pas être pris

        cp=new CombinaisonProcess(hand);
        Bomb bomb = cp.getBestBomb();
        assertFalse(bomb.isValid());
    }
}
