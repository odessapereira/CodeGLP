package data.game;

import data.cards.Card;
import java.util.HashMap;

/**
 * Represents the draw pile in the game, which holds the cards available to be drawn.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public class DrawPile {

    // Map for cards and their imagePath
    private HashMap<String, Card> drawCards;

    /**
     * Constructor for the DrawPile class.
     * Initializes the draw pile as an empty HashMap.
     */
    public DrawPile() {
        this.drawCards = new HashMap<>();
    }

    /**
     * Gets a copy of the remaining cards in the pile.
     *
     * @return A copy of the remaining cards in the draw pile.
     */
    public HashMap<String, Card> getRemainingCards() {
        return (HashMap<String, Card>) drawCards.clone(); // Return a clone to prevent external modifications
    }

    /**
     * Retrieves a card from the draw pile based on the imagePath.
     *
     * @param imagePath The image path associated with the card.
     * @return The card corresponding to the given imagePath, or null if not found.
     */
    public Card getCard(String imagePath) {
        return drawCards.get(imagePath); // Retrieve the card associated with the image path
    }

    /**
     * Removes a card from the draw pile based on the imagePath.
     *
     * @param imagePath The image path associated with the card to remove.
     */
    public void removeCard(String imagePath) {
        drawCards.remove(imagePath); // Remove the card from the draw pile
    }

    /**
     * Gets the entire draw pile.
     *
     * @return The draw pile as a HashMap of image paths to cards.
     */
    public HashMap<String, Card> getDrawCards() {
        return drawCards; // Return the draw pile HashMap
    }

    /**
     * Checks if the draw pile is empty.
     *
     * @return true if the draw pile is empty, false otherwise.
     */
    public boolean isEmpty() {
        return drawCards.isEmpty(); // Check if the draw pile is empty
    }

    /**
     * Adds a card to the draw pile.
     *
     * @param imagePath The image path associated with the card.
     * @param card The card to be added to the draw pile.
     */
    public void addCard(String imagePath, Card card) {
        if (card != null) { // Check to avoid adding a null card
            drawCards.put(imagePath, card); // Add the card to the draw pile
        }
    }
}
