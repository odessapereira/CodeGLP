package players;

import engine.GameEngine;
import game.GameTable;

public class BotPlayer extends Player {
    private PlayStrategy strategy;

    public BotPlayer(String name, PlayStrategy strategy) {
        super(name);
        this.strategy = strategy;
    }

    public void setStrategy(PlayStrategy strategy) {
        this.strategy = strategy;
    }
// va falloir modifier TurnManager pour exécuter la stratégie
    public PlayStrategy getStrategy() {
        return strategy;
    }
}
/* ancien code 
package data.players;

import data.cards.Card;
import java.util.List;

public class BotPlayer extends Player {

    // Constructor that initializes the bot with a name, hand, and jackpot
    public BotPlayer(String name, List<Card> hand, int jackpot) {
        super(name, hand, jackpot);
    }

    // Implementing the abstract method 'playTurn' for the bot to play its turn
    @Override
    public void playTurn() {
        // If the bot has no cards, it can't play anything
        if (getHand().isEmpty()) {
            System.out.println(getName() + " has no cards to play.");
            return;
        }

        // The bot randomly chooses a card from its hand to play
        Random rand = new Random();
        Hand hand = (Hand) getHand();
        int randomIndex = rand.nextInt(hand.getCardCount());
        Card cardToPlay = hand.getCards().get(randomIndex);

        // Remove the card from the hand and play it
        removeCard(cardToPlay);
        System.out.println(getName() + " plays: " + cardToPlay);
    }
}
*/

}


