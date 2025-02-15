package data.game;

import data.cards.Card;
import data.players.Player;

public class GameTable {

    private Player player;
    private DrawPile drawPile;
    private DiscardPile discardPile;

    // Constructor to initialize the game table with a player, draw pile, and discard pile
    public GameTable(Player player, DrawPile drawPile, DiscardPile discardPile) {
        this.player = player;
        this.drawPile = drawPile;
        this.discardPile = discardPile;
    }

    public GameTable() {

    }

    // Method to start the game or perform game-related operations
//    public void startGame() {
//        // Initialize the game (shuffle, distribute cards, etc.)
//        drawPile.shuffle();
//        // Other game setup tasks can be added here
//    }

    // Method for the player to draw a card from the draw pile
//    public void playerDrawCard() {
//        Card drawnCard = drawPile.drawCard();
//        if (drawnCard != null) {
//            player.addCardToHand(drawnCard);
//            System.out.println("Player drew a card: " + drawnCard);
//        } else {
//            System.out.println("The draw pile is empty!");
//        }
//    }

    // Method for the player to discard a card to the discard pile
//    public void playerDiscardCard(Card card) {
//        discardPile.addCard(card);
//        System.out.println("Player discarded a card: " + card);
//    }

    // Getters and setters for the attributes
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public DrawPile getDrawPile() {
        return drawPile;
    }

    public void setDrawPile(DrawPile drawPile) {
        this.drawPile = drawPile;
    }

    public DiscardPile getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(DiscardPile discardPile) {
        this.discardPile = discardPile;
    }

    @Override
    public String toString() {
        return "GameTable {" +
                "Player: " + player + ", " +
                "DrawPile: " + drawPile + ", " + " }";

    }
}
