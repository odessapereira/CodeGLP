package gui;

import data.cards.Card;
import data.players.Player;
import engine.CardsInteractions;
import engine.GameEngine;
import engine.GameTimer;
import engine.TurnManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * The main game window for the card game, managing the GUI components, player interactions,
 * and game flow. It implements Runnable to handle the game loop.
 *
 * @author Nadjib-M Fariza-A Odessa-T-P
 */
public class GameGUI extends JFrame implements Runnable {

    private JPanel tableArea;
    private PlayerPanel playerArea;
    private HashMap<CardPanel, Card> deck;
    private JPanel discardPileContainer;
    private Stack discardPile;
    private CardsInteractions ci;
    private TurnManager turnManager;
    private BotPanel leftBotPanel;
    private BotPanel topBotPanel;
    private BotPanel rightBotPanel;
    private GameEngine gameEngine;
    private HashMap<CardPanel, Card> selectedCards;
    private GameTimer gameTimer;
    private boolean running = true;

    // Color palette for the GUI
    public static final Color BACKGROUND_COLOR = new Color(27, 54, 45);
    public static final Color PLAYER_BACKGROUND_COLOR = new Color(45, 90, 75);
    public static final Color TABLE_COLOR = new Color(70, 120, 100);
    public static final Color BUTTON_COLOR = new Color(30, 34, 38);
    public static final Color HIGHLIGHT_COLOR = new Color(210, 45, 45, 204);

    public static final Dimension CARD_DIMENSION = new Dimension(71, 99);

    /**
     * Constructs the main game window, initializing GUI components and game logic.
     */
    public GameGUI() {
        setTitle("Jeu de Cartes");
        setSize(1200, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ci = CardsInteractions.getInstance();
        deck = new HashMap<CardPanel, Card>();
        discardPile = new Stack();
        playerArea = new PlayerPanel();

        leftBotPanel = new BotPanel(1);
        topBotPanel = new BotPanel(2);
        rightBotPanel = new BotPanel(3);

        add(leftBotPanel, BorderLayout.WEST);
        add(rightBotPanel, BorderLayout.EAST);
        add(topBotPanel, BorderLayout.NORTH);
        add(playerArea, BorderLayout.SOUTH);

        // Panel with buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlPanel.setBackground(null);
        playerArea.add(controlPanel, BorderLayout.NORTH);
        playerArea.setBackground(PLAYER_BACKGROUND_COLOR);

        // Create the main game table panel
        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(PLAYER_BACKGROUND_COLOR);
        tablePanel.setLayout(new GridLayout(1, 1, 10, 10));
        add(tablePanel, BorderLayout.CENTER);

        // Game table area where cards are placed
        tableArea = new JPanel();
        tableArea.setBackground(TABLE_COLOR);
        tableArea.setPreferredSize(new Dimension(200, 200));
        tableArea.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                "Table de jeu",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Helvetica", Font.PLAIN, 14),
                Color.BLACK
        ));
        tableArea.setLayout(new BorderLayout());

        JPanel drawPileContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel drawPile = new JPanel();
        discardPileContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));

        drawPile.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        drawPileContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 0),
                BorderFactory.createEmptyBorder(50, 50, 50, 50)
        ));
        discardPileContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 0),
                BorderFactory.createEmptyBorder(50, 50, 50, 50)
        ));

        drawPile.setBackground(Color.WHITE);
        drawPileContainer.setBackground(null);
        discardPileContainer.setBackground(null);

        drawPileContainer.setPreferredSize(new Dimension(200, 300));
        discardPileContainer.setPreferredSize(new Dimension(200, 300));

        drawPileContainer.add(new CardPanel("src/images/hiddenCard.jpeg", false, null));

        tableArea.add(drawPileContainer, BorderLayout.NORTH);
        tableArea.add(discardPileContainer, BorderLayout.SOUTH);

        tablePanel.add(tableArea, BorderLayout.CENTER);

        JButton poser = new JButton("Poser");
        JButton piocher = new JButton("Piocher");
        JButton passer = new JButton("Passer");

        Font buttonFont = new Font("Helvetica", Font.BOLD, 16);

        poser.setFont(buttonFont);
        piocher.setFont(buttonFont);
        passer.setFont(buttonFont);

        poser.setBackground(BUTTON_COLOR);
        piocher.setBackground(BUTTON_COLOR);
        passer.setBackground(BUTTON_COLOR);

        poser.setForeground(Color.WHITE);
        piocher.setForeground(Color.WHITE);
        passer.setForeground(Color.WHITE);

        controlPanel.add(poser);
        controlPanel.add(piocher);
        controlPanel.add(passer);

        piocher.addActionListener(new PiocherAction());
        poser.addActionListener(new PoserAction());
        passer.addActionListener(new PasserAction());

        selectedCards = new HashMap<CardPanel, Card>();
        this.gameTimer = new GameTimer();

        gameEngine = new GameEngine(this);
        gameEngine.initializeGame();
        turnManager = gameEngine.getTurnManager();

        Image icon = Toolkit.getDefaultToolkit().getImage("src/images/logo_glp.PNG");
        setIconImage(icon);

        setVisible(true);
    }

    /**
     * Creates and returns a label for displaying messages (unused in current implementation).
     *
     * @return A JLabel for messages.
     */
    private JLabel setLabelMessage() {
        return setLabelMessage();
    }

    /**
     * Draws a random card from the deck and adds it to the player's hand.
     */
    private void drawCard() {
        Card randomCard = ci.getRandomCard();
        CardPanel cardPanel = new CardPanel(randomCard.getImagePath(), false, null);
        ci.removeCard(randomCard.getImagePath());

        deck.put(cardPanel, randomCard);
        discardPile.add(randomCard);

        playerArea.addCardPanel(cardPanel, randomCard);
        playerArea.revalidate();
        playerArea.repaint();
    }

    /**
     * Action listener for the "Piocher" (Draw) button, triggering a card draw.
     */
    class PiocherAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            drawCard();
        }
    }

    /**
     * Action listener for the "Poser" (Play) button, handling card play if the move is valid.
     */
    class PoserAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectedCards = playerArea.getSelectedCards();
            if (selectedCards.size() != 0 && turnManager.isValidMove(new ArrayList<>(selectedCards.values()))) {
                turnManager.nextTurn();
                resetColorCurrentPlayer(0);
            }
        }
    }

    /**
     * Action listener for the "Passer" (Pass) button, advancing the turn and incrementing pass count.
     */
    class PasserAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            turnManager.nextTurn();
            resetColorCurrentPlayer(0);
            gameEngine.setNumberPassTurn(gameEngine.getNumberPassTurn() + 1);
        }
    }

    /**
     * Retrieves the currently selected cards.
     *
     * @return A HashMap mapping CardPanel instances to their corresponding Card objects.
     */
    public HashMap<CardPanel, Card> getSelectedCards() {
        return selectedCards;
    }

    /**
     * Retrieves the player's panel.
     *
     * @return The PlayerPanel for the human player.
     */
    public PlayerPanel getPlayerArea() {
        return playerArea;
    }

    /**
     * Retrieves the left bot's panel.
     *
     * @return The BotPanel for the left bot.
     */
    public BotPanel getLeftBotPanel() {
        return leftBotPanel;
    }

    /**
     * Retrieves the right bot's panel.
     *
     * @return The BotPanel for the right bot.
     */
    public BotPanel getRightBotPanel() {
        return rightBotPanel;
    }

    /**
     * Retrieves the top bot's panel.
     *
     * @return The BotPanel for the top bot.
     */
    public BotPanel getTopBotPanel() {
        return topBotPanel;
    }

    /**
     * Retrieves the discard pile container panel.
     *
     * @return The JPanel containing the discard pile.
     */
    public JPanel getDiscardPileContainer() {
        return discardPileContainer;
    }

    /**
     * Clears the discard pile container and updates the display.
     */
    public void clearDiscardPile() {
        if (discardPileContainer != null) {
            discardPileContainer.removeAll();
            discardPileContainer.revalidate();
            discardPileContainer.repaint();
        }
    }

    /**
     * Highlights the current player's panel with a colored border.
     *
     * @param index The index of the current player (0: human, 1: left bot, 2: top bot, 3: right bot).
     */
    public void colorCurrentPlayer(int index) {
        switch (index) {
            case 0:
                playerArea.changeColorPanel(HIGHLIGHT_COLOR, 6);
                break;
            case 1:
                leftBotPanel.changeColorPanel(HIGHLIGHT_COLOR, 6);
                break;
            case 2:
                topBotPanel.changeColorPanel(HIGHLIGHT_COLOR, 6);
                break;
            case 3:
                rightBotPanel.changeColorPanel(HIGHLIGHT_COLOR, 6);
                break;
        }
    }

    /**
     * Resets the current player's panel border to the default color and thickness.
     *
     * @param index The index of the current player (0: human, 1: left bot, 2: top bot, 3: right bot).
     */
    public void resetColorCurrentPlayer(int index) {
        switch (index) {
            case 0:
                playerArea.changeColorPanel(Color.white, 2);
                break;
            case 1:
                leftBotPanel.changeColorPanel(Color.white, 2);
                break;
            case 2:
                topBotPanel.changeColorPanel(Color.white, 2);
                break;
            case 3:
                rightBotPanel.changeColorPanel(Color.white, 2);
                break;
        }
    }

    /**
     * Runs the game loop, managing turns and game state until the game ends.
     */
    public void run() {
        int randomStartIndex = turnManager.getRandomPlayerIndex();
        turnManager.setCurrentPlayerIndex(randomStartIndex);

        int i = 0;
        while (running) {
            System.out.println("Thread en cours d'exécution : " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (gameEngine.isGameOver()) {
                running = false;
                String winner = gameEngine.getWinner();
                endGame(winner, gameTimer.gameTimeSeconds(), gameEngine.getNbTour(), gameEngine.getLostPLayers());
                break;
            }

            if (gameEngine.getNumberPassTurn() == 4) {
                discardPileContainer.removeAll();
                repaint();
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gameEngine.distrubateCardsPassed();
                gameEngine.setNumberPassTurn(0);
            }

            int playerIndex = turnManager.getCurrentPlayerIndex();

            if (playerIndex != 0) {
                colorCurrentPlayer(playerIndex);
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                turnManager.nextTurn();
                resetColorCurrentPlayer(playerIndex);
            } else {
                colorCurrentPlayer(playerIndex);
            }

            repaint();
            i++;
        }
    }

    /**
     * Ends the game, closes the current window, and opens the GameFrame with final statistics.
     *
     * @param winner      The name of the winning player.
     * @param time        The total game duration in seconds.
     * @param nbTour      The number of rounds played.
     * @param lostPlayers The list of players who lost.
     */
    private void endGame(String winner, int time, int nbTour, ArrayList<Player> lostPlayers) {
        dispose();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameFrame gameFrame = new GameFrame(winner, time, nbTour, lostPlayers);
                gameFrame.setVisible(true);
            }
        });
    }

    /**
     * Main method to launch the game.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        GameGUI gameMainGUI = new GameGUI();
        Thread gameThread = new Thread(gameMainGUI);
        gameThread.start();
    }
}