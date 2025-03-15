package data.cards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Bomb extends Combinaison{
    public Bomb(ArrayList<Card> cards) {
        super(cards);
    }

    @Override
    public boolean isValid() {
        if (getCards().size() < 4) return false; // Une bombe doit contenir au moins 4 cartes

        int jokerCount = 0;
        Map<Integer, Integer> cardCounts = new HashMap<>();

        // Compter les occurrences de chaque valeur de carte et les jokers
        for (Card card : getCards()) {
            if (card.isJoker(card)) {
                jokerCount++;
            } else {
                cardCounts.put(card.getNumber(), cardCounts.getOrDefault(card.getNumber(), 0) + 1);
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

    @Override
    public String toString() {
        return "Bomb: " + getCards();
    }
}


