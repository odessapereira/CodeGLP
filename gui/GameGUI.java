package gui;

import data.game.DrawPile;
import data.cards.Card;
import engine.CardsInteractions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameGUI extends JFrame {
    private JPanel playArea;
    private PlayerPanel playerArea = new PlayerPanel("You");
    private List<Card> deck;
    private JPanel discardPile;
    private JPanel drawPile; // Définissez drawPile ici
    private Random random;

    private final Color BACKGROUND_COLOR = new Color(11, 167, 53);
    private final Color PLAYER_BACKGROUND_COLOR = new Color(34, 139, 34);
    private final Color TABLE_BACKGROUND_COLOR = new Color(18, 124, 18, 218);

    public static final Dimension CARD_DIMENSION = new Dimension(71, 99);

    public GameGUI() {
        setTitle("Jeu de Cartes");
        setSize(1200, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        random = new Random();
        deck = new ArrayList<Card>();

        PlayerPanel leftPanel = new PlayerPanel("bot1");
        PlayerPanel rightPanel = new PlayerPanel("bot2");
        PlayerPanel northPanel = new PlayerPanel("bot3");

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(northPanel, BorderLayout.NORTH);
        add(playerArea, BorderLayout.SOUTH);

        setupPanels(leftPanel, rightPanel, northPanel);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlPanel.setBackground(null);
        playerArea.add(controlPanel, BorderLayout.NORTH);
        playerArea.setBackground(PLAYER_BACKGROUND_COLOR);

        JPanel tablePanel = new JPanel(); // CenterPanel
        tablePanel.setBackground(new Color(34, 139, 34)); // Vert comme une table de jeu
        tablePanel.setLayout(new GridLayout(1, 1, 10, 10));
        add(tablePanel, BorderLayout.CENTER);

        // Zone de jeu où les cartes sont posées
        playArea = new JPanel();
        playArea.setBackground(new Color(50, 205, 50));
        playArea.setPreferredSize(new Dimension(200, 200));
        playArea.setBorder(BorderFactory.createTitledBorder("Table de jeu"));
        playArea.setLayout(new BorderLayout());

        JPanel drawPileContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel discardPileContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));

        drawPile = new JPanel(); // Initialisez ici
        discardPile = new JPanel();

        drawPile.setPreferredSize(CARD_DIMENSION);
        discardPile.setPreferredSize(CARD_DIMENSION);

        drawPile.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        discardPile.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        setupDrawAndDiscardPiles(drawPileContainer, discardPileContainer);

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

    private void setupPanels(PlayerPanel leftPanel, PlayerPanel rightPanel, PlayerPanel northPanel) {
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Bot 1"));
        leftPanel.setPreferredSize(new Dimension(200, 200));
        leftPanel.setLayout(new BorderLayout());

        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Bot 2"));
        rightPanel.setPreferredSize(new Dimension(200, 200));
        rightPanel.setLayout(new BorderLayout());

        northPanel.setBackground(BACKGROUND_COLOR);
        northPanel.setBorder(BorderFactory.createTitledBorder("Bot 3"));
        northPanel.setPreferredSize(new Dimension(200, 200));
        northPanel.setLayout(new BorderLayout());
    }

    private void setupDrawAndDiscardPiles(JPanel drawPileContainer, JPanel discardPileContainer) {
        drawPile.setBackground(Color.WHITE);
        discardPile.setBackground(Color.WHITE);
        drawPile.add(new JLabel(new ImageIcon("src/images/hiddenCard.jpeg")));

        drawPileContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 0), // Bordure visible
                BorderFactory.createEmptyBorder(50, 50, 50, 50) // Padding interne
        ));
        discardPileContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 0), // Bordure visible
                BorderFactory.createEmptyBorder(50, 50, 50, 50) // Padding interne
        ));

        drawPileContainer.setBackground(null);
        discardPileContainer.setBackground(null);

        drawPileContainer.setPreferredSize(new Dimension(200, 300));
        discardPileContainer.setPreferredSize(new Dimension(200, 300));

        drawPileContainer.add(new JLabel("Draw Pile"));
        drawPileContainer.add(drawPile);
        discardPileContainer.add(new JLabel("Discard Pile"));
        discardPileContainer.add(discardPile);
    }

    private void drawCard() {
        CardsInteractions ci = new CardsInteractions();
        Card randomCard = ci.getRandomCard();
        JLabel cardLabel = new JLabel(new ImageIcon(randomCard.getImagePath()));
        playerArea.addCardLabel(cardLabel);
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
            JLabel selectedCard = playerArea.getSelectedCard();
            if (selectedCard != null) {
                // Retirer la carte de la main du joueur
                playerArea.remove(selectedCard);
                playerArea.revalidate();
                playerArea.repaint();

                // Ajouter seulement la carte posée dans la zone de jeu (playArea)
                playCard(selectedCard);
                playerArea.clearSelection(); // ✅ Désélectionner après avoir posé
            }
        }
    }

    private void playCard(JLabel cardLabel) {
        // Supprimer toutes les cartes précédentes posées dans playArea
        playArea.removeAll(); // Clear the play area before adding the new card

        // Ajouter la carte posée à la zone de jeu
        playArea.add(cardLabel, BorderLayout.CENTER); // Ajouter la carte posée

        // Garder la carte de la pile de pioche visible
        playArea.add(drawPile, BorderLayout.NORTH); // Ajouter le panneau de la pile de pioche

        // Garder la carte de la pile de défausse visible
        playArea.add(discardPile, BorderLayout.SOUTH); // Ajouter le panneau de la pile de défausse

        playArea.revalidate(); // Mettre à jour la zone de jeu
        playArea.repaint();    // Rafraîchir l'affichage
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameGUI::new);
    }
}


