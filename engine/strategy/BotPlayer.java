 package engine.strategy;

import data.cards.Card;
import data.players.Hand;
import data.players.Player;
import engine.TurnManager;
import gui.CardPanel;

import java.util.List;

 public class BotPlayer extends Player {
     private Strategy strategy;
     private int id;

    // Constructor that initializes the bot with a name, hand, and jackpot
    public BotPlayer(String name,Hand hand, Strategy strategy,int id) {
        super(name,hand);
        this.strategy = strategy;
        this.id=id;
    }



    // Implementing the abstract method 'playTurn' for the bot to play its turn
    @Override
    public void playTurn() {

    }

     @Override
     public void setHand(Hand hand) {
         super.setHand(hand);
     }

     public CardPanel getCardPanel(Card card) {
         boolean isRotated = (this.getId() == 1 || this.getId() == 2); // Rotation pour bots 1 et 2
         return new CardPanel(card.getImagePath(), isRotated);
     }

     private int getId() {
        return id;
     }


 }


