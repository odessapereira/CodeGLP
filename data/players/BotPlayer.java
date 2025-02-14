 package data.players;

import data.cards.Card;
import data.players.Hand;
import java.util.Random;

public class BotPlayer extends Player {

    // Constructor that initializes the bot with a name, hand, and jackpot
    public BotPlayer(String name, Hand hand, int jackpot) {
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
        Hand hand = getHand();
        int randomIndex = rand.nextInt(hand.getCardCount());
        Card cardToPlay = hand.getCards().get(randomIndex);

        // Remove the card from the hand and play it
        removeCardFromHand(cardToPlay);
        System.out.println(getName() + " plays: " + cardToPlay);
    }
}

}
