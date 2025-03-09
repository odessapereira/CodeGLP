package gui;
import data.cards.Card;
import engine.CardsInteractions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


public class GameGUI extends JFrame {

    private JPanel playArea;
    private PlayerPanel playerArea;
    private HashMap<Card,CardPanel> deck;
    private JPanel discardPileContainer;
    private Stack discardPile;
    private JPanel drawPile;
    private CardsInteractions ci;
    private JLabel labelMessage;

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

        // Création du JLabel pour afficher le message
        labelMessage = new JLabel("Selectionner pour savoir la combinaison");
        labelMessage.setFont(new Font("Arial", Font.PLAIN, 12));
        labelMessage.setBounds(10, 10, 200, 20);  // Positionner en haut à gauche

        //Panel with buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlPanel.setBackground(null);
        playerArea.add(controlPanel, BorderLayout.NORTH);
        playerArea.setBackground(PLAYER_BACKGROUND_COLOR);
        playerArea.add(labelMessage, BorderLayout.EAST);



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

        drawPileContainer.add(new CardPanel("src/images/hiddenCard.jpeg"));

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
    private JLabel setLabelMessage(){
        return setLabelMessage();
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
            ArrayList<CardPanel> selectedCards = playerArea.getSelectedCards();
            if (!selectedCards.isEmpty()) {

                playCard(selectedCards); // ✅ Ajouter la carte posée dans la zone de jeu
                playerArea.revalidate();
                playerArea.repaint();
                playerArea.clearSelection(); // ✅ Désélectionner après avoir posé
            }
        }
    }

    private void playCard(ArrayList<CardPanel> selectedCards) {
        discardPileContainer.removeAll();
        for (CardPanel card : selectedCards) {
            discardPileContainer.add(card, BorderLayout.CENTER);
        }
        discardPileContainer.revalidate();
        discardPileContainer.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameGUI::new);
    }
}
