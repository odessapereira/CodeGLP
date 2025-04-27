package engine;

import data.cards.*;
import data.players.Hand;

import java.util.*;

/**
 * This class handles the process of determining and creating valid moves (combinations) for the bots,
 * including bombs, series, doubles, and individual cards. It is responsible for evaluating the best possible move
 * a bot can make from its hand based on the game state.
 *
 * It provides methods to check for the existence of a valid bomb, series, or other combinations and to
 * select the best possible move to play.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */

public class CombinaisonProcess {
    private Hand hand;

    /**
     * Constructor for CombinaisonProcess.
     *
     * Initializes the combination process with the given hand.
     *
     * @param hand The bot's hand of cards.
     */
    public CombinaisonProcess(Hand hand) {
        this.hand = hand;
    }


    /**
     * Retrieves the bot's hand.
     *
     * @return The hand of cards.
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Checks if the hand contains a valid bomb.
     *
     * @return True if a valid bomb exists, false otherwise.
     */
    public boolean hasBomb() {
        // Iterate through all possible combinations in the hand
        for (int i = 0; i < hand.getCards().size(); i++) {
            for (int j = i + 1; j < hand.getCards().size(); j++) {
                for (int k = j + 1; k < hand.getCards().size(); k++) {
                    ArrayList<Card> combo = new ArrayList<>();
                    combo.add(hand.getCards().get(i));
                    combo.add(hand.getCards().get(j));
                    combo.add(hand.getCards().get(k));

                    // Create a bomb with the combination of cards
                    Bomb bomb = new Bomb(combo);

                    // Check if the bomb is valid
                    if (bomb.isValid()) {
                        return true;  // A valid bomb was found
                    }
                }
            }
        }

        // If no valid bomb is found, return false
        return false;
    }

    /**
     * Retrieves the next valid bomb that can beat the last played combination.
     *
     * @param lastCombinaison The last played combination.
     * @return The next valid bomb, or an empty bomb if none is found.
     */
    public Bomb getNextBomb(Combinaison lastCombinaison) {
        // If the last combination is not a bomb, no special handling is needed
        Bomb lastBomb = (lastCombinaison instanceof Bomb) ? (Bomb) lastCombinaison : null;
        int lastValue = (lastBomb != null) ? lastBomb.getValue() : -1; // -1 if no bomb
        int lastSize = (lastBomb != null) ? lastBomb.getCards().size() : 0;   // 0 if no bomb

        Bomb bestValidBomb = null;

        // Map to group cards by value (excluding jokers and cards with value 20)
        Map<Integer, List<Card>> cardsByValue = new HashMap<>();
        List<Card> jokers = new ArrayList<>();

        // Populate the map and collect jokers, ignoring cards with value 20
        for (Card card : hand.getCards()) {
            if (card.isJoker()) {
                jokers.add(card);
            } else if (card.getNumber() != 20) { // Exclude cards with value 20
                Integer value = card.getNumber();
                if (!cardsByValue.containsKey(value)) {
                    cardsByValue.put(value, new ArrayList<>());
                }
                cardsByValue.get(value).add(card);
            }
        }

        int jokerCount = Math.min(jokers.size(), 1); // Limit to 1 joker maximum

        // Examine each card value to find a valid bomb
        for (Map.Entry<Integer, List<Card>> entry : cardsByValue.entrySet()) {
            Integer value = entry.getKey();
            List<Card> cards = entry.getValue();

            // Skip values that cannot beat the last bomb (if it exists)
            if (lastBomb != null && (value < lastValue || (value == lastValue && cards.size() + jokerCount <= lastSize))) {
                continue;
            }

            // Calculate the maximum possible size of the bomb with this value (max 1 joker)
            int totalCards = cards.size() + jokerCount;
            if (totalCards >= 3) { // A bomb must have at least 3 cards
                int cardsToUse = Math.max(3, Math.min(totalCards, lastSize + 1)); // At least 3, better than lastSize if possible
                int jokersToUse = Math.min(cardsToUse - cards.size(), jokerCount); // 0 or 1 joker

                // Create the list of cards for the bomb
                ArrayList<Card> bombCards = new ArrayList<>(cards);
                if (jokersToUse > 0 && !jokers.isEmpty()) {
                    bombCards.add(jokers.get(0)); // Add a single joker if necessary
                }

                Bomb currentBomb = new Bomb(bombCards);
                if (currentBomb.isValid() &&
                        (lastBomb == null ||
                                currentBomb.getCards().size() > lastSize ||
                                (currentBomb.getCards().size() == lastSize && value > lastValue))) {
                    if (bestValidBomb == null ||
                            currentBomb.getCards().size() > bestValidBomb.getCards().size() ||
                            (currentBomb.getCards().size() == bestValidBomb.getCards().size() && value > bestValidBomb.getValue())) {
                        bestValidBomb = currentBomb;
                    }
                }
            }
        }

        // Return the best valid bomb or an empty bomb
        return bestValidBomb != null ? bestValidBomb : new Bomb(new ArrayList<>());
    }

    /**
     * Retrieves the best bomb in the hand.
     *
     * @return The best valid bomb, or an empty bomb if none is found.
     */
    public Bomb getBestBomb() {
        Bomb bestBomb = new Bomb(null);

        // Map to group cards by value
        Map<Integer, List<Card>> cardsByValue = new HashMap<>();

        // Populate the map, ignoring cards with value 20
        for (Card card : hand.getCards()) {
            if (!card.isJoker() && card.getNumber() != 20) { // Ignore jokers and cards with value 20
                Integer value = card.getNumber();
                if (!cardsByValue.containsKey(value)) {
                    cardsByValue.put(value, new ArrayList<>());
                }
                cardsByValue.get(value).add(card);
            }
        }

        // Count available jokers
        int jokerCount = 0;
        for (Card card : hand.getCards()) {
            if (card.isJoker()) {
                jokerCount++;
            }
        }

        // For each card value (except 20), check if a bomb can be formed
        for (Map.Entry<Integer, List<Card>> entry : cardsByValue.entrySet()) {
            List<Card> cardsOfSameValue = entry.getValue();

            // If a bomb can be formed (minimum 3 cards)
            if (cardsOfSameValue.size() >= 3 || (cardsOfSameValue.size() + jokerCount >= 3)) {
                // Calculate the total number of cards possible for the bomb
                int totalCards = cardsOfSameValue.size() + jokerCount;
                int cardsToUse = Math.max(3, Math.min(totalCards, cardsOfSameValue.size() + jokerCount)); // At least 3, up to max available
                int jokersToUse = cardsToUse - cardsOfSameValue.size(); // Number of jokers needed

                // Create a list for the bomb
                ArrayList<Card> bombCards = new ArrayList<>(cardsOfSameValue);

                // Add necessary jokers
                int jokersAdded = 0;
                for (Card card : hand.getCards()) {
                    if (card.isJoker() && jokersAdded < jokersToUse) {
                        bombCards.add(card);
                        jokersAdded++;
                    }
                }

                // Create the bomb
                Bomb currentBomb = new Bomb(bombCards);

                // Check if this bomb is valid
                if (currentBomb.isValid()) {
                    // If this is the first valid bomb
                    if (bestBomb == null) {
                        bestBomb = currentBomb;
                    }
                    // If this bomb has more cards than the current best
                    else if (currentBomb.getCards().size() > bestBomb.getCards().size()) {
                        bestBomb = currentBomb;
                    }
                    // If same number of cards, compare the value
                    else if (currentBomb.getCards().size() == bestBomb.getCards().size() &&
                            currentBomb.getValue() > bestBomb.getValue()) {
                        bestBomb = currentBomb;
                    }
                }
            }
        }

        // Return the best bomb or an empty bomb if none is found
        return bestBomb != null ? bestBomb : new Bomb(new ArrayList<>());
    }

    /**
     * Retrieves the weakest bomb in the hand that does not include a card with value 20.
     *
     * @return The weakest valid bomb, or an empty bomb if none is found.
     */
    public Bomb getWeakestBombWithout2() {
        Bomb weakestBomb = null;

        // Group cards by value (excluding 20)
        Map<Integer, List<Card>> cardsByValue = new HashMap<>();

        for (Card card : hand.getCards()) {
            if (!card.isJoker() && card.getNumber() != 20) {
                cardsByValue.putIfAbsent(card.getNumber(), new ArrayList<>());
                cardsByValue.get(card.getNumber()).add(card);
            }
        }

        // Count jokers
        int jokerCount = 0;
        for (Card card : hand.getCards()) {
            if (card.isJoker()) {
                jokerCount++;
            }
        }

        // Search for the weakest possible bomb
        for (Map.Entry<Integer, List<Card>> entry : cardsByValue.entrySet()) {
            List<Card> cardsOfSameValue = entry.getValue();
            int value = entry.getKey();

            if (cardsOfSameValue.size() >= 3 || cardsOfSameValue.size() + jokerCount >= 3) {
                int totalCards = cardsOfSameValue.size() + jokerCount;
                int cardsToUse = Math.max(3, Math.min(totalCards, 4)); // Limit to 4 max for consistency
                int jokersToUse = cardsToUse - cardsOfSameValue.size();

                ArrayList<Card> bombCards = new ArrayList<>(cardsOfSameValue);

                int jokersAdded = 0;
                for (Card card : hand.getCards()) {
                    if (card.isJoker() && jokersAdded < jokersToUse) {
                        bombCards.add(card);
                        jokersAdded++;
                    }
                }

                Bomb currentBomb = new Bomb(bombCards);

                if (currentBomb.isValid()) {
                    if (weakestBomb == null) {
                        weakestBomb = currentBomb;
                    } else if (currentBomb.getCards().size() < weakestBomb.getCards().size()) {
                        weakestBomb = currentBomb;
                    } else if (currentBomb.getCards().size() == weakestBomb.getCards().size() &&
                            currentBomb.getValue() < weakestBomb.getValue()) {
                        weakestBomb = currentBomb;
                    }
                }
            }
        }

        return weakestBomb != null ? weakestBomb : new Bomb(new ArrayList<>());
    }


    /**
     * Checks if the hand contains a valid series.
     *
     * @return True if a valid series exists, false otherwise.
     */
    public boolean hasSerie() {
        List<Card> cards = hand.getCards();

        // Check if there are enough cards for a series (at least 3)
        if (cards.size() < 3) {
            return false;
        }

        // Identify the joker (only one)
        Card jokerCard = null;
        int nonJokerCount = 0;
        for (Card card : cards) {
            if (card.isJoker()) {
                if (jokerCard == null) { // Take the first joker only
                    jokerCard = card;
                }
            } else {
                nonJokerCount++;
            }
        }
        boolean hasJoker = jokerCard != null;
        if (nonJokerCount + (hasJoker ? 1 : 0) < 3) {
            return false;
        }

        // Iterate through each card as a potential starting point
        for (int start = 0; start < cards.size(); start++) {
            if (cards.get(start).isJoker()) {
                continue; // Do not start with a joker alone
            }

            List<Card> serieCandidate = new ArrayList<>();
            int currentValue = cards.get(start).getNumber();
            serieCandidate.add(cards.get(start));
            int jokerUsed = 0;

            // Look for subsequent cards in the hand
            for (int i = 0; i < cards.size(); i++) {
                if (i <= start || serieCandidate.contains(cards.get(i))) {
                    continue; // Skip already used cards or those before the start
                }

                Card nextCard = cards.get(i);
                if (nextCard.getNumber() == currentValue + 1) {
                    serieCandidate.add(nextCard);
                    currentValue++;
                } else if (hasJoker && jokerUsed == 0 && !serieCandidate.contains(jokerCard)) {
                    // Use the joker to fill a gap
                    serieCandidate.add(jokerCard);
                    jokerUsed++;
                    currentValue++;
                    i--; // Go back to check the next card
                }

                // Check if a valid series is found
                if (serieCandidate.size() >= 3) {
                    Serie serie = new Serie(new ArrayList<>(serieCandidate));
                    if (serie.isValid()) {
                        return true;
                    }
                }
            }
        }

        return false; // No valid series found
    }

    /**
     * Retrieves the next valid series that can beat the last played series.
     *
     * @param lastCombinaison The last played combination.
     * @return The next valid series, or an empty series if none is found.
     */
    public Serie getNextSerie(Combinaison lastCombinaison) {
        ArrayList<Card> emptyList = new ArrayList<>();
        Serie emptySerie = new Serie(emptyList); // Return an empty series by default

        if (!(lastCombinaison instanceof Serie)) {
            return emptySerie;
        }

        Serie lastSerie = (Serie) lastCombinaison;

        if (!lastSerie.isValid() || lastSerie.getCards().size() < 3) {
            return emptySerie;
        }

        ArrayList<Card> lastCards = new ArrayList<>(lastSerie.getCards());
        Collections.sort(lastCards, Comparator.comparing(Card::getNumber)); // Sort for analysis

        int startValue = lastSerie.getValue(); // The value returned by getValue()
        ArrayList<Integer> allowedStartValues = new ArrayList<>();

        // Start from the second card (index 1) to the end
        for (int i = 1; i < lastCards.size(); i++) {
            Card card = lastCards.get(i);
            if (!card.isJoker()) {
                allowedStartValues.add(card.getNumber());
            } else if (i > 1) {
                // Add the next value to the previous one if it's a joker (approximate case)
                Card prevCard = lastCards.get(i - 1);
                if (!prevCard.isJoker()) {
                    allowedStartValues.add(prevCard.getNumber() + 1);
                }
            }
        }

        // Retrieve the cards from the hand
        ArrayList<Card> handCards = new ArrayList<>(hand.getCards());
        ArrayList<Card> jokers = new ArrayList<>();
        ArrayList<Card> nonJokerCards = new ArrayList<>();
        for (Card card : handCards) {
            if (card.isJoker()) {
                jokers.add(card);
            } else {
                nonJokerCards.add(card);
            }
        }

        // Test a series starting from each allowed value
        for (int start : allowedStartValues) {
            ArrayList<Card> serieCandidate = new ArrayList<>();
            int value = start;
            int jokersUsed = 0;

            while (serieCandidate.size() < 3 && value <= 13) {
                boolean found = false;
                for (Card card : nonJokerCards) {
                    if (!serieCandidate.contains(card) && card.getNumber() == value) {
                        serieCandidate.add(card);
                        found = true;
                        break;
                    }
                }
                if (!found && jokersUsed < jokers.size()) {
                    serieCandidate.add(jokers.get(jokersUsed));
                    jokersUsed++;
                    found = true;
                }
                if (!found) {
                    break;
                }
                value++;
            }

            if (serieCandidate.size() == 3) {
                Serie candidate = new Serie(new ArrayList<>(serieCandidate));
                if (candidate.isValid()) {
                    return candidate;
                }
            }
        }

        return emptySerie;
    }



    /**
     * Retrieves the best series in the hand.
     *
     * @return The best valid series, or an empty series if none is found.
     */
    public Serie getBestSerie() {
        Serie bestSerie = new Serie(null);

        // Separate normal cards and jokers
        List<Card> normalCards = new ArrayList<>();
        List<Card> jokers = new ArrayList<>();
        for (Card card : hand.getCards()) {
            if (card.isJoker()) {
                jokers.add(card);
            } else {
                normalCards.add(card);
            }
        }

        // Sort normal cards by ascending value
        Collections.sort(normalCards, Comparator.comparing(Card::getNumber));

        // Test all possible series using normal cards and jokers
        for (int i = 0; i <= normalCards.size(); i++) {
            // For each possible starting point, try to build a series
            for (int startValue = 3; startValue <= 20; startValue++) { // Possible values for the start of the series
                List<Card> serieCandidate = new ArrayList<>();
                int jokersUsed = 0;
                int currentValue = startValue;
                int normalCardIndex = i;

                // Build a series by adding normal cards or jokers
                while ((normalCardIndex < normalCards.size() || jokersUsed < jokers.size()) && serieCandidate.size() < normalCards.size() + jokers.size()) {
                    // Check if a normal card matches the current value
                    if (normalCardIndex < normalCards.size() && normalCards.get(normalCardIndex).getNumber() == currentValue) {
                        serieCandidate.add(normalCards.get(normalCardIndex));
                        normalCardIndex++;
                        currentValue++;
                    }
                    // Otherwise, use a joker if available
                    else if (jokersUsed < jokers.size()) {
                        serieCandidate.add(jokers.get(jokersUsed));
                        jokersUsed++;
                        currentValue++;
                    }
                    // If neither normal card nor joker, stop building
                    else {
                        break;
                    }

                    // Check if the candidate series is valid (at least 3 cards)
                    if (serieCandidate.size() >= 3) {
                        Serie newSerie = new Serie(new ArrayList<>(serieCandidate));
                        if (newSerie.isValid()) {
                            // Update the best series if it is larger
                            if (bestSerie == null || newSerie.getCards().size() > bestSerie.getCards().size()) {
                                bestSerie = newSerie;
                            }
                        }
                    }
                }
            }
        }

        return bestSerie; // Return the best series found or null if none is valid
    }

    /**
     * Retrieves the shortest series with the lowest starting value.
     *
     * @return The shortest valid series with the lowest starting value, or null if none is found.
     */
    public Serie getShortestLowestSerie() {
        Serie bestSerie = null;

        // Sort cards by ascending value
        List<Card> sortedCards = new ArrayList<>(hand.getCards());
        Collections.sort(sortedCards, Comparator.comparing(Card::getNumber));

        // Try to find all valid series with at least 3 cards
        for (int i = 0; i < sortedCards.size(); i++) {
            List<Card> serieCandidate = new ArrayList<>();
            serieCandidate.add(sortedCards.get(i));

            int nextValue = sortedCards.get(i).getNumber() + 1;

            for (int j = i + 1; j < sortedCards.size(); j++) {
                Card currentCard = sortedCards.get(j);

                if (currentCard.getNumber() == nextValue) {
                    serieCandidate.add(currentCard);
                    nextValue++;

                    // Check if we have at least 3 cards to form a valid series
                    if (serieCandidate.size() >= 3) {
                        Serie newSerie = new Serie(new ArrayList<>(serieCandidate));
                        if (newSerie.isValid()) {
                            if (bestSerie == null
                                    || newSerie.getCards().size() < bestSerie.getCards().size()
                                    || (newSerie.getCards().size() == bestSerie.getCards().size()
                                    && newSerie.getCards().get(0).getNumber() < bestSerie.getCards().get(0).getNumber())) {
                                bestSerie = newSerie;
                            }
                        }
                    }
                } else if (currentCard.getNumber() > nextValue) {
                    // If a value is skipped, the series is broken
                    break;
                }
            }
        }

        return bestSerie;
    }

    /**
     * Checks if the hand contains a valid double.
     *
     * @return True if a valid double exists, false otherwise.
     */
    public boolean hasDouble() {
        // Iterate through all possible card combinations to check for a valid double
        for (int i = 0; i < hand.getCards().size(); i++) {
            for (int j = i + 1; j < hand.getCards().size(); j++) {
                ArrayList<Card> combo = new ArrayList<>();
                combo.add(hand.getCards().get(i));
                combo.add(hand.getCards().get(j));

                // Create a double with the combination of cards
                DoubleCard aDouble = new DoubleCard(combo);

                // Check if the double is valid
                if (aDouble.isValid()) {
                    return true; // A valid double was found
                }
            }
        }

        return false; // No valid double found
    }

    /**
     * Retrieves the next valid double that can beat the last played double.
     *
     * @param lastDoubleCard The last played double card.
     * @return The next valid double, or an empty double if none is found.
     */
    public DoubleCard getNextDouble(DoubleCard lastDoubleCard) {
        // If nothing is played or invalid, return an empty DoubleCard
        if (lastDoubleCard == null || !lastDoubleCard.isValid()) {
            return new DoubleCard(null);
        }

        // Required value = next value
        int requiredValue = lastDoubleCard.getValue() + 1;

        // Count cards with the required value and jokers
        List<Card> matchingCards = new ArrayList<>();
        List<Card> jokers = new ArrayList<>();
        for (Card card : hand.getCards()) {
            if (card.isJoker()) {
                jokers.add(card);
            } else if (card.getNumber() == requiredValue) {
                matchingCards.add(card);
            }
        }

        // Case 1: 2 cards with the required value
        if (matchingCards.size() >= 2) {
            return new DoubleCard(new ArrayList<>(matchingCards.subList(0, 2)));
        }

        // Case 2: 1 card + 1 joker
        if (matchingCards.size() == 1 && !jokers.isEmpty()) {
            ArrayList<Card> doubleCards = new ArrayList<>();
            doubleCards.add(matchingCards.get(0));
            doubleCards.add(jokers.get(0));
            return new DoubleCard(doubleCards);
        }

        // Otherwise, nothing valid
        return new DoubleCard(null);
    }

    /**
     * Retrieves the best double in the hand.
     *
     * @return The best valid double, or an empty double if none is found.
     */
    public DoubleCard getBestDouble() {
        Map<Integer, List<Card>> cardsByValue = new HashMap<>();
        List<Card> jokers = new ArrayList<>();

        // Separate jokers and normal cards
        for (Card card : hand.getCards()) {
            if (card.isJoker()) {
                jokers.add(card);
            } else {
                cardsByValue.putIfAbsent(card.getNumber(), new ArrayList<>());
                cardsByValue.get(card.getNumber()).add(card);
            }
        }

        int bestNaturalValue = -1;
        int bestJokerValue = -1;

        // Case 1: Natural double (without joker), ignoring cards with value 20
        for (int value : cardsByValue.keySet()) {
            if (value != 20 && cardsByValue.get(value).size() >= 2 && value > bestNaturalValue) {
                bestNaturalValue = value;
            }
        }

        // Case 2: Double with one joker + one non-20 card
        if (!jokers.isEmpty()) {
            for (int value : cardsByValue.keySet()) {
                if (value != 20 && cardsByValue.get(value).size() >= 1 && value > bestJokerValue) {
                    bestJokerValue = value;
                }
            }
        }

        // Choose the best double (natural or with joker) based on value
        if (bestNaturalValue >= bestJokerValue && bestNaturalValue != -1) {
            return new DoubleCard(new ArrayList<>(cardsByValue.get(bestNaturalValue).subList(0, 2)));
        } else if (bestJokerValue != -1) {
            ArrayList<Card> doubleCards = new ArrayList<>();
            doubleCards.add(cardsByValue.get(bestJokerValue).get(0));
            doubleCards.add(jokers.get(0));
            return new DoubleCard(doubleCards);
        }

        return new DoubleCard(null);
    }

    /**
     * Retrieves the lowest double in the hand.
     *
     * @return The lowest valid double, or an empty double if none is found.
     */
    public DoubleCard getLowestDouble() {
        // Lists for cards by value and jokers
        Map<Integer, List<Card>> cardsByValue = new HashMap<>();
        List<Card> jokers = new ArrayList<>();

        // Sort cards: jokers separated, others by value
        for (Card card : hand.getCards()) {
            if (card.isJoker()) {
                jokers.add(card);
            } else {
                cardsByValue.putIfAbsent(card.getNumber(), new ArrayList<>());
                cardsByValue.get(card.getNumber()).add(card);
            }
        }

        // Find the smallest value with at least 2 cards
        int lowestValue = Integer.MAX_VALUE;
        for (int value : cardsByValue.keySet()) {
            if (cardsByValue.get(value).size() >= 2 && value < lowestValue) {
                lowestValue = value;
            }
        }

        // Case 1: If a double without joker is found
        if (lowestValue != Integer.MAX_VALUE) {
            return new DoubleCard(new ArrayList<>(cardsByValue.get(lowestValue).subList(0, 2)));
        }

        // Case 2: Look for a double with 1 card + 1 joker
        if (!jokers.isEmpty()) {
            lowestValue = Integer.MAX_VALUE;
            for (int value : cardsByValue.keySet()) {
                if (cardsByValue.get(value).size() >= 1 && value < lowestValue) {
                    lowestValue = value;
                }
            }
            if (lowestValue != Integer.MAX_VALUE) {
                ArrayList<Card> doubleCards = new ArrayList<>();
                doubleCards.add(cardsByValue.get(lowestValue).get(0));
                doubleCards.add(jokers.get(0));
                return new DoubleCard(doubleCards);
            }
        }

        // No double found
        return new DoubleCard(null);
    }

    /**
     * Checks if the hand contains a double joker.
     *
     * @return True if at least two jokers are in the hand, false otherwise.
     */
    public boolean hasDoubleJoker() {
        // Check if there are at least two jokers in the hand
        int jokerCount = 0;

        for (Card card : hand.getCards()) {
            if (card.isJoker()) { // Assumes Card has an isJoker() method
                jokerCount++;
                if (jokerCount == 2) {
                    return true; // Two jokers found, a double joker is possible
                }
            }
        }
        return false; // Not enough jokers to form a double
    }

    /**
     * Retrieves the double joker from the hand.
     *
     * @return A list containing the two jokers, or an empty list if not available.
     */
    public ArrayList<Card> getDoubleJoker() {
        ArrayList<Card> jokers = new ArrayList<>();

        // Retrieve the two jokers from the hand
        for (Card card : hand.getCards()) {
            if (card.isJoker()) {
                jokers.add(card);
            }
        }

        return jokers; // Return the list containing the double joker (or empty if no double)
    }

    /**
     * Checks if the given cards form a double joker.
     *
     * @param cards The cards to check.
     * @return True if the cards are exactly two jokers, false otherwise.
     */
    public boolean isDoubleJoker(ArrayList<Card> cards) {
        int jokerCount = 0;

        for (Card card : cards) {
            if (card.isJoker()) { // Assumes Card has an isJoker() method
                jokerCount++;
            }
        }

        return jokerCount == 2 && cards.size() == 2;
    }

    /**
     * Retrieves the highest non-joker card in the hand.
     *
     * @return The highest non-joker card, or null if none exists.
     */
    public Card getHighestCard() {
        // Initialize the highest card to null
        Card highestCard = null;

        // Iterate through all cards in the hand
        for (Card card : hand.getCards()) {
            // Ignore jokers
            if (!card.isJoker()) {
                // If highestCard is null or the current card has a higher value
                if (highestCard == null || card.getNumber() > highestCard.getNumber()) {
                    highestCard = card;
                }
            }
        }

        return highestCard; // Return the non-joker card with the highest number, or null if no valid card
    }

    /**
     * Retrieves the next higher card that beats the last played card.
     *
     * @param lastPlayedCard The last played card.
     * @return The next higher non-joker card, or null if none exists.
     */
    public Card getNextHigherCard(Card lastPlayedCard) {
        // Check that the last played card is not null
        if (lastPlayedCard == null) {
            return null;
        }

        // Value of the last played card
        int lastValue = lastPlayedCard.getNumber();
        int requiredValue = lastValue + 1;

        // Look for the card with exactly the next value
        Card nextCard = null;

        for (Card card : hand.getCards()) {
            // Check that the card has exactly the required value and is not a joker
            if (card.getNumber() == requiredValue && !card.isJoker()) {
                // If multiple cards have the same value, take the one with the highest suit
                if (nextCard == null) {
                    nextCard = card;
                }
            }
        }

        return nextCard; // Return the found card or null if no valid card
    }


    /**
     * Checks if the hand contains a special card (value 20).
     *
     * @return True if a special card exists, false otherwise.
     */
    public boolean hasSpecialCard() {
        // Check if there is at least one card with value 20 in the hand
        for (Card card : hand.getCards()) {
            if (card.getNumber() == 20) {
                return true; // A special card with value 20 was found
            }
        }
        return false; // No special card with value 20 found
    }

    /**
     * Retrieves a special card (value 20) from the hand.
     *
     * @return The first special card found, or null if none exists.
     */
    public Card getSpecialCard() {
        for (Card card : hand.getCards()) {
            if (card.getNumber() == 20) {
                return card; // Return the first card found with value 20
            }
        }
        return null; // Return null if no card with value 20 is found
    }

    /**
     * Checks if the hand contains a simple card (non-joker, non-special).
     *
     * @return True if a simple card exists, false otherwise.
     */
    public boolean hasSimple() {
        for (Card card : hand.getCards()) {
            if (!card.isJoker() && card.getNumber() != 20) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the lowest non-joker card in the hand.
     *
     * @return The lowest non-joker card, or null if none exists.
     */
    public Card getLowestCard() {
        // Initialize the lowest card to null
        Card lowestCard = null;

        // Iterate through all cards in the hand
        for (Card card : hand.getCards()) {
            // Ignore jokers
            if (!card.isJoker()) {
                // If lowestCard is null or the current card has a lower value
                if (lowestCard == null || card.getNumber() < lowestCard.getNumber()) {
                    lowestCard = card;
                }
            }
        }

        return lowestCard;
    }

    /**
     * Counts the number of special cards (value 20) in the hand.
     *
     * @return The number of special cards.
     */
    public int countSpecialCards() {
        int count = 0;

        for (Card card : hand.getCards()) {
            if (card.getNumber() == 20) { // Add other values if needed
                count++;
            }
        }
        return count;
    }

    /**
     * Finds a combination that empties the hand.
     *
     * @return A list of cards that can empty the hand, or null if no such combination exists.
     */
    public ArrayList<Card> findWinningCombination() {
        ArrayList<Card> selected = new ArrayList<>();

        // 0. Check double joker
        if (hasDoubleJoker()) {
            ArrayList<Card> doubleJoker = getDoubleJoker();
            ArrayList<Card> tempHand = new ArrayList<>(hand.getCards());
            tempHand.removeAll(doubleJoker);
            CombinaisonProcess tempCp = new CombinaisonProcess(new Hand(tempHand));
            if (canPlayAllCards(tempCp, tempHand)) {
                selected.addAll(doubleJoker);
                return selected;
            }
        }

        // 4. Check bombs
        if (hasBomb()) {
            Bomb winningBomb = getBestBomb();
            if (winningBomb != null) {
                ArrayList<Card> tempHand = new ArrayList<>(hand.getCards());
                tempHand.removeAll(winningBomb.getCards());
                CombinaisonProcess tempCp = new CombinaisonProcess(new Hand(tempHand));
                if (canPlayAllCards(tempCp, tempHand)) {
                    selected.addAll(winningBomb.getCards());
                    return selected;
                }
            }
        }

        // 2. Check doubles
        if (hasDouble() && hand.size() == 2) {
            DoubleCard lowDouble = getBestDouble();
            if (lowDouble != null) {
                ArrayList<Card> tempHand = new ArrayList<>(hand.getCards());
                tempHand.removeAll(lowDouble.getCards());
                CombinaisonProcess tempCp = new CombinaisonProcess(new Hand(tempHand));
                if (canPlayAllCards(tempCp, tempHand)) {
                    selected.addAll(lowDouble.getCards());
                    return selected;
                }
            }
        }

        // 3. Check series
        else if (hasSerie()) {
            Serie winningSerie = getBestSerie();
            if (winningSerie != null) {
                ArrayList<Card> tempHand = new ArrayList<>(hand.getCards());
                tempHand.removeAll(winningSerie.getCards());
                CombinaisonProcess tempCp = new CombinaisonProcess(new Hand(tempHand));
                if (canPlayAllCards(tempCp, tempHand)) {
                    selected.addAll(winningSerie.getCards());
                    return selected;
                }
            }
        }

        if (hasSpecialCard() && hand.size() == 1) {
            for (Card card : hand.getCards()) {
                ArrayList<Card> tempHand = new ArrayList<>(hand.getCards());
                tempHand.remove(card);
                CombinaisonProcess tempCp = new CombinaisonProcess(new Hand(tempHand));
                if (canPlayAllCards(tempCp, tempHand)) {
                    selected.add(card);
                    return selected;
                }
            }
        }

        // 1. Check simple cards
        if (hasSimple() && hand.size() == 1) {
            for (Card card : hand.getCards()) {
                ArrayList<Card> tempHand = new ArrayList<>(hand.getCards());
                tempHand.remove(card);
                CombinaisonProcess tempCp = new CombinaisonProcess(new Hand(tempHand));
                if (canPlayAllCards(tempCp, tempHand)) {
                    selected.add(card);
                    return selected;
                }
            }
        }

        return null; // No combination can empty the hand
    }

    /**
     * Checks if all remaining cards can be played in one or more valid combinations.
     *
     * @param cp The CombinaisonProcess instance for the temporary hand.
     * @param remainingCards The remaining cards to check.
     * @return True if all cards can be played, false otherwise.
     */
    private boolean canPlayAllCards(CombinaisonProcess cp, ArrayList<Card> remainingCards) {
        if (remainingCards.isEmpty()) {
            return true; // Empty hand, condition satisfied
        }

        // Special case: one card remaining and it's a special card
        if (remainingCards.size() == 1 && cp.hasSpecialCard()) {
            return true; // The special card can be played to empty the hand
        }

        // Check if the remaining cards form a valid combination
        return cp.hasSimple() || cp.hasDouble() || cp.hasSerie() || cp.hasBomb() || cp.hasDoubleJoker();
    }

    /**
     * Retrieves a middle-valued card (neither the lowest nor the highest).
     *
     * @return The middle card, or the lowest card if no simple cards exist.
     */
    public Card getMiddleCard() {
        ArrayList<Card> simples = new ArrayList<>();
        for (Card card : hand.getCards()) {
            if (!card.isSpecial()) simples.add(card);
        }
        if (simples.isEmpty()) return getLowestCard();
        return simples.get(simples.size() / 2); // Middle card
    }

    /**
     * Calculates the total value of the hand based on predefined combination values.
     *
     * @return The total value of the hand in euros.
     */
    public int calculateHandValue() {
        int totalValue = 0;
        ArrayList<Card> remainingCards = new ArrayList<>(hand.getCards());

        // Copy of the hand for temporary CombinaisonProcess calls
        Hand tempHand = new Hand(new ArrayList<>(remainingCards));
        CombinaisonProcess cp = new CombinaisonProcess(tempHand);

        // 1. Double joker (40 euros)
        if (cp.hasDoubleJoker()) {
            totalValue += 40;
            remainingCards.removeAll(cp.getDoubleJoker());
            cp = new CombinaisonProcess(new Hand(remainingCards)); // Update
        }

        // 2. Bomb (15 euros for three or more cards)
        while (cp.hasBomb() && cp.getBestBomb() != null) {
            Bomb bomb = cp.getBestBomb();
            totalValue += 15;
            remainingCards.removeAll(bomb.getCards());
            cp = new CombinaisonProcess(new Hand(remainingCards)); // Update
        }

        // 3. Single joker (15 euros)
        for (int i = 0; i < remainingCards.size(); i++) {
            if (remainingCards.get(i).isJoker()) {
                totalValue += 15;
                remainingCards.remove(i);
                break;
            }
        }

        cp = new CombinaisonProcess(new Hand(remainingCards)); // Update

        // 4. Double (10 euros for two cards)
        while (cp.hasDouble() && cp.getBestDouble() != null) {
            DoubleCard doubleCard = cp.getBestDouble();
            totalValue += 10;
            remainingCards.removeAll(doubleCard.getCards());
            cp = new CombinaisonProcess(new Hand(remainingCards)); // Update
        }

        // 5. Series (10 euros for three or more consecutive cards)
        while (cp.hasSerie() && cp.getBestSerie() != null) {
            Serie serie = cp.getBestSerie();
            totalValue += 10;
            remainingCards.removeAll(serie.getCards());
            cp = new CombinaisonProcess(new Hand(remainingCards)); // Update
        }

        // 6. Special card (e.g., 2) (10 euros)
        while (cp.hasSpecialCard()) {
            totalValue += 10;
            remainingCards.remove(cp.getSpecialCard());
            cp = new CombinaisonProcess(new Hand(remainingCards)); // Update
        }

        // 7. Remaining simple cards (2 euros each)
        for (Card card : remainingCards) {
            if (!card.isJoker()) {
                totalValue += 2;
            }
        }

        return totalValue;
    }
}
