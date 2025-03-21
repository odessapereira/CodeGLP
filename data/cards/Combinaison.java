package data.cards;

import java.util.ArrayList;

public abstract class Combinaison {
    private ArrayList<Card> cards;

    public Combinaison(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public abstract boolean isValid();  // À implémenter dans les sous-classes


}
