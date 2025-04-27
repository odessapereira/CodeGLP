package engine.strategy;

import data.cards.Card;
import data.players.Hand;
import data.players.Player;

/**
 * Represents a bot player that uses a specific strategy to play.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public class BotPlayer extends Player {

    private Strategy strategy;
    private int id;

    /**
     * Constructor for BotPlayer.
     *
     * @param name The bot's name.
     * @param hand The bot's hand of cards.
     * @param strategy The strategy the bot uses to play.
     * @param id The bot's identifier.
     */
    public BotPlayer(String name, Hand hand, Strategy strategy, int id) {
        super(name, hand, id);
        this.strategy = strategy;
        this.id = id;
    }

    /**
     * The bot's way of playing its turn.
     * (Not implemented here; handled in GameEngine using the bot's strategy.)
     *
     * @param player The current player (bot itself).
     * @param lastPlayedCard The last card played on the table.
     */
    @Override
    public void playTurn(Player player, Card lastPlayedCard) {
        // The actual logic is handled externally using the bot's strategy.
    }

    // Getter for strategy
    public Strategy getStrategy() {
        return strategy;
    }

    // Getter for id
    public int getId() {
        return id;
    }
}
