package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameGUI extends JFrame {
    private JPanel playerArea, playArea;
    private List<String> deck;
    private Random random;

    public GameGUI() {
        setTitle("Jeu de Cartes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        random = new Random();
        deck = new ArrayList<>();
        for (String card : new String[]{"2c.gif", "Jd.gif", "5h.gif", "9s.gif", "Qc.gif", "10c.gif", "Ah.gif", "Qs.gif", "7d.gif", "Kd.gif"}) {
            deck.add(card);
        }

        // Création du panneau principal (table de jeu)
        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(new Color(34, 139, 34)); // Vert comme une table de jeu
        tablePanel.setLayout(new GridLayout(2, 1, 10, 10));
        add(tablePanel, BorderLayout.CENTER);

        // Zone de jeu où les cartes sont posées
        playArea = new JPanel();
        playArea.setBackground(new Color(50, 205, 50));
        playArea.setBorder(BorderFactory.createTitledBorder("Table de jeu"));
        tablePanel.add(playArea);

        // Zone des joueurs (cartes en main)
        playerArea = new JPanel();
        playerArea.setBackground(new Color(0, 100, 0));
        playerArea.setBorder(BorderFactory.createTitledBorder("Cartes du joueur"));
        tablePanel.add(playerArea);

        // Zone de la pioche et de la défausse
        JPanel deckPanel = new JPanel();
        deckPanel.setLayout(new GridLayout(1, 2, 10, 10));
        add(deckPanel, BorderLayout.EAST);

        JButton drawPile = new JButton("Pioche");
        JButton discardPile = new JButton("Défausse");
        deckPanel.add(drawPile);
        deckPanel.add(discardPile);

        drawPile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawCard();
            }
        });

        setVisible(true);
    }

    private void drawCard() {
        if (!deck.isEmpty()) {
            String drawnCard = deck.remove(random.nextInt(deck.size()));
            JLabel cardLabel = new JLabel(new ImageIcon("src/images/" + drawnCard));
            cardLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    playCard(cardLabel);
                }
            });
            playerArea.add(cardLabel);
            playerArea.revalidate();
            playerArea.repaint();
        }
    }

    private void playCard(JLabel cardLabel) {
        playerArea.remove(cardLabel);
        playArea.add(cardLabel);
        playerArea.revalidate();
        playerArea.repaint();
        playArea.revalidate();
        playArea.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameGUI::new);
    }
}

