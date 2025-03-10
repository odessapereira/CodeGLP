package data.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Serie extends Combinaison {

    public Serie(ArrayList<Card> cards) {
        super(cards);
    }

    @Override
    public boolean isValid() {
        List<Card> cards = getCards(); // Récupérer la liste des cartes

        if (cards.size() < 3) return false; // Au moins 3 cartes requises

        // Trier les cartes par ordre croissant de numéro
        Collections.sort(cards, Comparator.comparingInt(Card::getNumber));

        int expectedNumber = cards.get(0).getNumber(); // Numéro attendu (début de la séquence)

        // Vérification de la séquence
        for (int i = 1; i < cards.size(); i++) {
            Card currentCard = cards.get(i);
            if ( currentCard.getNumber() != expectedNumber + 1) {
                return false; // rupture de séquence
            }
            expectedNumber++; // Passer au prochain numéro attendu
        }

        return true; // Si toutes les conditions sont remplies, c'est une série valide
    }


    @Override
    public String toString() {
        return "Series: " + getCards();
    }
}
