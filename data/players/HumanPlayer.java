package data.players;

import data.cards.Card;
import data.game.GameTable;
import gui.GameGUI;

import java.util.List;

public class HumanPlayer extends Player{

    private static int id=0;
    private GameGUI gameGUI;

    public HumanPlayer(String name, Hand hand, int id) {
        super(name, hand);
    }

    @Override
    public void playTurn() {
//        gameGUI.promptPlayerMove(this);
    }
}
