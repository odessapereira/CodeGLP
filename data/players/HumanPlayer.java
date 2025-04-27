package data.players;

import data.cards.Card;

/**
 * Class representing a human player in the game.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public class HumanPlayer extends Player {

    private static int id = 0;

    /**
     * Constructor to create a HumanPlayer with a name, hand, and id.
     *
     * @param name The name of the human player.
     * @param hand The hand of cards held by the player.
     * @param id The unique identifier of the player.
     */
    public HumanPlayer(String name, Hand hand, int id) {
        super(name, hand, id);
    }

    /**
     * Method to play a turn.
     * Human players will interact through the GUI, so no logic is handled here.
     *
     * @param player The player taking the turn.
     * @param lastPlayedCard The last card played.
     */
    @Override
    public void playTurn(Player player, Card lastPlayedCard) {
        // Human actions are handled externally via the user interface.
    }
}
