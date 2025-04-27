package data.players;

import data.cards.Card;
import java.util.ArrayList;

/**
 * Represents a player's hand, which contains a list of cards the player currently holds.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public class Hand {
    private ArrayList<Card> cards; // List of cards in the player's hand

    /**
     * Default constructor for the Hand class. Initializes an empty hand.
     */
    public Hand() {
        this.cards = new ArrayList<>(); // Initialize the hand as an empty list
    }

    /**
     * Constructor to initialize the hand with a specific list of cards.
     *
     * @param cards The list of cards to initialize the hand.
     */
    public Hand(ArrayList<Card> cards) {
        this.cards = cards; // Set the cards for the hand
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card The card to add to the hand.
     */
    public void addCard(Card card) {
        cards.add(card); // Add the card to the hand
    }

    /**
     * Removes a card from the player's hand.
     *
     * @param card The card to remove from the hand.
     * @return true if the card was successfully removed, false otherwise.
     */
    public boolean removeCard(Card card) {
        return cards.remove(card); // Remove the card from the hand and return whether the operation was successful
    }

    /**
     * Sets the list of cards in the hand.
     *
     * @param cards The new list of cards to set for the hand.
     */
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards; // Set the new list of cards
    }

    /**
     * Checks if the hand is empty.
     *
     * @return true if the hand is empty, false otherwise.
     */
    public boolean isEmpty() {
        return cards.isEmpty(); // Return whether the hand is empty
    }

    /**
     * Gets the list of cards in the player's hand.
     *
     * @return A copy of the list of cards in the hand to prevent external modifications.
     */
    public ArrayList<Card> getCards() {
        return new ArrayList<>(cards); // Return a copy of the cards to avoid external modifications
    }

    /**
     * Gets the number of cards in the player's hand.
     *
     * @return The size of the hand.
     */
    public int size() {
        return cards.size(); // Return the number of cards in the hand
    }
}
