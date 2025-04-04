package data.cards;

import java.util.List;

public abstract class Combinaison {
    private List<Card> cards;

    public Combinaison(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public abstract boolean isValid();  // À implémenter dans les sous-classes

    @Override
    public String toString() {
        return "Combinaison{" +
                "cards=" + cards +
                '}';
    }
}
