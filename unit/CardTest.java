package unit;


import data.cards.Card;
import org.junit.Test;


import static org.junit.Assert.*;

public class CardTest {
    @Test
    public void testGetNumber() {
        Card card = new Card(7, "h", "src/images/7h.gif");
        assertEquals(7, card.getNumber());
    }
    @Test
    public void testGetColor() {
        Card card = new Card(12, "s", "src/images/12s.gif");
        assertEquals("s", card.getColor());
    }

    @Test
    public void testGetImagePath() {
        Card card = new Card(10, "c", "src/images/10c.gif");
        assertEquals("src/images/10c.gif", card.getImagePath());
    }

    @Test
    public void testIsJokerTrue() {
        Card jokerCard = new Card(0, "joker1", "src/images/joker1.jpeg");
        assertTrue(jokerCard.isJoker());
    }

    @Test
    public void testIsJokerFalse() {
        Card nonJoker = new Card(3, "d", "src/images/3d.gif");
        assertFalse(nonJoker.isJoker());
    }



}
