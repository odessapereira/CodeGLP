package data.cards;

import java.util.ArrayList;

/**
 * Represents a combination of cards in the game.
 * This class is abstract and serves as the base class for different types of card combinations.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public abstract class Combinaison {
    private ArrayList<Card> cards;

    /**
     * Constructor for the Combinaison class.
     * Initializes the combination with a list of cards.
     * If the list is null, an empty list is used.
     *
     * @param cards List of cards representing the combination.
     */
    public Combinaison(ArrayList<Card> cards) {
        this.cards = (cards != null) ? cards : new ArrayList<>(); // Use the provided cards or initialize an empty list.
    }

    /**
     * Returns the list of cards in the combination.
     *
     * @return The list of cards in the combination.
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Checks if the combination of cards is valid.
     * This is an abstract method, so it must be implemented in subclasses.
     *
     * @return true if the combination is valid, otherwise false.
     */
    public abstract boolean isValid();  // To be implemented in subclasses

    /**
     * Returns the value of the combination.
     * This is an abstract method, so it must be implemented in subclasses.
     *
     * @return The value of the combination.
     */
    public abstract int getValue();
}
