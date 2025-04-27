package unit;

import data.cards.Card;
import data.players.Hand;
import engine.GameEngine;
import engine.strategy.BotPlayer;
import engine.strategy.ReservedStrategy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ReservedStrategyTest {

    private Card createCard(int number, String suit, String imagePath) {
        return new Card(number, suit, imagePath);
    }

    private ArrayList<Card> createCardList(Card... cards) {
        return new ArrayList<>(Arrays.asList(cards));
    }

    private ReservedStrategy createStrategy(ArrayList<Card> handCards) {
        GameEngine gameEngine = new GameEngine();
        return new ReservedStrategy(gameEngine);
    }

    @Test
    public void testFirstTurn_PlayWeakestCard() {
        Card c1 = createCard(3, "s", "src/images/3s.gif");
        Card c2 = createCard(10, "h", "src/images/10h.gif");
        ArrayList<Card> handCards = createCardList(c1, c2);
        ReservedStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);


        ArrayList<Card> result = strategy.playTurn(new BotPlayer("bot 1", hand, strategy, 3), null);

        assertEquals(createCardList(c1), result);
    }

    @Test
    public void testFirstTurn_AvoidSpecialCard() {
        Card special = createCard(20, "s", "src/images/2s.gif");
        Card normal = createCard(4, "h", "src/images/4h.gif");
        ArrayList<Card> handCards = createCardList(special, normal);
        ReservedStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("bot", hand, strategy, 3), null);

        assertEquals(createCardList(normal), result);
    }

    @Test
    public void testSubsequentTurn_PlayMinimalCardAbove() {
        Card low = createCard(6, "s", "src/images/6s.gif");
        Card high = createCard(11, "d", "src/images/11d.gif");
        ArrayList<Card> handCards = createCardList(low, high);
        ArrayList<Card> lastPlayed = createCardList(createCard(5, "c", "src/images/5c.gif"));

        ReservedStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);
        ArrayList<Card> result = strategy.playTurn(new BotPlayer("bot", hand, strategy, 3), lastPlayed);

        assertEquals(createCardList(low), result);
    }

    @Test
    public void testSubsequentTurn_PassIfNoCardAbove() {
        Card low = createCard(4, "s", "src/images/4s.gif");
        ArrayList<Card> handCards = createCardList(low);
        ArrayList<Card> lastPlayed = createCardList(createCard(10, "s", "src/images/10s.gif"));

        ReservedStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);
        ArrayList<Card> result = strategy.playTurn(new BotPlayer("bot", hand, strategy, 3), lastPlayed);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testSubsequentTurn_WeakestDouble() {
        Card d1 = createCard(5, "s", "src/images/5s.gif");
        Card d2 = createCard(5, "h", "src/images/5h.gif");
        Card d3 = createCard(8, "d", "src/images/8d.gif");
        ArrayList<Card> handCards = createCardList(d1, d2, d3);
        ArrayList<Card> lastPlayed = createCardList(
                createCard(4, "d", "src/images/4d.gif"),
                createCard(4, "c", "src/images/4c.gif")
        );

        ReservedStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);
        ArrayList<Card> result = strategy.playTurn(new BotPlayer("bot", hand, strategy, 3), lastPlayed);

        assertEquals(createCardList(d1, d2), result);
    }

    @Test
    public void testSubsequentTurn_PlayMinimalSerieAbove() {
        Card c1 = createCard(6, "s", "src/images/6s.gif");
        Card c2 = createCard(7, "h", "src/images/7h.gif");
        Card c3 = createCard(8, "d", "src/images/8d.gif");
        ArrayList<Card> handCards = createCardList(c1, c2, c3);
        ArrayList<Card> lastPlayed = createCardList(
                createCard(5, "c", "src/images/5c.gif"),
                createCard(6, "d", "src/images/6d.gif"),
                createCard(7, "s", "src/images/7s.gif")
        );

        ReservedStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);
        ArrayList<Card> result = strategy.playTurn(new BotPlayer("bot", hand, strategy, 3), lastPlayed);

        assertEquals(handCards, result);
    }

    @Test
    public void testAvoidBombIfPossible() {
        Card b1 = createCard(11, "s", "src/images/11s.gif");
        Card b2 = createCard(11, "h", "src/images/11h.gif");
        Card b3 = createCard(11, "d", "src/images/11d.gif");
        Card b4 = createCard(11, "c", "src/images/11c.gif");
        Card normal = createCard(10, "s", "src/images/10s.gif");

        ArrayList<Card> handCards = createCardList(b1, b2, b3, b4, normal);
        ArrayList<Card> lastPlayed = createCardList(createCard(9, "s", "src/images/9s.gif"));

        ReservedStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("bot", hand, strategy, 3), lastPlayed);

        // Il doit jouer la carte normale au lieu de la bombe
        assertEquals(createCardList(normal), result);
    }

    @Test
    public void testAvoidBombIfPossibleAndPlay2() {
        Card b1 = createCard(11, "s", "src/images/11s.gif");
        Card b2 = createCard(11, "h", "src/images/11h.gif");
        Card b3 = createCard(11, "d", "src/images/11d.gif");
        Card b4 = createCard(11, "c", "src/images/11c.gif");
        Card normal = createCard(20, "s", "src/images/20s.gif");

        ArrayList<Card> handCards = createCardList(b1, b2, b3, b4, normal);
        ArrayList<Card> lastPlayed = createCardList(createCard(10, "s", "src/images/9s.gif"),createCard(10, "d", "src/images/9d.gif"));

        ReservedStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("bot", hand, strategy, 3), lastPlayed);

        // Il doit jouer la carte normale au lieu de la bombe
        assertEquals(createCardList(normal), result);
    }

    @Test
    public void testPlayBombAsLastResort() {
        Card b1 = createCard(10, "s", "src/images/10s.gif");
        Card b2 = createCard(10, "h", "src/images/10h.gif");
        Card b3 = createCard(10, "d", "src/images/10d.gif");
        Card b4 = createCard(10, "c", "src/images/10c.gif");
        ArrayList<Card> handCards = createCardList(b1, b2, b3, b4);
        ArrayList<Card> lastPlayed = createCardList(createCard(13, "s", "src/images/13s.gif"));

        ReservedStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);
        ArrayList<Card> result = strategy.playTurn(new BotPlayer("bot", hand, strategy, 3), lastPlayed);

        assertEquals(handCards, result); // Bombe jouée car seule possibilité
    }

    @Test
    public void testAvoidUsing2() {
        Card two = createCard(20, "s", "src/images/2s.gif");
        Card c1 = createCard(9, "h", "src/images/9h.gif");

        ArrayList<Card> handCards = createCardList(two, c1);
        ArrayList<Card> lastPlayed = createCardList(createCard(8, "d", "src/images/7d.gif"));

        ReservedStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("bot", hand, strategy, 3), lastPlayed);

        assertEquals(createCardList(c1), result); // Pas besoin du 2
    }

    @Test
    public void testAvoidUsing2Double() {
        Card two = createCard(20, "s", "src/images/2s.gif");
        Card c1 = createCard(9, "h", "src/images/9h.gif");
        Card c2 = createCard(0, "joker1", "src/images/joker1.jpeg");


        ArrayList<Card> handCards = createCardList(two, c1,c2);
        ArrayList<Card> lastPlayed = createCardList(createCard(8, "d", "src/images/7d.gif"),createCard(8, "h", "src/images/8h.gif"));

        ReservedStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("bot", hand, strategy, 3), lastPlayed);

        assertEquals(createCardList(c1,c2), result); // Pas besoin du 2
    }

    @Test
    public void testUse2AsLastResort() {
        Card two = createCard(20, "s", "src/images/2s.gif");

        ArrayList<Card> handCards = createCardList(two);
        ArrayList<Card> lastPlayed = createCardList(createCard(13, "d", "src/images/13d.gif"));

        ReservedStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("bot", hand, strategy, 3), lastPlayed);

        assertEquals(createCardList(two), result); // Dernière option
    }

    @Test
    public void testPassIfNoBetterCard() {
        Card c1 = createCard(5, "s", "src/images/5s.gif");
        Card c2 = createCard(6, "h", "src/images/6h.gif");

        ArrayList<Card> handCards = createCardList(c1, c2);
        ArrayList<Card> lastPlayed = createCardList(createCard(10, "d", "src/images/10d.gif"));

        ReservedStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("bot", hand, strategy, 3), lastPlayed);

        assertTrue(result.isEmpty()); // Aucun coup possible → il passe
    }


    @Test
    public void testAvoidBreakingBomb() {
        Card b1 = createCard(10, "s", "src/images/10s.gif");
        Card b2 = createCard(10, "h", "src/images/10h.gif");
        Card b3 = createCard(10, "d", "src/images/10d.gif");
        Card b4 = createCard(10, "c", "src/images/10c.gif");
        Card other = createCard(9, "s", "src/images/9s.gif");

        ArrayList<Card> handCards = createCardList(b1, b2, b3, b4, other);
        ArrayList<Card> lastPlayed = createCardList(createCard(8, "d", "src/images/8d.gif"));

        ReservedStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("bot", hand, strategy, 5), lastPlayed);

        assertEquals(createCardList(other), result); // Il ne casse pas la bombe 10
    }

    @Test
    public void testAvoidBreakingDoubleJoker() {
        Card joker1 = createCard(0, "joker1", "src/images/joker1.jpeg");
        Card joker2 = createCard(0, "joker1", "src/images/joker1.jpeg");
        Card c1 = createCard(10, "d", "src/images/10d.gif");

        ArrayList<Card> handCards = createCardList(joker1, joker2, c1);
        ArrayList<Card> lastPlayed = createCardList(createCard(9, "d", "src/images/9d.gif"));

        ReservedStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("bot", hand, strategy, 5), lastPlayed);

        assertEquals(c1, result.get(0));//laisser le double joker en dernier
    }


    @Test
    public void testUseJokerToCompleteDoubleIfNeeded() {
        Card joker = createCard(0, "joker1", "src/images/joker1.jpeg");
        Card c1 = createCard(8, "s", "src/images/8s.gif");

        ArrayList<Card> handCards = createCardList(joker, c1);
        ArrayList<Card> lastPlayed = createCardList(
                createCard(7, "d", "src/images/7d.gif"),
                createCard(7, "h", "src/images/7h.gif")
        );

        ReservedStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("bot", hand, strategy, 2), lastPlayed);

        // Il joue une double 8 grâce au joker
        assertEquals(createCardList(c1, joker), result);
    }
    @Test
    public void testPreferSeriesWithtJokerFirstTour() {
        Card joker = createCard(0, "joker1", "src/images/joker1.jpeg");
        Card c1 = createCard(6, "s", "src/images/6s.gif");
        Card c2 = createCard(7, "h", "src/images/7h.gif");
        Card c3 = createCard(8, "d", "src/images/8d.gif");
        Card alt1 = createCard(10, "c", "src/images/10c.gif");
        Card alt2 = createCard(11, "s", "src/images/11s.gif");

        // 6-7-8 sans joker, ou 10-joker-11 plus fort, mais stratégie réservée
        ArrayList<Card> handCards = createCardList(c1, c2, c3, joker, alt1, alt2);
        ArrayList<Card> lastPlayed = new ArrayList<>(); // Il commence

        ReservedStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("bot", hand, strategy, 6), lastPlayed);

        // Il joue la série sans joker (6-7-8)
        assertEquals(handCards.size(), result.size());
    }


}
