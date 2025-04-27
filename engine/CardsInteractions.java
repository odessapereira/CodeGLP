package engine;

import data.cards.Card;
import data.game.DrawPile;

import java.util.*;

/**
 * Singleton class to handle card interactions such as getting, adding, removing, and drawing cards.
 * This class manages a deck of cards, reloading the deck when necessary.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public class CardsInteractions {
    private static CardsInteractions instance; // Singleton instance
    private DrawPile pioche; // The deck of cards
    private ArrayList<Card> cardsCopy; // A copy of the deck for reloading
    private List<String> keys; // A list of keys representing card image paths


    // Private constructor to prevent external instantiation
    private CardsInteractions() {
        pioche = new DrawPile();
        cardsCopy = new ArrayList<>();
        initializeCards(); // Initialize the deck of cards once
    }

    // Method to get the singleton instance
    public static synchronized CardsInteractions getInstance() {
        if (instance == null) {
            instance = new CardsInteractions();
        }
        return instance;
    }

    // Initializes the deck with cards (called only once)
    private void initializeCards() {
        String[] suits = {"c", "d", "h", "s"}; // c = clubs, d = diamonds, h = hearts, s = spades

        // Create cards from 3 to 14 (excluding 2 and 15)
        for (int number = 3; number < 15; number++) {
            for (String suit : suits) {
                String imagePath = "src/images/" + number + suit + ".gif"; // Image path
                Card card = new Card(number, suit, imagePath);
                pioche.addCard(imagePath, card); // Add the card to the deck
            }
        }
        // Adding joker cards
        pioche.addCard("src/images/joker1.jpeg", new Card(0, "joker1", "src/images/joker1.jpeg"));
        pioche.addCard("src/images/joker2.jpeg", new Card(0, "joker2", "src/images/joker2.jpeg"));

        // Adding special cards
        pioche.addCard("src/images/special-c.gif", new Card(20, "c", "src/images/special-c.gif"));
        pioche.addCard("src/images/special-d.gif", new Card(20, "d", "src/images/special-d.gif"));
        pioche.addCard("src/images/special-h.gif", new Card(20, "h", "src/images/special-h.gif"));
        pioche.addCard("src/images/special-s.gif", new Card(20, "s", "src/images/special-s.gif"));

        // Creating a copy of the cards in the deck
        for (Card card : pioche.getDrawCards().values()) {
            cardsCopy.add(card);
        }
    }


    /**
     * Method to remove a card from the deck by its image path.
     *
     * @param imagePath The image path of the card to be removed.
     */
    public void removeCard(String imagePath) {
        pioche.removeCard(imagePath);
    }

    /**
     * Method to draw a random card from the deck.
     * If the deck is empty, it will reload the deck from the discarded cards.
     *
     * @return A random card from the deck.
     */
    public Card getRandomCard() {

        // Check if the deck is empty
        if (pioche.isEmpty()) {
            reloadDrawPileFromDiscard(cardsCopy); // Reload the deck if it is empty
        }

        // Create a list of keys (image paths)
        keys = new ArrayList<>(pioche.getDrawCards().keySet());

        // Select a random index
        Random rand = new Random();
        int randomIndex = rand.nextInt(keys.size());

        // Get the image path corresponding to the random index
        String randomKey = keys.get(randomIndex);

        // Retrieve the card associated with the selected key
        Card randomCard = pioche.getDrawCards().get(randomKey);

        // Remove the card from the deck
        pioche.removeCard(randomKey);

        return randomCard;
    }

    /**
     * Method to reload the deck from the discarded cards.
     *
     * @param discardedCards A list of discarded cards to reload into the deck.
     */
    public void reloadDrawPileFromDiscard(ArrayList<Card> discardedCards) {
        for (Card card : discardedCards) {
            String imagePath = card.getImagePath();
            pioche.addCard(imagePath, card);
        }
    }
}
