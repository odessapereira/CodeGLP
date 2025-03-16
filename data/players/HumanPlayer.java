package data.players;

import data.cards.Card;

import java.util.List;

public class HumanPlayer extends Player{

    public HumanPlayer(String name, List<Card> hand) {
        super(name, hand);
    }

    @Override
    public void playTurn() {

    }
}
