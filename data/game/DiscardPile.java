package data.game;

import data.cards.Card;

import java.util.ArrayList;
import java.util.Stack;

public class DiscardPile {

    private Stack<Card> playedCards;

    public DiscardPile() {
        this.playedCards = new Stack<>();
    }

    public void addCard(Card card) {
        playedCards.push(card);
    }

    public void addCards(Stack<Card> cards) {
        while (!cards.isEmpty()) {
            playedCards.push(cards.pop());
        }
    }

    public Card getLastCard() {
        return playedCards.isEmpty() ? null : playedCards.peek();
    }

    public Stack<Card> getPlayedCards() {
        return (Stack<Card>) playedCards.clone(); // Return a copy to avoid modifications
    }

    public void clear() {
        playedCards.clear();
    }

    @Override
    public String toString() {
        return "DiscardPile: " + playedCards;
    }
}
