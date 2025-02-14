package data.cards;

import java.util.List;
import java.util.Collections;

public class Serie extends Combinaison {

    public Serie(List<Card> cards) {
        super(cards);
    }

    @Override
    public boolean isValid() {
        if (getCards().size() < 3) return false; // At least 3 cards required

        String color = getCards().get(0).getColor();
        int previousNumber = getCards().get(0).getNumber();

        for (int i = 1; i < getCards().size(); i++) {
            Card currentCard = getCards().get(i);
            if (!currentCard.getColor().equals(color) || currentCard.getNumber() != previousNumber + 1) {
                return false;
            }
            previousNumber = currentCard.getNumber(); // Move to the next card
        }
        return true;
    }

    @Override
    public String toString() {
        return "Series: " + getCards();
    }
}
