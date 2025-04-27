package data.game;

import data.cards.*;

import java.util.ArrayList;

/**
 * Represents a discard pile in the game, which holds the played cards and the last valid combination of cards played.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public class DiscardPile {

    private ArrayList<Card> playedCards; // List of cards that have been played
    private ArrayList<Card> lastCombinaison; // The last valid combination of cards played

    /**
     * Constructor for the DiscardPile class.
     * Initializes the played cards and the last combination as empty lists.
     */
    public DiscardPile() {
        this.playedCards = new ArrayList<>();
        this.lastCombinaison = new ArrayList<>();
    }

    /**
     * Adds the given list of cards to the played cards.
     *
     * @param cards List of cards to be added to the discard pile.
     */
    public void addCards(ArrayList<Card> cards) {
        for (Card card : cards) {
            playedCards.add(card); // Add each card from the list to the played cards
        }
    }

    /**
     * Updates the last combination with the provided list of cards.
     * A valid combination must be a Bomb, Serie, or DoubleCard. If only one card is played, it's not considered a combination.
     *
     * @param cards List of cards representing the new combination to check.
     */
    public void updateLastCombinaison(ArrayList<Card> cards) {
        if (cards.size() == 1) {
            lastCombinaison = cards; // A single card played is not considered a combination
        } else if (new Bomb(cards).isValid() || new Serie(cards).isValid() || new DoubleCard(cards).isValid()) {
            lastCombinaison = cards; // If the cards form a valid combination, update the last combination
        } else {
            lastCombinaison = null; // If the cards are not valid, reset the last combination
        }
    }

    /**
     * Gets the last valid combination of cards played.
     *
     * @return The last combination of cards, or null if no valid combination exists.
     */
    public ArrayList<Card> getLastCombinaison() {
        return lastCombinaison;
    }

    /**
     * Sets the last combination of cards played.
     *
     * @param lastCombinaison List of cards to set as the last valid combination.
     */
    public void setLastCombinaison(ArrayList<Card> lastCombinaison) {
        this.lastCombinaison = lastCombinaison;
    }
}
