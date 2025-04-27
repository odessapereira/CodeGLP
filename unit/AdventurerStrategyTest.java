package unit;


import data.cards.*;
import data.players.Hand;
import engine.GameEngine;
import engine.TurnManager;
import engine.strategy.AdventurerStrategy;
import engine.strategy.BotPlayer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class AdventurerStrategyTest {

    // Helper method to create a Card
    private Card createCard(int number, String suit, String imagePath) {
        return new Card(number, suit, imagePath);
    }

    // Helper method to create a list of cards
    private ArrayList<Card> createCardList(Card... cards) {
        return new ArrayList<>(Arrays.asList(cards));
    }

    // Créer une stratégie avec des dépendances concrètes
    private AdventurerStrategy createStrategy(ArrayList<Card> handCards) {
        Hand hand = new Hand(handCards);
        TurnManager turnManager = new TurnManager();
        GameEngine gameEngine = new GameEngine();
        BotPlayer bot = new BotPlayer("botPlayer",hand,new AdventurerStrategy(gameEngine),3);
        return new AdventurerStrategy(gameEngine);
    }

    // Premier tour : lastPlayedCombinaison est null ou vide
    @Test
    public void testFirstTurn_DoubleJoker() {
        Card joker1 = createCard(0, "joker1", "src/images/joker1.jpeg");
        Card joker2 = createCard(0, "joker2", "src/images/joker2.jpeg");
        ArrayList<Card> handCards = createCardList(joker1, joker2);
        AdventurerStrategy strategy = createStrategy(handCards);

        Hand hand=new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("botPlayer",hand,strategy,3), null);

        assertEquals(createCardList(joker1, joker2), result);
    }

    @Test
    public void testFirstTurn_Bomb() {
        Card card1 = createCard(5, "s", "src/images/5s.gif");
        Card card2 = createCard(5, "h", "src/images/5h.gif");
        Card card3 = createCard(5, "d", "src/images/5d.gif");
        Card card4 = createCard(5, "c", "src/images/5c.gif");
        ArrayList<Card> handCards = createCardList(card1, card2, card3, card4);
        AdventurerStrategy strategy = createStrategy(handCards);
        Hand hand=new Hand(handCards);
        ArrayList<Card> result = strategy.playTurn(new BotPlayer("botPlayer",hand,createStrategy(handCards),3), new ArrayList<>());

        assertEquals(handCards, result);
    }

    @Test
    public void testFirstTurn_SpecialCard() {
        Card specialCard = createCard(20, "s", "src/images/2s.gif");
        ArrayList<Card> handCards = createCardList(specialCard);
        AdventurerStrategy strategy = createStrategy(handCards);
        Hand hand=new Hand(handCards);
        ArrayList<Card> result = strategy.playTurn(new BotPlayer("botPlayer",hand,createStrategy(handCards),3), null);

        assertEquals(createCardList(specialCard), result);
    }

    @Test
    public void testFirstTurn_Serie() {
        Card card1 = createCard(9, "s", "src/images/9s.gif");
        Card card2 = createCard(10, "h", "src/images/10h.gif");
        Card card3 = createCard(11, "d", "src/images/11d.gif");
        ArrayList<Card> handCards = createCardList(card1, card2, card3);
        AdventurerStrategy strategy = createStrategy(handCards);
        Hand hand=new Hand(handCards);
        ArrayList<Card> result = strategy.playTurn(new BotPlayer("botPlayer",hand,createStrategy(handCards),3), null);

        assertEquals(handCards, result);
    }

    @Test
    public void testFirstTurn_Double() {
        Card card1 = createCard(7, "s", "src/images/7s.gif");
        Card card2 = createCard(7, "h", "src/images/7h.gif");
        ArrayList<Card> handCards = createCardList(card1, card2);
        AdventurerStrategy strategy = createStrategy(handCards);
        Hand hand=new Hand(handCards);
        ArrayList<Card> result = strategy.playTurn(new BotPlayer("botPlayer",hand,createStrategy(handCards),3), null);

        assertEquals(handCards, result);
    }

    @Test
    public void testFirstTurn_HighestCard() {
        Card card = createCard(13, "s", "src/images/13s.gif");
        ArrayList<Card> handCards = createCardList(card);
        AdventurerStrategy strategy = createStrategy(handCards);
        Hand hand=new Hand(handCards);
        ArrayList<Card> result = strategy.playTurn(new BotPlayer("botPlayer",hand,createStrategy(handCards),3), null);

        assertEquals(createCardList(card), result);
    }

    // Tours suivants : lastPlayedCombinaison non vide
    @Test
    public void testSubsequentTurn_DoubleJoker() {
        Card joker1 = createCard(0, "joker1", "src/images/joker1.jpeg");
        Card joker2 = createCard(0, "joker2", "src/images/joker2.jpeg");
        ArrayList<Card> handCards = createCardList(joker1, joker2);
        ArrayList<Card> lastPlayed = createCardList(createCard(10, "s", "src/images/10s.gif"));
        AdventurerStrategy strategy = createStrategy(handCards);
        Hand hand=new Hand(handCards);
        ArrayList<Card> result = strategy.playTurn((new BotPlayer("botPlayer",hand,createStrategy(handCards),3)), lastPlayed);

        assertEquals(createCardList(joker1, joker2), result);
    }

    @Test
    public void testSubsequentTurn_BetterBomb() {
        Card card1 = createCard(6, "s", "src/images/6s.gif");
        Card card2 = createCard(6, "h", "src/images/6h.gif");
        Card card3 = createCard(6, "d", "src/images/6d.gif");
        Card card4 = createCard(6, "c", "src/images/6c.gif");
        ArrayList<Card> handCards = createCardList(card1, card2, card3, card4);
        ArrayList<Card> lastPlayed = createCardList(
                createCard(5, "s", "src/images/5s.gif"),
                createCard(5, "h", "src/images/5h.gif"),
                createCard(5, "d", "src/images/5d.gif"),
                createCard(5, "c", "src/images/5c.gif")
        );
        AdventurerStrategy strategy = createStrategy(handCards);
        Hand hand=new Hand(handCards);
        ArrayList<Card> result = strategy.playTurn(new BotPlayer("botPlayer",hand,createStrategy(handCards),3), lastPlayed);

        assertEquals(handCards, result);
    }

    @Test
    public void testSubsequentTurn_BetterDouble() {
        Card card1 = createCard(8, "s", "src/images/8s.gif");
        Card card2 = createCard(8, "h", "src/images/8h.gif");
        ArrayList<Card> handCards = createCardList(card1, card2);
        ArrayList<Card> lastPlayed = createCardList(
                createCard(7, "s", "src/images/7s.gif"),
                createCard(7, "h", "src/images/7h.gif")
        );
        AdventurerStrategy strategy = createStrategy(handCards);
        Hand hand=new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("botPlayer",hand,createStrategy(handCards),3), lastPlayed);

        assertEquals(handCards, result);
    }

    @Test
    public void testSubsequentTurn_BombAfterDouble() {
        Card card1 = createCard(6, "s", "src/images/6s.gif");
        Card card2 = createCard(6, "h", "src/images/6h.gif");
        Card card3 = createCard(6, "d", "src/images/6d.gif");
        Card card4 = createCard(6, "c", "src/images/6c.gif");
        ArrayList<Card> handCards = createCardList(card1, card2, card3, card4);
        ArrayList<Card> lastPlayed = createCardList(
                createCard(7, "s", "src/images7s.gif"),
                createCard(7, "h", "src/images7h.gif")
        );
        AdventurerStrategy strategy = createStrategy(handCards);
        Hand hand=new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("botPlayer",hand,createStrategy(handCards),3), lastPlayed);

        assertEquals(handCards, result);
    }

    @Test
    public void testSubsequentTurn_SpecialCardAfterDouble() {
        Card specialCard = createCard(20, "s", "src/images/2s.gif");
        ArrayList<Card> handCards = createCardList(specialCard);
        ArrayList<Card> lastPlayed = createCardList(
                createCard(7, "s", "src/images/7s.gif"),
                createCard(7, "h", "src/images/7h.gif")
        );
        AdventurerStrategy strategy = createStrategy(handCards);
        Hand hand=new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("botPlayer",hand,createStrategy(handCards),3), lastPlayed);

        assertEquals(createCardList(specialCard), result);
    }

    @Test
    public void testSubsequentTurn_BetterSerie() {
        Card card1 = createCard(10, "s", "src/images/10s.gif");
        Card card2 = createCard(11, "h", "src/images/11h.gif");
        Card card3 = createCard(12, "d", "src/images/12d.gif");
        ArrayList<Card> handCards = createCardList(card1, card2, card3);
        ArrayList<Card> lastPlayed = createCardList(
                createCard(9, "s", "src/images/9s.gif"),
                createCard(10, "h", "src/images/10h.gif"),
                createCard(11, "d", "src/images/11d.gif")
        );
        AdventurerStrategy strategy = createStrategy(handCards);

        Hand hand=new Hand(handCards);
        ArrayList<Card> result = strategy.playTurn(new BotPlayer("botPlayer",hand,createStrategy(handCards),3), lastPlayed);

        assertEquals(handCards, result);
    }

    @Test
    public void testSubsequentTurn_BombAfterSerie() {
        Card card1 = createCard(6, "s", "src/images/6s.gif");
        Card card2 = createCard(6, "h", "src/images/6h.gif");
        Card card3 = createCard(6, "d", "src/images/6d.gif");
        Card card4 = createCard(6, "c", "src/images/6c.gif");
        ArrayList<Card> handCards = createCardList(card1, card2, card3, card4);
        ArrayList<Card> lastPlayed = createCardList(
                createCard(9, "s", "src/images/9s.gif"),
                createCard(10, "h", "src/images/10h.gif"),
                createCard(11, "d", "src/images/11d.gif")
        );
        AdventurerStrategy strategy = createStrategy(handCards);
        Hand hand=new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("botPlayer",hand,createStrategy(handCards),3), lastPlayed);

        assertEquals(handCards, result);
    }

    @Test
    public void testSubsequentTurn_HigherCard() {
        Card card = createCard(11, "s", "11s.gif");
        ArrayList<Card> handCards = createCardList(card);
        ArrayList<Card> lastPlayed = createCardList(createCard(10, "s", "10s.gif"));
        AdventurerStrategy strategy = createStrategy(handCards);
        Hand hand=new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("botPlayer",hand,createStrategy(handCards),3), lastPlayed);

        assertEquals(createCardList(card), result);
    }

    @Test
    public void testSubsequentTurn_BombAfterSingle() {
        Card card1 = createCard(6, "s", "src/images/6s.gif");
        Card card2 = createCard(6, "h", "src/images/6h.gif");
        Card card3 = createCard(6, "d", "src/images/6d.gif");
        Card card4 = createCard(6, "c", "src/images/6c.gif");
        ArrayList<Card> handCards = createCardList(card1, card2, card3, card4);
        ArrayList<Card> lastPlayed = createCardList(createCard(10, "s", "src/images/10s.gif"));
        AdventurerStrategy strategy = createStrategy(handCards);

        Hand hand=new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("botPlayer",hand,strategy,3), lastPlayed);

        assertEquals(handCards, result);
    }

    @Test
    public void testSubsequentTurn_SpecialCardAfterSingle() {
        Card specialCard = createCard(20, "s", "src/images/20s.gif");
        ArrayList<Card> handCards = createCardList(specialCard);
        ArrayList<Card> lastPlayed = createCardList(createCard(10, "s", "src/images/10s.gif"));
        AdventurerStrategy strategy = createStrategy(handCards);
        Hand hand=new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("botPlayer",hand,strategy,3), lastPlayed);

        assertEquals(createCardList(specialCard), result);
    }

    @Test
    public void testPassTurn_NoValidMove() {
        ArrayList<Card> handCards = createCardList(createCard(5, "s", "src/images/5s.gif"));
        ArrayList<Card> lastPlayed = createCardList(createCard(13, "s", "src/images/13s.gif"));
        AdventurerStrategy strategy = createStrategy(handCards);
        Hand hand=new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("botPlayer",hand,strategy,3), lastPlayed);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFirstTurn_WinningCombination() {
        Card card1 = createCard(10, "s", "src/images/10s.gif");
        Card card2 = createCard(11, "h", "src/images/11h.gif");
        Card card3 = createCard(12, "d", "src/images/12d.gif");
        ArrayList<Card> handCards = createCardList(card1, card2, card3);
        AdventurerStrategy strategy = createStrategy(handCards);
        Hand hand=new Hand(handCards);

        ArrayList<Card> result = strategy.playTurn(new BotPlayer("botPlayer",hand,strategy,3), null);

        assertEquals(handCards, result);
    }
}
