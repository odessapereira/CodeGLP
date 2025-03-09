package gui;
import data.cards.Card;
import engine.CardsInteractions;
import engine.CombinationChecker;
import data.cards.Combinaison;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.stream.Collectors;


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

     // Créer un objet Card pour la carte cachée
        Card hiddenCard = new Card(-1, "hidden", "src/images/hiddenCard.jpeg");

        // Créer un CardPanel en utilisant l'objet Card
        CardPanel hiddenCardPanel = new CardPanel(hiddenCard);

        // Ajouter le CardPanel au container de la pioche
        drawPileContainer.add(hiddenCardPanel);


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
        // Récupérer une carte aléatoire
        Card randomCard = ci.getRandomCard();

        // Créer un CardPanel à partir de l'objet Card
        CardPanel cardPanel = new CardPanel(randomCard);  // Utilisation du constructeur de CardPanel qui prend un objet Card

        // Retirer la carte du paquet
        ci.removeCard(randomCard.getImagePath());

        // Stocker la carte dans le deck (HashMap)
        deck.put(randomCard, cardPanel);

        // Ajouter le CardPanel à la zone du joueur
        playerArea.addCardPanel(cardPanel);
        
        // Recalculer la mise en page pour afficher la nouvelle carte
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

                // Si une seule carte est sélectionnée, on la pose directement
                if (selectedCards.size() == 1) {
                    playCard(selectedCards);  // Ajouter la carte dans la zone de jeu sans validation
                    labelMessage.setText("✅ Une seule carte posée.");
                } else {
                    // Conversion des CardPanel en Card pour la vérification
                	ArrayList<Card> cards = selectedCards.stream()
                		    .map(cardPanel -> {
                		        // Créer une Card avec le numéro et la couleur qui sont stockés dans CardPanel
                		        return new Card(cardPanel.getCardNumber(), cardPanel.getCardColor(), cardPanel.getImagePath());
                		    })
                		    .collect(Collectors.toCollection(ArrayList::new));

                	System.out.println("Cartes à valider : " + cards); // Vérifie les cartes avant validation

                    // Vérifier la combinaison pour plusieurs cartes
                    Combinaison combinaison = CombinationChecker.getValidCombination(cards);

                    if (combinaison != null) {
                        // Si la combinaison est valide
                        playCard(selectedCards);
                        labelMessage.setText("✅ Combinaison valide : " + combinaison.toString());
                    } else {
                        // Si la combinaison est invalide
                        labelMessage.setText("❌ Combinaison invalide !");
                    }
                }

                // Désélectionner les cartes après avoir tenté de poser
                playerArea.clearSelection();
                playerArea.revalidate();
                playerArea.repaint();
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
