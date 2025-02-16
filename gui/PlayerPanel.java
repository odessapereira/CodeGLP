package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PlayerPanel extends JPanel {
    private JLabel playerInfoLabel;
    private JPanel cardsPanel;
    private ArrayList<JLabel> cardLabels = new ArrayList<>();

    public PlayerPanel(String playerName) {
        setLayout(new BorderLayout());
        cardsPanel = new JPanel();
        cardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
//        cardsPanel.setBackground(Color.LIGHT_GRAY); // Définir une couleur de fond visible

        // Label avec les informations du joueur
//        playerInfoLabel = new JLabel("Joueur: " + playerName);
//        add(playerInfoLabel, BorderLayout.NORTH); // Place le label en haut
//        cardsPanel.add(playerInfoLabel);
        cardsPanel.setPreferredSize(new Dimension(800, 150));
        cardsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        cardsPanel.setBackground(null);

        add(cardsPanel, BorderLayout.SOUTH); // Ajout du panel des cartes
    }

    public void addCardLabel(JLabel cardLabel) {
        cardsPanel.add(cardLabel);
        cardsPanel.revalidate();
        cardsPanel.repaint();

    }

    public void clearCards() {
//        cardLabels.clear();
        cardsPanel.removeAll();
        revalidate();
        repaint();
    }
}
