package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PlayerPanel extends JPanel {
    private JLabel playerInfoLabel;
    private JPanel cardsPanel;
    private ArrayList<JLabel> cardLabels = new ArrayList<>();
    private JLabel selectedCard; // ✅ Ajout d'un champ pour gérer la sélection

    public PlayerPanel(String playerName) {
        setLayout(new BorderLayout());
        cardsPanel = new JPanel();
        cardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        cardsPanel.setPreferredSize(new Dimension(800, 150));
        cardsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        cardsPanel.setBackground(null);

        add(cardsPanel, BorderLayout.SOUTH);
    }

    public void addCardLabel(JLabel cardLabel) {
        // Ajout de la carte à la main du joueur
        cardsPanel.add(cardLabel);
        cardLabels.add(cardLabel); // Ajout à la liste des cartes

        // Ajouter un écouteur de clic pour la sélection de la carte
        cardLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelectedCard(cardLabel); // Sélectionner la carte sur clic
            }
        });

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    public void clearCards() {
        // Efface toutes les cartes de la main du joueur
        cardsPanel.removeAll();
        cardLabels.clear();
        revalidate();
        repaint();
    }

    public void setSelectedCard(JLabel cardLabel) {
        // Si une carte était déjà sélectionnée, la désélectionner
        if (selectedCard != null) {
            selectedCard.setBorder(null); // Retirer la bordure de l'ancienne sélection
        }

        // Sélectionner la nouvelle carte
        selectedCard = cardLabel;
        selectedCard.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2)); // Ajout d'un cadre pour marquer la sélection
    }

    public JLabel getSelectedCard() {
        return selectedCard; // Retourne la carte sélectionnée
    }

    public void clearSelection() {
        // Désélectionne la carte et retire la bordure
        if (selectedCard != null) {
            selectedCard.setBorder(null);
            selectedCard = null;
        }
    }
}
