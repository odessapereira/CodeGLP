package engine;

import data.cards.Card;
import data.game.DiscardPile;
import data.game.DrawPile;
import data.game.GameTable;
import data.players.Hand;
import data.players.HumanPlayer;
import data.players.Player;
import engine.strategy.*;
import gui.CardPanel;
import gui.GameGUI;
import gui.PlayerPanel;
import gui.BotPanel;

import org.apache.log4j.Logger;
import log.LoggerUtility;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the game logic, including player turns, card distribution, and game state.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public class GameEngine {

    private ArrayList<Player> players;
    private DrawPile drawPile;
    private DiscardPile discardPile;
    private int currentTurn;
    private boolean isGameOver;
    private GameGUI gameGUI;
    private CardsInteractions ci;
    private TurnManager turnManager;
    private Hand humanHand;
    private JPanel discardPileContainer;
    private GameTable gameTable;
    private int numberPassTurn = 0;
    private int lastPlayedIndex = -1;
    private ArrayList<Card> lastPlayedCombinaison;
    private String winner;
    private ArrayList<Player> lostPLayers;
    private static final Logger logger = LoggerUtility.getLogger(GameEngine.class);

    /**
     * Constructor for GameEngine with a GameGUI.
     *
     * Initializes the game engine with the provided GUI, setting up the draw pile, discard pile, and other game components.
     *
     * @param gameGUI The graphical user interface for the game.
     */
    public GameEngine(GameGUI gameGUI) {
        this.drawPile = new DrawPile();
        this.discardPile = new DiscardPile();
        this.currentTurn = 0;
        this.isGameOver = false;
        this.gameGUI = gameGUI;
        this.ci = CardsInteractions.getInstance();
        humanHand = new Hand();
        discardPileContainer = gameGUI.getDiscardPileContainer();
        players = new ArrayList<Player>();
        lastPlayedCombinaison = new ArrayList<Card>();
        this.gameTable = new GameTable(players, drawPile, discardPile);
        this.winner = " ";
        this.lostPLayers = new ArrayList<>();
    }

    /**
     * Default constructor for GameEngine.
     *
     * Creates an empty game engine without initializing the GUI or other components.
     */
    public GameEngine() {}

    /**
     * Initializes the game by distributing 5 cards to each player and bot.
     *
     * Sets up the players, distributes cards, initializes the turn manager, and prepares the game for play.
     */
    public void initializeGame() {
        // Initialize players if not already created
        if (players == null || players.isEmpty()) {
            players.add(new HumanPlayer("Human Player", new Hand(), 0));
            players.add(new BotPlayer("Reserved Bot (bot 1)", new Hand(), new ReservedStrategy(this), 1));
            players.add(new BotPlayer("Normal Bot (bot 2)", new Hand(), new NormalStrategy(this), 2));
            players.add(new BotPlayer("Adventurer Bot (bot 3)", new Hand(), new AdventurerStrategy(this), 3));
        }

        // Number of cards per player
        int nbCartesParJoueur = 5;

        // Distribute cards to players and bots
        distributeCardsToPlayer(gameGUI.getPlayerArea(), nbCartesParJoueur, players.get(0));
        distributeCardsToBot(gameGUI.getLeftBotPanel(), nbCartesParJoueur, 1, players.get(1));
        distributeCardsToBot(gameGUI.getTopBotPanel(), nbCartesParJoueur, 2, players.get(2));
        distributeCardsToBot(gameGUI.getRightBotPanel(), nbCartesParJoueur, 3, players.get(3));

        GameTable gameTable = new GameTable(players, drawPile, discardPile);

        // Initialize the turn manager
        turnManager = new TurnManager(players, gameTable, this);

        // Display a message indicating the start of the game
        System.out.println("The game is ready! It's the Human Player's turn.");

        // Set the first turn to the human player
        currentTurn = 0;
    }

    /**
     * Retrieves the turn manager.
     *
     * @return The turn manager for the game.
     */
    public TurnManager getTurnManager() {
        return turnManager;
    }

    /**
     * Distributes cards to the human player and updates the GUI.
     *
     * @param playerPanel The panel where the player's cards are displayed.
     * @param nbCartes The number of cards to distribute.
     * @param player The human player receiving the cards.
     */
    public void distributeCardsToPlayer(PlayerPanel playerPanel, int nbCartes, Player player) {
        for (int i = 0; i < nbCartes; i++) {
            Card card = ci.getRandomCard();  // Retrieve a card
            CardPanel cardPanel = new CardPanel(card.getImagePath(), false, null);  // Create a CardPanel with the card's image

            humanHand.addCard(card);

            if (card != null) {
                playerPanel.addCardPanel(cardPanel, card);
                playerPanel.revalidate();
                playerPanel.repaint();
            } else {
                System.out.println("The draw pile is empty!");
            }
        }
        player.setHand(humanHand);
    }

    /**
     * Distributes cards to a bot and updates the GUI.
     *
     * @param botPanel The panel where the bot's cards are displayed.
     * @param nbCartes The number of cards to distribute.
     * @param botId The identifier of the bot.
     * @param player The bot player receiving the cards.
     */
    public void distributeCardsToBot(BotPanel botPanel, int nbCartes, int botId, Player player) {
        for (int i = 0; i < nbCartes; i++) {
            Card hiddenCard = ci.getRandomCard();  // Retrieve a card from the draw pile
            CardPanel cardPanel;  // Create a CardPanel with the hidden card's image

            // Choose display based on bot
            if (botId == 2) {
                // Bot 2: hidden cards
                cardPanel = new CardPanel("src/images/hiddenCard.jpeg", false, null);
            } else {
                // Bots 1 and 3: normal card display
                cardPanel = new CardPanel("src/images/hiddenCardPivot.jpg", true, null);
            }

            player.getHand().addCard(hiddenCard);

            if (hiddenCard != null) {
                botPanel.addCardPanel(cardPanel, hiddenCard);  // Add the hidden card to the bot's panel
                botPanel.revalidate();
                botPanel.repaint();
            } else {
                System.out.println("The draw pile is empty!");
            }
        }
    }

    /**
     * Manages the human player's turn.
     *
     * Processes the selected cards, validates the move, and updates the game state if valid.
     */
    public void playHumanTurn() {
        logger.info("[HUMAN] Start of the human player's turn.");

        HashMap<CardPanel, Card> selectedCards = gameGUI.getSelectedCards();

        if (!selectedCards.isEmpty()) {
            ArrayList<Card> selected = new ArrayList<>(selectedCards.values());
            if (turnManager.isValidMove(selected)) {
                logger.info("[HUMAN] Played cards: " + selected);

                discardPile.updateLastCombinaison(selected);
                playSelectedCardsHuman(selectedCards);

                gameGUI.getPlayerArea().clearSelection(); // Clear selection after playing
                gameGUI.getPlayerArea().revalidate();
                gameGUI.getPlayerArea().repaint();
            } else {
                logger.warn("[HUMAN] Invalid move: " + selected);
            }
        }
        if (players.get(0).getHand().isEmpty()) {
            isGameOver = true;
            winner = "human player";
        }
    }

    /**
     * Plays the selected cards for the human player.
     *
     * Updates the discard pile and removes the played cards from the player's hand.
     *
     * @param selectedCards The cards selected by the human player.
     */
    public void playSelectedCardsHuman(HashMap<CardPanel, Card> selectedCards) {
        gameGUI.clearDiscardPile(); // Clear the discard pile
        discardPile.addCards(new ArrayList<>(selectedCards.values()));

        for (Map.Entry<CardPanel, Card> cardEntry : selectedCards.entrySet()) { // Iterate through the HashMap keys
            humanHand.removeCard(cardEntry.getValue());
            gameGUI.getDiscardPileContainer().add(cardEntry.getKey(), BorderLayout.CENTER); // Add each card to the discard pile
        }

        numberPassTurn = 0;
        lastPlayedIndex = 0;

        gameGUI.getDiscardPileContainer().revalidate();
        gameGUI.getDiscardPileContainer().repaint();
    }

    /**
     * Manages a bot's turn.
     *
     * Uses the bot's strategy to select cards and updates the game state.
     *
     * @param bot The bot player whose turn is being played.
     */
    public void playBotTurn(BotPlayer bot) {
        logger.info("[BOT] Start of " + bot.getName() + "'s turn.");

        ArrayList<Card> selectedCard;
        HashMap<CardPanel, Card> selectedCardsPanels;

        Strategy strategy = bot.getStrategy();
        lastPlayedCombinaison = discardPile.getLastCombinaison();

        if (!bot.getHand().isEmpty()) {
            selectedCard = strategy.playTurn(bot, lastPlayedCombinaison);
            selectedCardsPanels = createCardPanels(selectedCard);

            logger.debug("[BOT] Cards selected by strategy: " + selectedCard);
        } else {
            logger.info("[BOT] Empty hand, nothing to play.");
            return;
        }
        if (!selectedCardsPanels.isEmpty()) {
            discardPile.updateLastCombinaison(new ArrayList<>(selectedCardsPanels.values()));
            playSelectedCardsBot(selectedCardsPanels, bot);

            logger.info("[BOT] " + bot.getName() + " played: " + new ArrayList<>(selectedCardsPanels.values()));

            if (bot.getId() == 1) {
                gameGUI.getLeftBotPanel().removePlayedCards(selectedCardsPanels); // Clear selection after playing
            }

            if (bot.getId() == 2) {
                gameGUI.getTopBotPanel().removePlayedCards(selectedCardsPanels); // Clear selection after playing
            }

            if (bot.getId() == 3) {
                gameGUI.getRightBotPanel().removePlayedCards(selectedCardsPanels); // Clear selection after playing
            }
        } else {
            // Calculate how many times the players have passed
            numberPassTurn++;
            logger.warn("[BOT] " + bot.getName() + " passed their turn (no valid combination found).");
        }
    }

    /**
     * Plays the selected cards for a bot.
     *
     * Updates the discard pile and removes the played cards from the bot's hand.
     *
     * @param selectedCards The cards selected by the bot.
     * @param bot The bot player playing the cards.
     */
    public void playSelectedCardsBot(HashMap<CardPanel, Card> selectedCards, BotPlayer bot) {
        gameGUI.clearDiscardPile(); // Clear the discard pile
        discardPile.addCards(new ArrayList<>(selectedCards.values()));

        for (CardPanel cardPanel : selectedCards.keySet()) { // Iterate through the HashMap keys
            gameGUI.getDiscardPileContainer().add(cardPanel, BorderLayout.CENTER); // Add the card to the discard pile
            bot.getHand().removeCard(selectedCards.get(cardPanel)); // Remove the card from the hand
        }

        numberPassTurn = 0;
        lastPlayedIndex = bot.getId();

        gameGUI.getDiscardPileContainer().revalidate();
        gameGUI.getDiscardPileContainer().repaint();
    }

    /**
     * Creates CardPanels for the selected cards.
     *
     * @param selectedCards The list of cards to create panels for.
     * @return A HashMap mapping CardPanels to their corresponding cards.
     */
    public HashMap<CardPanel, Card> createCardPanels(List<Card> selectedCards) {
        HashMap<CardPanel, Card> cardPanelMap = new HashMap<>();

        for (Card card : selectedCards) {
            String imagePath = card.getImagePath(); // Retrieve the card's image path
            CardPanel cardPanel = new CardPanel(imagePath, false, null); // Create a CardPanel
            cardPanelMap.put(cardPanel, card); // Add to the HashMap
        }

        return cardPanelMap;
    }

    /**
     * Sets the number of consecutive passed turns.
     *
     * @param numberPassTurn The number of passed turns.
     */
    public void setNumberPassTurn(int numberPassTurn) {
        this.numberPassTurn = numberPassTurn;
    }

    /**
     * Retrieves the number of consecutive passed turns.
     *
     * @return The number of passed turns.
     */
    public int getNumberPassTurn() {
        return numberPassTurn;
    }

    /**
     * Distributes one card to each player after all players pass their turns.
     *
     * Updates the game state to allow the last player to play first without checking the last combination.
     */
    public void distrubateCardsPassed() {
        distributeCardsToBot(gameGUI.getLeftBotPanel(), 1, 1, players.get(1));
        distributeCardsToBot(gameGUI.getTopBotPanel(), 1, 2, players.get(2));
        distributeCardsToBot(gameGUI.getRightBotPanel(), 1, 3, players.get(3));

        distributeCardsToPlayer(gameGUI.getPlayerArea(), 1, players.get(0));

        // Last player replays first
        turnManager.setCurrentPlayerIndex(lastPlayedIndex);

        // The player will play any card without checking the last played combination
        lastPlayedCombinaison.clear();
        lastPlayedIndex = (turnManager.getCurrentPlayerIndex() + 1) % players.size();
    }

    /**
     * Checks if the game is over.
     *
     * If the game is over, updates the list of losing players.
     *
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        if (turnManager.isGameOver()) {
            setLostPLayers();
        }
        return turnManager.isGameOver();
    }

    /**
     * Retrieves the winner of the game.
     *
     * @return The name of the winner.
     */
    public String getWinner() {
        logger.warn(winner + " has won the game");
        return winner;
    }

    /**
     * Sets the winner of the game.
     *
     * @param winner The name of the winner.
     */
    public void setWinner(String winner) {
        this.winner = winner;
    }

    /**
     * Identifies and logs the players who lost the game.
     *
     * Adds players with non-empty hands to the list of losers.
     */
    public void setLostPLayers() {
        for (Player player : players) {
            if (player.getHand().size() != 0) {
                lostPLayers.add(player);
                logger.warn(player + " has lost the game");
            }
        }
    }

    /**
     * Retrieves the list of players who lost the game.
     *
     * @return The list of losing players.
     */
    public ArrayList<Player> getLostPLayers() {
        return lostPLayers;
    }

    /**
     * Checks if any opponent has a low number of cards.
     *
     * @return True if an opponent has 3 or fewer cards, false otherwise.
     */
    public boolean hasAdversaryWithFewCards() {
        int maxCards = 3;

        for (Player player : players) {
            if (!player.equals(players.get(turnManager.getCurrentPlayerIndex())) && player.getHand().getCards().size() <= maxCards) {
                return true;
            }
        }

        return false;
    }

    /**
     * Retrieves the number of turns played.
     *
     * @return The number of turns.
     */
    public int getNbTour() {
        return turnManager.getNbTour();
    }
}