package data.players;

import data.cards.Card;
import java.util.ArrayList;
import java.util.List;

public abstract class Player {

    // Attributes for the player
    private String name;
    private List<Card> hand;  // The player's hand of cards
    private int jackpot;  // The player's jackpot value

    // Constructor to initialize the player with a name, hand, and jackpot
    public Player(String name, List<Card> hand, int jackpot) {
        this.name = name;
        this.hand = hand != null ? hand : new ArrayList<>();  // If hand is null, initialize as an empty list
        this.jackpot = jackpot;
    }

    // Abstract method to be implemented by subclasses to perform actions with the player's hand
    public abstract void playTurn();

    // Method to add a card to the player's hand
    public void addCardToHand(Card card) {
        hand.add(card);
    }

    // Method to remove a card from the player's hand
    public void removeCardFromHand(Card card) {
        hand.remove(card);
    }

    // Getters and setters for the attributes
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public int getJackpot() {
        return jackpot;
    }

    public void setJackpot(int jackpot) {
        this.jackpot = jackpot;
    }


    @Override
    public String toString() {
        return "Player: " + name + ", Jackpot: " + jackpot + ", Hand: " + hand;
    }
}

