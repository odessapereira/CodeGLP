package gui;
import data.cards.Card;

import engine.strategy.AdventurerStrategy;
import engine.strategy.BotPlayer;
import data.players.HumanPlayer;
import data.players.Player;
import engine.CardsInteractions;
import engine.GameEngine;
import engine.TurnManager;
import engine.strategy.NormalStrategy;
import engine.strategy.ReservedStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;



public class GameGUI extends JFrame {

    private JPanel playArea;
    private PlayerPanel playerArea;
    private HashMap<CardPanel,Card> deck;
    private JPanel discardPileContainer;
    private Stack discardPile;
    private JPanel drawPile;
    private CardsInteractions ci;
    private TurnManager turnManager;
    private BotPanel leftBotPanel;
    private BotPanel topBotPanel;
    private BotPanel rightBotPanel;


    private final Color BACKGROUND_COLOR = new Color(11, 167, 53);
    private final Color PLAYER_BACKGROUND_COLOR = new Color(34, 139, 34);

    public static final Dimension CARD_DIMENSION = new Dimension(71,99);



    public GameGUI() {
        setTitle("Jeu de Cartes");
        setSize(1200, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ci = CardsInteractions.getInstance();
        deck = new HashMap<CardPanel,Card>();
        discardPile = new Stack();
        playerArea = new PlayerPanel("You");


        leftBotPanel = new BotPanel(1);   // Bot à gauche
        rightBotPanel = new BotPanel(2);  // Bot à droite
        topBotPanel = new BotPanel(3);    // Bot en haut


        add(leftBotPanel, BorderLayout.WEST);
        add(rightBotPanel, BorderLayout.EAST);
        add(topBotPanel, BorderLayout.NORTH);
        add(playerArea, BorderLayout.SOUTH);

        List<Player> players = new ArrayList<>();

        BotPlayer bot1 = new BotPlayer("Bot Normal",null ,new NormalStrategy(),1);
        BotPlayer bot2 = new BotPlayer("Bot Réservé",null ,new ReservedStrategy(),2);
        BotPlayer bot3 = new BotPlayer("Bot Aventurier",null ,new AdventurerStrategy(),3);


        players.add(new HumanPlayer("you",null, 0));
        players.add(bot1);
        players.add(bot2);
        players.add(bot3);

        GameEngine gameEngine = new GameEngine(players, this, deck);
        gameEngine.initializeGame();
        turnManager = new TurnManager(players);

//         Left Panel
        leftBotPanel.setBackground(BACKGROUND_COLOR);


        // Right Panel
        rightBotPanel.setBackground(BACKGROUND_COLOR);

        // North Panel
        topBotPanel.setBackground(BACKGROUND_COLOR);


        // South Panel
        playerArea.setBackground(BACKGROUND_COLOR);


        // Positionner en haut à gauche

        //Panel with buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlPanel.setBackground(null);
        playerArea.add(controlPanel, BorderLayout.NORTH);
        playerArea.setBackground(PLAYER_BACKGROUND_COLOR);





        // Création du panneau principal (table de jeu)
        JPanel tablePanel = new JPanel(); // CenterPanel
        tablePanel.setBackground(new Color(34, 139, 34)); // Vert comme une table de jeu
        tablePanel.setLayout(new GridLayout(1, 1, 10, 10));
        add(tablePanel, BorderLayout.CENTER);




        // Zone de jeu où les cartes sont posées
        playArea = new JPanel();
        playArea.setBackground(new Color(50, 205, 50));
        playArea.setPreferredSize(new Dimension(200,200));
        playArea.setBorder(BorderFactory.createTitledBorder("Table de jeu"));
        playArea.setLayout(new BorderLayout());






        JPanel drawPileContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JPanel drawPile = new JPanel();
        discardPileContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));


        drawPile.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        drawPileContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 0), // Bordure visible
                BorderFactory.createEmptyBorder(50, 50, 50, 50) // Padding interne
        ));
        discardPileContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 0), // Bordure visible
                BorderFactory.createEmptyBorder(50, 50, 50, 50) // Padding interne
        ));



        drawPile.setBackground(Color.WHITE);

        drawPileContainer.setBackground(null);
        discardPileContainer.setBackground(null);

        drawPileContainer.setPreferredSize(new Dimension(200,300));
        discardPileContainer.setPreferredSize(new Dimension(200,300));

        drawPileContainer.add(new CardPanel("src/images/hiddenCard.jpeg", false));

        playArea.add(drawPileContainer, BorderLayout.NORTH);
        playArea.add(discardPileContainer, BorderLayout.SOUTH);

        tablePanel.add(playArea, BorderLayout.CENTER);



        JButton poser = new JButton("Poser");
        JButton piocher = new JButton("Piocher");
        JButton passer = new JButton("Passer");
        controlPanel.add(poser);
        controlPanel.add(piocher);
        controlPanel.add(passer);

        piocher.addActionListener(new PiocherAction());
        poser.addActionListener(new PoserAction());


        setVisible(true);
    }
    private JLabel setLabelMessage(){
        return setLabelMessage();
    }

    private void drawCard() {
        Card randomCard = ci.getRandomCard();
        CardPanel cardPanel = new CardPanel(randomCard.getImagePath(),false); // Utilisation de CardPanel
        ci.removeCard(randomCard.getImagePath());

        deck.put(cardPanel,randomCard); // Stocker la carte sous forme de CardPanel
        discardPile.add(randomCard);

        playerArea.addCardPanel(cardPanel,randomCard); // Ajouter la carte au joueur
        playerArea.revalidate();
        playerArea.repaint();

//        //ajouter la carte a la main
//        ci.AddCardHand(randomCard);
    }


    class PiocherAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            drawCard();
        }
    }

    class PoserAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            HashMap<CardPanel,Card> selectedCards = playerArea.getSelectedCards();

            if (!selectedCards.isEmpty() && turnManager.isValidMove(new ArrayList<>(selectedCards.values()))) {
                playCard(selectedCards); // Ajouter la carte posée dans la zone de jeu
                playerArea.revalidate();
                playerArea.repaint();
                playerArea.clearSelection(); // Désélectionner après avoir posé
            }
        }
    }


    private void playCard(HashMap<CardPanel, Card> selectedCards) {
        discardPileContainer.removeAll(); // Vide la pile de défausse

        for (CardPanel cardPanel : selectedCards.keySet()) { // Parcourt les clés de la HashMap
            discardPileContainer.add(cardPanel, BorderLayout.CENTER); // Ajoute chaque carte à la défausse
        }

        discardPileContainer.revalidate(); // Met à jour l'affichage
        discardPileContainer.repaint();
    }

    public PlayerPanel getPlayerArea() {
        return playerArea;
    }

    public BotPanel getLeftBotPanel() {
        return leftBotPanel;
    }

    public BotPanel getRightBotPanel() {
        return rightBotPanel;
    }

    public BotPanel getTopBotPanel() {
        return topBotPanel;
    }

    public JPanel getDiscardPileContainer() {
        return discardPileContainer;
    }

    public void clearDiscardPile() {
        if (discardPileContainer != null) {
            discardPileContainer.removeAll(); // Supprime tous les composants
            discardPileContainer.revalidate(); // Met à jour la mise en page
            discardPileContainer.repaint(); // Redessine le panel
        }
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameGUI::new);
    }
}
