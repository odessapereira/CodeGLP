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
import gui.BotPanel;

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
    private CardsInteractions ci;
    private TurnManager turnManager;

    public GameEngine(List<Player> players, GameGUI gameGUI, HashMap<CardPanel, Card> deck) {
        this.players = players;
        this.drawPile = new DrawPile();
        this.discardPile = new DiscardPile();
        this.gameTable = new GameTable();
        this.currentTurn = 0;
        this.isGameOver = false;
        this.gameGUI = gameGUI;
        this.deck = deck;
        this.ci=CardsInteractions.getInstance();
        this.turnManager=new TurnManager(players);
    }

    /**
     * Méthode qui initialise le jeu en distribuant 5 cartes pour chaque joueur et chaque bot
     */
    public void initializeGame() {
        int nbCartesParJoueur = 5; // Nombre de cartes à distribuer à chaque joueur et bot

        // Distribuer les cartes au joueur humain
        distributeCardsToPlayer(gameGUI.getPlayerArea(), nbCartesParJoueur);

        // Distribuer les cartes aux bots
        distributeCardsToBot(gameGUI.getLeftBotPanel(), nbCartesParJoueur);
        distributeCardsToBot(gameGUI.getRightBotPanel(), nbCartesParJoueur);
        distributeCardsToBot(gameGUI.getTopBotPanel(), nbCartesParJoueur);
    }

    private void distributeCardsToPlayer(PlayerPanel playerPanel, int nbCartes) {
        for (int i = 0; i < nbCartes; i++) {
            Card card = ci.getRandomCard();  // Récupère une carte
            CardPanel cardPanel = new CardPanel(card.getImagePath());  // Crée un CardPanel avec l'image de la carte

            if (card != null) {
                playerPanel.addCardPanel(cardPanel, card);
                playerPanel.revalidate();
                playerPanel.repaint();
            } else {
                System.out.println("Le draw pile est vide !");
            }
        }
    }

    private void distributeCardsToBot(BotPanel botPanel, int nbCartes) {
        for (int i = 0; i < nbCartes; i++) {
            Card hiddenCard =ci.getRandomCard() ;  // Récupère une carte du draw pile
            CardPanel cardPanel = new CardPanel("src/images/hiddenCard.jpeg");  // Crée un CardPanel avec l'image de la carte cachée

            if (hiddenCard != null) {
                botPanel.addCardPanel(cardPanel, hiddenCard);  // Ajouter la carte cachée au panneau du bot
                botPanel.revalidate();
                botPanel.repaint();
            } else {
                System.out.println("Le draw pile est vide !");
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
