package engine.strategy;

import data.cards.Card;
import data.cards.Combinaison;

import java.util.ArrayList;

/**
 * Strategy interface for the bot's gameplay.
 *
 * All bot strategies (such as Reserved, Adventurer, etc.) must implement this interface.
 * This interface defines the method for deciding the bot's move based on the current game situation.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public interface Strategy {

    /**
     * Method to determine the bot's move during its turn.
     *
     * @param bot The bot player.
     * @param lastPlayedCombinaison The last combination played on the table (could be empty if the round is starting).
     * @return An ArrayList of Cards representing the bot's move.
     */
    ArrayList<Card> playTurn(BotPlayer bot, ArrayList<Card> lastPlayedCombinaison);
}
