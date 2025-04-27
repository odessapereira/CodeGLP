package data.players;

import data.cards.Card;
import data.game.GameTable;
import gui.CardPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Abstract class representing a player in the game, holding a hand of cards and a name.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public abstract class Player {

    // Attributes for the player
    private String name;
    private Hand hand; // The player's hand of cards
    private int id;    // The player's unique identifier

    /**
     * Constructor to initialize a player with a name, hand, and id.
     *
     * @param name The name of the player.
     * @param hand The hand of cards held by the player. If null, a new empty hand is created.
     * @param id The unique identifier of the player.
     */
    public Player(String name, Hand hand, int id) {
        this.name = name;
        this.hand = hand != null ? hand : new Hand();
        this.id = id;
    }

    /**
     * Abstract method to be implemented by subclasses to define the behavior when playing a turn.
     *
     * @param player The player taking the turn.
     * @param lastCardPlayed The last card played in the previous turn.
     */
    public abstract void playTurn(Player player, Card lastCardPlayed);

    /**
     * Gets the player's name.
     *
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player's hand of cards.
     *
     * @return The hand of cards.
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Sets the player's hand of cards.
     *
     * @param hand The new hand of cards.
     */
    public void setHand(Hand hand) {
        this.hand = hand;
    }
}
