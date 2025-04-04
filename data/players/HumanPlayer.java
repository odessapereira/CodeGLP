package data.players;

import data.cards.Card;

import java.util.List;

public class HumanPlayer extends Player{

    public HumanPlayer(String name, List<Card> hand, int jackpot) {
        super(name, hand, jackpot);
    }

    @Override
    public void playTurn() {

    }
}
