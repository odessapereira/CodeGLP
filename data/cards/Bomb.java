package data.cards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a combination of cards forming a bomb in the game.
 * A bomb can contain jokers and cards of the same value.
 * It extends the abstract class Combinaison.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */

public class Bomb extends Combinaison{
    public Bomb(ArrayList<Card> cards) {
        super(cards);
    }


    /**
     * Checks if the combination of cards is valid as a bomb.
     *
     * A bomb must:
     * - Contain at least 3 cards (excluding jokers).
     * - Not contain special cards.
     * - Jokers can be used to complete a combination.
     *
     * @return true if the combination is valid, otherwise false.
     * Author: Nadjib-M Fariza-A Odessa-T-P
     */

    @Override
    public boolean isValid() {
        if (getCards().size() < 3) return false; // Une bombe doit contenir au moins 4 cartes

        int jokerCount = 0;
        Map<Integer, Integer> cardCounts = new HashMap<>();

        // Compter les occurrences de chaque valeur de carte et les jokers
        for (Card card : getCards()) {
            if (card.isJoker()) {
                jokerCount++;
            } else {
                cardCounts.put(card.getNumber(), cardCounts.getOrDefault(card.getNumber(), 0) + 1);
            }
        }

        for (Card card : getCards()){
            if(card.isSpecial()){
                return false;
            }
        }

        // Vérifier s'il existe une valeur qui peut former une bombe avec les jokers
        for (Map.Entry<Integer, Integer> entry : cardCounts.entrySet()) {
            if (entry.getValue() + jokerCount == getCards().size()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Calculates and returns the value of the bomb.
     * The value of the bomb is determined by the majority value of the cards.
     * If the bomb contains only jokers, a default value is used.
     *
     * @return The value of the bomb (the most frequent card value),
     *         or a default value if it contains only jokers.
     * Author: Nadjib-M Fariza-A Odessa-T-P
     */

    public int getValue() {
        Map<Integer, Integer> cardCounts = new HashMap<>();
        int jokerCount = 0;

        // Compter les occurrences de chaque valeur et les jokers
        if (!getCards().isEmpty()) {
            for (Card card : getCards()) {
                if (card.isJoker()) {
                    jokerCount++;
                } else {
                    cardCounts.put(card.getNumber(), cardCounts.getOrDefault(card.getNumber(), 0) + 1);
                }
            }
        }

        // Trouver la valeur majoritaire dans la bombe
        int maxValue = 0;
        int maxCount = 0;

        for (Map.Entry<Integer, Integer> entry : cardCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxValue = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        // Si tous les éléments sont des jokers, on leur donne une valeur par défaut (ex: 2 car plus fort)
        if (cardCounts.isEmpty() && jokerCount > 0) {
            return 2;
        }

        return maxValue;
    }



}


