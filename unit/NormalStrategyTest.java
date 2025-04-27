package unit;

import data.cards.Card;
import data.players.Hand;
import engine.GameEngine;
import engine.strategy.BotPlayer;
import engine.strategy.NormalStrategy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class NormalStrategyTest {

    private Card createCard(int number, String suit, String imagePath) {
        return new Card(number, suit, imagePath);
    }

    private ArrayList<Card> createCardList(Card... cards) {
        return new ArrayList<>(Arrays.asList(cards));
    }

    private NormalStrategy createStrategy(ArrayList<Card> handCards) {
        Hand hand = new Hand(handCards);
        GameEngine engine = new GameEngine();
        BotPlayer bot = new BotPlayer("NormalBot", hand, new NormalStrategy(engine), 3);
        return new NormalStrategy(engine);
    }

    @Test
    public void testPlayTurn_PlaysBetterDoubleThanPrevious() {
        Card card1 = createCard(9, "h", "src/images/9h.gif");
        Card card2 = createCard(9, "s", "src/images/9s.gif");
        ArrayList<Card> handCards = createCardList(card1, card2);

        Card lastCard1 = createCard(8, "h", "src/images/8h.gif");
        Card lastCard2 = createCard(8, "s", "src/images/8s.gif");
        ArrayList<Card> lastPlayed = createCardList(lastCard1, lastCard2);

        NormalStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);
        BotPlayer bot = new BotPlayer("NormalBot", hand, strategy, 3);

        ArrayList<Card> result = strategy.playTurn(bot, lastPlayed);

        assertEquals(handCards, result);
    }

    @Test
    public void testPlayTurn_NoPlayableCard_ShouldPass() {
        Card card1 = createCard(4, "h", "src/images/4h.gif");
        ArrayList<Card> handCards = createCardList(card1);

        Card lastCard = createCard(10, "s", "src/images/10s.gif");
        ArrayList<Card> lastPlayed = createCardList(lastCard);

        NormalStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);
        BotPlayer bot = new BotPlayer("NormalBot", hand, strategy, 3);

        ArrayList<Card> result = strategy.playTurn(bot, lastPlayed);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testPlayTurn_SpecialCardFirstTurn() {
        Card card = createCard(20, "d", "src/images/2d.gif");  // 2 bloqueur
        ArrayList<Card> handCards = createCardList(card);

        NormalStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);
        BotPlayer bot = new BotPlayer("NormalBot", hand, strategy, 3);

        ArrayList<Card> result = strategy.playTurn(bot, null);

        assertEquals(createCardList(card), result);
    }

    @Test
    public void testPlayTurn_PlaySingleCard() {
        Card card1 = createCard(6, "s", "src/images/6s.gif");
        Card card2 = createCard(10, "s", "src/images/10s.gif");

        ArrayList<Card> handCards = createCardList(card1,card2);

        ArrayList<Card> lastPlayed = createCardList(createCard(5, "h", "src/images/5h.gif"));

        NormalStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);
        BotPlayer bot = new BotPlayer("NormalBot", hand, strategy, 3);

        ArrayList<Card> result = strategy.playTurn(bot, lastPlayed);

        assertEquals(createCardList(card1), result);
    }

    @Test
    public void testPlayTurn_PlayBombWhenNothingElse() {
        Card card1 = createCard(7, "s", "src/images/7s.gif");
        Card card2 = createCard(7, "h", "src/images/7h.gif");
        Card card3 = createCard(7, "d", "src/images/7d.gif");
        Card card4 = createCard(7, "c", "src/images/7c.gif");
        ArrayList<Card> handCards = createCardList(card1, card2, card3, card4);

        ArrayList<Card> lastPlayed = createCardList(createCard(12, "h", "src/images/12h.gif"));

        NormalStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);
        BotPlayer bot = new BotPlayer("NormalBot", hand, strategy, 3);

        ArrayList<Card> result = strategy.playTurn(bot, lastPlayed);

        assertEquals(handCards, result);
    }

    @Test
    public void testPlayTurn_PlaysBetterSerieThanPrevious() {
        Card c1 = createCard(5, "h", "src/images/5h.gif");
        Card c2 = createCard(6, "s", "src/images/6s.gif");
        Card c3 = createCard(7, "d", "src/images/7d.gif");
        ArrayList<Card> handCards = createCardList(c1, c2, c3);

        Card last1 = createCard(3, "d", "src/images/3d.gif");
        Card last2 = createCard(4, "s", "src/images/4s.gif");
        Card last3 = createCard(5, "h", "src/images/5h.gif");

        ArrayList<Card> lastPlayed = createCardList(last1, last2, last3);

        NormalStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);
        BotPlayer bot = new BotPlayer("NormalBot", hand, strategy, 3);

        ArrayList<Card> result = strategy.playTurn(bot, lastPlayed);

        assertEquals(handCards, result);
    }

    @Test
    public void testPlayTurn_PrefersCombinaisonOverBomb() {
        Card b1 = createCard(7, "s", "src/images/7s.gif");
        Card b2 = createCard(7, "h", "src/images/7h.gif");
        Card b3 = createCard(7, "d", "src/images/7d.gif");
        Card b4 = createCard(7, "c", "src/images/7c.gif");

        Card d1 = createCard(8, "h", "src/images/8h.gif");
        Card d2 = createCard(8, "s", "src/images/8s.gif");

        ArrayList<Card> handCards = createCardList(b1, b2, b3, b4, d1, d2);

        ArrayList<Card> lastPlayed = createCardList(createCard(7, "h", "src/images/7h.gif"),createCard(7, "s", "src/images/7s.gif"));

        NormalStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);
        BotPlayer bot = new BotPlayer("NormalBot", hand, strategy, 3);

        ArrayList<Card> result = strategy.playTurn(bot, lastPlayed);

        assertEquals(createCardList(d1, d2), result);
    }

    @Test
    public void testPlayTurn_CannotBeatSerie_ShouldPass() {
        Card c1 = createCard(3, "h", "src/images/3h.gif");
        Card c2 = createCard(4, "s", "src/images/4s.gif");
        Card c3 = createCard(6, "d", "src/images/6d.gif");
        ArrayList<Card> handCards = createCardList(c1, c2, c3);

        Card last1 = createCard(7, "h", "src/images/7h.gif");
        Card last2 = createCard(8, "s", "src/images/8s.gif");
        Card last3 = createCard(9, "d", "src/images/9d.gif");
        ArrayList<Card> lastPlayed = createCardList(last1, last2, last3);

        NormalStrategy strategy = createStrategy(handCards);
        Hand hand = new Hand(handCards);
        BotPlayer bot = new BotPlayer("NormalBot", hand, strategy, 3);

        ArrayList<Card> result = strategy.playTurn(bot, lastPlayed);

        assertTrue(result.isEmpty());
    }




}
