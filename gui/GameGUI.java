package gui;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameGUI extends JFrame {
    private JPanel playArea;

    private PlayerPanel playerArea = new PlayerPanel("You");
    private List<String> deck;
    private JPanel discardPile;
    private Random random;

    private final Color BACKGROUND_COLOR = new Color(11, 167, 53);
    private final Color PLAYER_BACKGROUND_COLOR = new Color(34, 139, 34);
    private final Color TABLE_BACKGROUND_COLOR = new Color(18, 124, 18, 218);

    public static final Dimension CARD_DIMENSION = new Dimension(71,99);

    public GameGUI() {
        setTitle("Jeu de Cartes");
        setSize(1200, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        random = new Random();
        deck = new ArrayList<>();
        for (String card : new String[]{"2c.gif", "Jd.gif", "5h.gif", "9s.gif", "Qc.gif", "10c.gif", "Ah.gif", "Qs.gif", "7d.gif", "Kd.gif"}) {
            deck.add(card);
        }


//        JPanel centerPanel = new JPanel();
        PlayerPanel leftPanel = new PlayerPanel("bot1");
//        JPanel rightPanel = new JPanel();
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
        JPanel discardPileContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JPanel drawPile = new JPanel();
        discardPile = new JPanel();

        drawPile.setPreferredSize(CARD_DIMENSION);
        discardPile.setPreferredSize(CARD_DIMENSION);

        drawPile.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        discardPile.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        drawPileContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 0), // Bordure visible
                BorderFactory.createEmptyBorder(50, 50, 50, 50) // Padding interne
        ));
        discardPileContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 0), // Bordure visible
                BorderFactory.createEmptyBorder(50, 50, 50, 50) // Padding interne
        ));


        drawPile.setBackground(Color.WHITE);
        discardPile.setBackground(Color.WHITE);
        drawPile.add(new JLabel(new ImageIcon("src/images/hiddenCard.jpeg")));

        drawPileContainer.setBackground(null);
        discardPileContainer.setBackground(null);

        drawPileContainer.setPreferredSize(new Dimension(200,300));
        discardPileContainer.setPreferredSize(new Dimension(200,300));

        drawPileContainer.add(new JLabel("Draw Pile"));
        drawPileContainer.add(drawPile);
        discardPileContainer.add(new JLabel("Discard Pile"));
        discardPileContainer.add(discardPile);

        playArea.add(drawPileContainer, BorderLayout.NORTH);
        playArea.add(discardPileContainer, BorderLayout.SOUTH);

        tablePanel.add(playArea, BorderLayout.CENTER);

//
//        // Zone des joueurs (cartes en main)
//        playerArea = new JPanel();
//        playerArea.setBackground(BACKGROUND_COLOR);
//        playerArea.setBorder(BorderFactory.createTitledBorder("Cartes du joueur"));
//        tablePanel.add(playerArea);
//
//        // Zone de la pioche et de la défausse
//        JPanel deckPanel = new JPanel();
//        deckPanel.setLayout(new GridLayout(1, 2, 10, 10));
//        add(deckPanel, BorderLayout.EAST);
//
        JButton poser = new JButton("Poser");
        JButton piocher = new JButton("Piocher");
        controlPanel.add(poser);
        controlPanel.add(piocher);
//
        piocher.addActionListener(new PiocherAction()); 
            

        setVisible(true);
    }

    private void drawCard() {
        if (!deck.isEmpty()) {
            String drawnCard = deck.remove(random.nextInt(deck.size()));
            JLabel cardLabel = new JLabel(new ImageIcon("src/images/" + drawnCard));
//            JLabel cardLabel = new JLabel("src/images/" + drawnCard);
//            cardLabel.addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    playCard(cardLabel);
//                    playCard(cardLabel);
//                }
//            });
            playerArea.addCardLabel(cardLabel);
//            playerArea.add(cardLabel);
            playerArea.revalidate();
            playerArea.repaint();
        }
    }
    class PiocherAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            drawCard();
        }
    }
    private void playCard(JLabel cardLabel) {
        playerArea.remove(cardLabel);
        discardPile.add(cardLabel);
        playerArea.revalidate();
        playerArea.repaint();
        playArea.revalidate();
        playArea.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameGUI::new);
    }
}
