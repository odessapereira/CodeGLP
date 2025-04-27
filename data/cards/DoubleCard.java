package data.cards;

import java.util.ArrayList;

/**
 * Represents a "Double" combination of cards in the game.
 * A "Double" consists of exactly two cards with the same number or one joker card.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public class DoubleCard extends Combinaison {

    /**
     * Constructor for the DoubleCard class.
     * Initializes the combination with a list of cards.
     *
     * @param cards List of cards representing the combination.
     */
    public DoubleCard(ArrayList<Card> cards) {
        super(cards);
    }

    /**
     * Checks if the combination of cards is valid as a Double.
     *
     * A valid Double must meet the following criteria:
     * - Contain exactly 2 cards.
     * - The cards must have the same number, or one of them must be a joker.
     * - Special cards (with number 2) are not allowed.
     *
     * @return true if the combination is valid, otherwise false.
     */
    @Override
    public boolean isValid() {
        // The combination must contain exactly 2 cards.
        if (getCards().size() != 2) {
            return false;
        }

        Card card1 = getCards().get(0);
        Card card2 = getCards().get(1);

        // Check if either card is a joker or special.
        boolean isCard1Joker = card1.isJoker();
        boolean isCard2Joker = card2.isJoker();
        boolean isSpecialCard1 = card1.isSpecial();
        boolean isSpecialCard2 = card2.isSpecial();

        // A Double cannot contain special cards (cards with number 2).
        if (isSpecialCard1 || isSpecialCard2) {
            return false;
        }

        // A valid Double is:
        // - Either both cards have the same number.
        // - Or one of the cards is a joker.
        // - No special cards allowed.
        return card1.getNumber() == card2.getNumber() || isCard1Joker || isCard2Joker;
    }

    /**
     * Returns the value of the Double combination.
     * The value is determined by the number on the card.
     * If one of the cards is a joker, the value of the other card is used.
     *
     * @return The value of the Double (the number of the cards).
     */
    @Override
    public int getValue() {
        Card card1 = getCards().get(0);
        Card card2 = getCards().get(1);

        // Check if either card is a joker.
        boolean isCard1Joker = card1.isJoker();
        boolean isCard2Joker = card2.isJoker();

        // If one card is a joker, return the value of the other card.
        if (isCard1Joker) return card2.getNumber();
        if (isCard2Joker) return card1.getNumber();

        // If neither card is a joker, return the value of one of the cards (both are identical).
        return card1.getNumber();
    }
}
