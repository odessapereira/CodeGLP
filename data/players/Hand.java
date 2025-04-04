package data.players;

import data.cards.Card;
import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> cards; // List of cards in the player's hand

    public Hand() {
        this.cards = new ArrayList<>();
    }

    // Add a card to the hand
    public void addCard(Card card) {
        cards.add(card);
    }

    // Remove a card from the hand
    public boolean removeCard(Card card) {
        return cards.remove(card);
    }

    // Play a card (returns the card and removes it from the hand)
    public Card playCard(int index) {
        if (index >= 0 && index < cards.size()) {
            return cards.remove(index);
        }
        System.out.println("❌ Invalid index!");
        return null;
    }

    // Check if the hand is empty
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    // Get the number of remaining cards
    public int getCardCount() {
        return cards.size();
    }


    // Get the list of cards in hand
    public List<Card> getCards() {
        return new ArrayList<>(cards); // Returns a copy to avoid external modifications
    }

    // Display the cards in hand
    public void displayHand() {
        System.out.println("🃏 Cards in hand:");
        for (int i = 0; i < cards.size(); i++) {
            System.out.println((i + 1) + ". " + cards.get(i));
        }
    }
}
