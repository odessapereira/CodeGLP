package data.cards;

import java.util.List;

public class Bomb extends Combinaison{
    public Bomb(List<Card> cards) {
        super(cards);
    }

    @Override
    public boolean isValid() {
        if (getCards().size() < 4) return false; // A bomb must have at least 4 cards

        int value = getCards().get(0).getNumber();
        for (Card card : getCards()) {
            if (card.getNumber() != value) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Bomb: " + getCards();
    }
}


