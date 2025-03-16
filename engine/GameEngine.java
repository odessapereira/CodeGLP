package engine;

import data.cards.Card;
import data.game.DiscardPile;
import data.game.DrawPile;
import data.game.GameTable;
import data.players.HumanPlayer;
import data.players.Player;
import gui.CardPanel;
import gui.GameGUI;
import gui.PlayerPanel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEngine {

    private List<Player> players;
    private DrawPile drawPile;
    private DiscardPile discardPile;
    private GameTable gameTable;
    private int currentTurn;
    private boolean isGameOver;
    private GameGUI gameGUI;
    private HashMap<CardPanel, Card> deck;

    public GameEngine(List<Player> players, GameGUI gameGUI, HashMap<CardPanel, Card> deck) {
        this.players = players;
        this.drawPile = new DrawPile();
        this.discardPile = new DiscardPile();
        this.gameTable = new GameTable();
        this.currentTurn = 0;
        this.isGameOver = false;
        this.gameGUI = gameGUI;
        this.deck = deck;
    }

    /**
     * Initializes and starts the game.
     */
    public void startGame() {
        initializeGame();
        dealCards();
    }

    /**
     * Initializes the game state, setting up the deck, players, and starting conditions.
     */
    private void initializeGame() {
        currentTurn = 0;
        isGameOver = false;
    }

    /**
     * Distributes 5 cards to each player, handling visibility for human and bot players.
     */
    private void dealCards() {
        for (Player player : players) {
            distributeInitialCards(player);
        }
    }

    /**
     * Distributes 5 cards to a given player.
     */
    private void distributeInitialCards(Player player) {
        boolean isHuman = player instanceof HumanPlayer;
        PlayerPanel playerPanel = gameGUI.getPlayerArea();

        for (int i = 0; i < 5; i++) {
            Card drawnCard = drawPile.drawCard();
            if (drawnCard != null) {
                CardPanel cardPanel;

                if (isHuman) {
                    cardPanel = new CardPanel(drawnCard.getImagePath()); // Afficher la vraie carte
                } else {
                    cardPanel = new CardPanel("src/images/hiddenCard.jpeg"); // Afficher une carte cachée
                }

                deck.put(cardPanel, drawnCard);

                if (isHuman) {
                    playerPanel.addCardPanel(cardPanel, drawnCard);
                    playerPanel.revalidate();
                    playerPanel.repaint();
                }

                player.getHand().add(drawnCard);
            } else {
                System.out.println("Le deck est vide ! Impossible de distribuer plus de cartes.");
                return;
            }
        }
    }

    /**
     * Allows a player to draw a card from the deck.
     */
    public void drawCard(int playerId) {
        Card drawnCard = drawPile.drawCard();
        if (drawnCard != null) {
            players.get(playerId).getHand().add(drawnCard);
        } else {
            System.out.println("Draw pile is empty! Unable to draw more cards.");
        }
    }

    /**
     * Returns the current state of the game, including players' hands and cards on the table.
     */
    public Map<String, Object> getGameState() {
        Map<String, Object> state = new HashMap<>();
        state.put("currentTurn", currentTurn);
        state.put("players", players);
        state.put("drawPile", drawPile);
        state.put("discardPile", discardPile);
        state.put("gameTable", gameTable);
        return state;
    }
}
