package data.game;

import data.players.Player;

import java.util.ArrayList;

/**
 * Represents the game table, which holds the players, the draw pile, and the discard pile.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public class GameTable {

    private ArrayList<Player> players; // List of players in the game
    private DrawPile drawPile; // The pile of cards to be drawn
    private DiscardPile discardPile; // The pile of cards that have been discarded

    /**
     * Constructor to initialize the game table with a list of players, a draw pile, and a discard pile.
     *
     * @param players The list of players in the game.
     * @param drawPile The draw pile containing the available cards.
     * @param discardPile The discard pile containing the discarded cards.
     */
    public GameTable(ArrayList<Player> players, DrawPile drawPile, DiscardPile discardPile) {
        this.players = players; // Set the players for the game
        this.drawPile = drawPile; // Set the draw pile
        this.discardPile = discardPile; // Set the discard pile
    }

    /**
     * Default constructor for the GameTable class.
     */
    public GameTable() {}

    /**
     * Gets the discard pile of the game table.
     *
     * @return The discard pile.
     */
    public DiscardPile getDiscardPile() {
        return discardPile; // Return the discard pile
    }

    /**
     * Sets the discard pile for the game table.
     *
     * @param discardPile The discard pile to set.
     */
    public void setDiscardPile(DiscardPile discardPile) {
        this.discardPile = discardPile; // Set the discard pile
    }
}
