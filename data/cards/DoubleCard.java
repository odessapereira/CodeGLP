package data.cards;

import java.util.ArrayList;

public class DoubleCard extends Combinaison{

    public DoubleCard (ArrayList<Card> cards) {
        super(cards);

    }


    @Override
    public boolean isValid() {
        if (getCards().size() != 2) {
            return false;
        }

        Card card1 = getCards().get(0);
        Card card2 = getCards().get(1);

        // Vérification si une des cartes est un joker
        boolean isCard1Joker = card1.isJoker(card1);
        boolean isCard2Joker = card2.isJoker(card2);

        // Un double est valide si :
        // - Les deux cartes ont le même numéro
        // - L'une des cartes est un joker
        return card1.getNumber() == card2.getNumber() || isCard1Joker || isCard2Joker;
    }


}
