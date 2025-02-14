package data.cards;

import java.util.List;

public class Double extends Combinaison{

    public Double (List<Card> cards) {
        super(cards);

    }


    @Override
    public boolean isValid() {
        return getCards().size() == 2 && getCards().get(0).getNumber() == getCards().get(1).getNumber();
    }

    @Override
    public String toString() {
        return "Double : " + getCards();
    }
}
