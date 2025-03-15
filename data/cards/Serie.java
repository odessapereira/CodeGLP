package data.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Serie extends Combinaison {

    public Serie(ArrayList<Card> cards) {
        super(cards);
    }

    @Override
    public boolean isValid() {
        if (getCards().size() < 3) return false; // Une série doit contenir au moins 3 cartes

        // Trier les cartes par ordre croissant de numéro
        getCards().sort(Comparator.comparingInt(Card::getNumber));

        int jokerCount = 0;  // Nombre de jokers dans la série
        int previousNumber = -1; // Le numéro de la dernière carte (pour comparer)
        boolean firstCard = true;

        for (Card card : getCards()) {
            if (card.isJoker(card)) {
                jokerCount++; // Compter les jokers
                continue;
            }

            if (firstCard) {
                previousNumber = card.getNumber(); // Première carte non joker
                firstCard = false;
                continue;
            }

            int gap = card.getNumber() - previousNumber; // Différence entre les cartes

            if (gap == 0) {
                return false; // Deux cartes de même valeur ne peuvent pas être dans une série
            } else if (gap == 1) {
                previousNumber = card.getNumber(); // Séquence correcte, on continue
            } else if (gap - 1 <= jokerCount) {
                // On peut combler l'écart avec des jokers
                jokerCount -= (gap - 1);
                previousNumber = card.getNumber(); // Mise à jour de la séquence
            } else {
                return false; // Impossible de combler l'écart
            }
        }
        return true;
    }



    @Override
    public String toString() {
        return "Series: " + getCards();
    }
}
