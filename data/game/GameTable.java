package data.game;

import data.cards.Card;
import data.players.Player;

public class GameTable {

    private Player player;
    private DrawPile drawPile;
    private DiscardPile discardPile;

    // Constructor to initialize the game table with a player, draw pile, and discard pile
    public GameTable(Player player, DrawPile drawPile, DiscardPile discardPile) {
        this.player = player;
        this.drawPile = drawPile;
        this.discardPile = discardPile;
    }

    public GameTable() {

    }


    // Getters and setters for the attributes
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public DrawPile getDrawPile() {
        return drawPile;
    }

    public void setDrawPile(DrawPile drawPile) {
        this.drawPile = drawPile;
    }

    public DiscardPile getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(DiscardPile discardPile) {
        this.discardPile = discardPile;
    }

    @Override
    public String toString() {
        return "GameTable {" +
                "Player: " + player + ", " +
                "DrawPile: " + drawPile + ", " + " }";

    }
}
