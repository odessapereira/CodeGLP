package data.cards;

import java.util.List;

public class Serie extends Combinaison {

    public Serie(List<Card> cards) {
        super(cards);
    }

    @Override
    public boolean isValid() {
        if (getCards() == null || getCards().size() < 3) return false; // Vérifie que la liste n'est pas nulle et qu'il y a au moins 3 cartes

        // Tri des cartes par numéro croissant
        getCards().sort((c1, c2) -> Integer.compare(c1.getNumber(), c2.getNumber()));

        int previousNumber = getCards().get(0).getNumber();

        for (int i = 1; i < getCards().size(); i++) {
            Card currentCard = getCards().get(i);

            // Vérifie que les numéros des cartes sont consécutifs
            if (currentCard.getNumber() != previousNumber + 1) {
                return false;
            }

            // Mise à jour du numéro précédent pour la prochaine comparaison
            previousNumber = currentCard.getNumber();
        }

        // Si tout est valide, retourne true
        return true;
    }

    @Override
    public String toString() {
        return "Série: " + getCards();
    }
}
