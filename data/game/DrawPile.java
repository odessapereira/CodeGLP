package data.game;

import data.cards.Card;

import java.util.Collections;
import java.util.Stack;

public class DrawPile {

    // The unique instance of the DrawPile
    private static DrawPile instance;

    // The stack of cards
    private Stack<Card> drawCards;

    // Private constructor to prevent the creation of other instances
    private DrawPile() {
        this.drawCards = new Stack<>();
    }

    // Method to get the unique instance of DrawPile
    public static DrawPile getInstance() {
        if (instance == null) {
            instance = new DrawPile();
        }
        return instance;
    }

    // Add cards to the pile (This should only be done when initializing the pile, not during gameplay)
    public void initializeCards(Stack<Card> cards) {
        drawCards.addAll(cards);
    }

    // Shuffle the cards in the pile
    public void shuffle() {
        Collections.shuffle(drawCards);
    }

    // Draw a card from the pile
    public Card drawCard() {
        return drawCards.isEmpty() ? null : drawCards.pop();
    }

    // Get a copy of the remaining cards in the pile
    public Stack<Card> getRemainingCards() {
        return (Stack<Card>) drawCards.clone();
    }

    // Check if the pile is empty
    public boolean isEmpty() {
        return drawCards.isEmpty();
    }

    // Clear the pile (should be used only when resetting the pile)
    public void clear() {
        drawCards.clear();
    }

    @Override
    public String toString() {
        return "DrawPile: " + drawCards;
    }
}
