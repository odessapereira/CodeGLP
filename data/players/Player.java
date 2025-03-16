package data.players;

import data.cards.Card;
import java.util.ArrayList;
import java.util.List;

public abstract class Player {

    // Attributes for the player
    private String name;
    private List<Card> hand;  // The player's hand of cards

    // Constructor to initialize the player with a name, hand, and jackpot
    public Player(String name, List<Card> hand) {
        this.name = name;
        this.hand = hand != null ? hand : new ArrayList<>();  // If hand is null, initialize as an empty list
    }



    // Abstract method to be implemented by subclasses to perform actions with the player's hand
    public abstract void playTurn();

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



    @Override
    public String toString() {
        return "Player: " + name + ", Hand: " + hand;
    }
}

