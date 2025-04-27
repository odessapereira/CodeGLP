package data.cards;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Represents a "Serie" combination of cards in the game.
 * A "Serie" consists of at least three cards with consecutive numbers, and jokers can fill gaps.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public class Serie extends Combinaison {

    /**
     * Constructor for the Serie class.
     * Initializes the combination with a list of cards.
     *
     * @param cards List of cards representing the combination.
     */
    public Serie(ArrayList<Card> cards) {
        super(cards);
    }

    /**
     * Checks if the combination of cards is valid as a Serie.
     *
     * A valid Serie must meet the following criteria:
     * - Contains at least 3 cards.
     * - Cards must have consecutive numbers, and jokers can fill gaps.
     * - No two cards can have the same number.
     *
     * @return true if the combination is valid, otherwise false.
     */
    @Override
    public boolean isValid() {
        // A Serie must contain at least 3 cards.
        if (getCards().size() < 3) return false;

        // Sort the cards by ascending number.
        getCards().sort(Comparator.comparingInt(Card::getNumber));

        int jokerCount = 0;  // Number of jokers in the Serie
        int previousNumber = -1; // The number of the last card (for comparison)
        boolean firstCard = true;

        for (Card card : getCards()) {
            if (card.isJoker()) {
                jokerCount++; // Count the jokers
                continue;
            }

            if (firstCard) {
                previousNumber = card.getNumber(); // First non-joker card
                firstCard = false;
                continue;
            }

            int gap = card.getNumber() - previousNumber; // Difference between cards

            if (gap == 0) {
                return false; // Two cards with the same number cannot be in a Serie
            } else if (gap == 1) {
                previousNumber = card.getNumber(); // Correct sequence, continue
            } else if (gap - 1 <= jokerCount) {
                // We can fill the gap with jokers
                jokerCount -= (gap - 1);
                previousNumber = card.getNumber(); // Update the sequence
            } else {
                return false; // Unable to fill the gap
            }
        }
        return true;
    }

    /**
     * Returns the value of the Serie combination.
     * The value is determined by the highest second value that can be formed using the available jokers.
     *
     * @return The value of the Serie (the highest second value).
     */
    @Override
    public int getValue() {
        ArrayList<Card> cards = new ArrayList<>(getCards());
        cards.sort(Comparator.comparingInt(Card::getNumber));

        int jokerCount = 0;
        ArrayList<Integer> values = new ArrayList<>();

        for (Card card : cards) {
            if (card.isJoker()) {
                jokerCount++;
            } else {
                values.add(card.getNumber());
            }
        }

        if (values.size() + jokerCount < 2) return 0;

        int maxSecondValue = Integer.MIN_VALUE;

        for (int shift = -jokerCount; shift <= jokerCount; shift++) {
            ArrayList<Integer> simulated = new ArrayList<>(values);
            for (int i = 0; i < jokerCount; i++) {
                int insertValue = values.get(0) - 1 - i;
                if (insertValue > 0 && !simulated.contains(insertValue)) {
                    simulated.add(insertValue);
                }
            }

            simulated.sort(Integer::compareTo);

            int jokersLeft = jokerCount;
            int previous = simulated.get(0);
            int second = -1;
            boolean valid = true;

            for (int i = 1; i < simulated.size(); i++) {
                int current = simulated.get(i);
                int gap = current - previous;

                if (gap == 0) {
                    valid = false;
                    break;
                } else if (gap == 1) {
                    if (second == -1) second = current;
                } else if (gap - 1 <= jokersLeft) {
                    jokersLeft -= (gap - 1);
                    if (second == -1) second = previous + 1;
                } else {
                    valid = false;
                    break;
                }

                previous = current;
            }

            if (valid && second != -1) {
                maxSecondValue = Math.max(maxSecondValue, second);
            }
        }

        return maxSecondValue == Integer.MIN_VALUE ? 0 : maxSecondValue;
    }
}
