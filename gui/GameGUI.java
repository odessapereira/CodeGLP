package gui;
import data.cards.Card;
import data.cards.*;
import game.DiscardPile;
import engine.CardsInteractions;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class GameGUI extends JFrame {

    private JPanel playArea;
    private PlayerPanel playerArea;
    private HashMap<Card,CardPanel> deck;
//    private JPanel discardPilePanels;
    private JPanel discardPileContainer;
    private Stack discardPile;
    private JPanel drawPile;
    private CardsInteractions ci;

    private final Color BACKGROUND_COLOR = new Color(11, 167, 53);
    private final Color PLAYER_BACKGROUND_COLOR = new Color(34, 139, 34);
    private final Color TABLE_BACKGROUND_COLOR = new Color(18, 124, 18, 218);

    public static final Dimension CARD_DIMENSION = new Dimension(71,99);

    public GameGUI() {
        setTitle("Jeu de Cartes");
        setSize(1200, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ci = new CardsInteractions();
        deck = new HashMap<Card,CardPanel>();
        discardPile = new Stack();
        playerArea = new PlayerPanel("You");


        PlayerPanel leftPanel = new PlayerPanel("bot1");
        PlayerPanel rightPanel = new PlayerPanel("bot2");
        PlayerPanel northPanel = new PlayerPanel("bot3");

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(northPanel, BorderLayout.NORTH);
        add(playerArea, BorderLayout.SOUTH);

        // Left Panel
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Bot 1"));
        leftPanel.setPreferredSize(new Dimension(200, 200));
        leftPanel.setLayout(new BorderLayout());

        // Right Panel
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Bot 2"));
        rightPanel.setPreferredSize(new Dimension(200, 200));
        rightPanel.setLayout(new BorderLayout());

        // North Panel
        northPanel.setBackground(BACKGROUND_COLOR);
        northPanel.setBorder(BorderFactory.createTitledBorder("Bot 3"));
        northPanel.setPreferredSize(new Dimension(200, 200));
        northPanel.setLayout(new BorderLayout());

        // South Panel
        playerArea.setBackground(BACKGROUND_COLOR);
//        playerArea.setBorder(BorderFactory.createTitledBorder("Cartes du joueur"));

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
//        JPanel discardPileContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JPanel drawPile = new JPanel();
        discardPileContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));

//        drawPile.setPreferredSize(new Dimension(70,110));
//        discardPilePanels.setPreferredSize(CARD_DIMENSION);

        drawPile.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
//        discardPilePanels.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        drawPileContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 0), // Bordure visible
                BorderFactory.createEmptyBorder(50, 50, 50, 50) // Padding interne
        ));
        discardPileContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 0), // Bordure visible
                BorderFactory.createEmptyBorder(50, 50, 50, 50) // Padding interne
        ));


        drawPile.setBackground(Color.WHITE);
//        discardPilePanels.setBackground(Color.WHITE);
//        drawPile.add(new CardPanel("src/images/hiddenCard.jpeg"));

        drawPileContainer.setBackground(null);
        discardPileContainer.setBackground(null);

        drawPileContainer.setPreferredSize(new Dimension(200,300));
        discardPileContainer.setPreferredSize(new Dimension(200,300));

        drawPileContainer.add(new CardPanel("src/images/hiddenCard.jpeg"));
//        drawPileContainer.add(drawPile);
//        discardPileContainer.add(new JLabel("Discard Pile"));
//        discardPileContainer.add(discardPilePanels);

        playArea.add(drawPileContainer, BorderLayout.NORTH);
        playArea.add(discardPileContainer, BorderLayout.SOUTH);

        tablePanel.add(playArea, BorderLayout.CENTER);


        JButton poser = new JButton("Poser");
        JButton piocher = new JButton("Piocher");
        controlPanel.add(poser);
        controlPanel.add(piocher);

        piocher.addActionListener(new PiocherAction());
        poser.addActionListener(new PoserAction());


        setVisible(true);
    }

    private void drawCard() {
        Card randomCard = ci.getRandomCard();
        CardPanel cardPanel = new CardPanel(randomCard.getImagePath()); // Utilisation de CardPanel
        ci.removeCard(randomCard.getImagePath());
        deck.put(randomCard, cardPanel); // Stocker la carte sous forme de CardPanel
        playerArea.addCardPanel(cardPanel); // Ajouter la carte au joueur
        playerArea.revalidate();
        playerArea.repaint();
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
            ArrayList<CardPanel> selectedCards = playerArea.getSelectedCard();
            if (selectedCards != null && !selectedCards.isEmpty()) {
                // Retirer les cartes de la main du joueur
                for (CardPanel card : selectedCards) {
                    playerArea.remove(card);
                }
                playerArea.revalidate();
                playerArea.repaint();

                // Ajouter les cartes posées dans la zone de jeu (playArea)
                playCard(selectedCards);
                playerArea.clearSelection(); // Désélectionner après avoir posé
            }
        }
    }


    private void playCard(ArrayList<CardPanel> selectedCards) {
        discardPileContainer.removeAll();
        for(CardPanel c: selectedCards) {
        discardPileContainer.add(c, BorderLayout.CENTER);
        }
//        discardPile.add(drawPile, BorderLayout.NORTH);
//        discardPile.add(discardPile, BorderLayout.SOUTH);
//        discardPile
        discardPileContainer.revalidate();
        discardPileContainer.repaint();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameGUI::new);
    }
}
