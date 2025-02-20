package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PlayerPanel extends JPanel {

    private JPanel cardsPanel;
    private ArrayList<CardPanel> cardPanels;
    private CardPanel selectedCard;

    public PlayerPanel(String playerName) {
        setLayout(new BorderLayout());
        cardsPanel = new JPanel();
        cardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cardsPanel.setPreferredSize(new Dimension(800, 150));
        cardsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        cardsPanel.setBackground(null);
        cardsPanel.setBorder(BorderFactory.createTitledBorder("Cartes du joueur"));

        add(cardsPanel, BorderLayout.SOUTH); // Ajout du panel des cartes
        cardPanels = new ArrayList<>();
    }

    public void addCardPanel(CardPanel cardPanel) {
        cardsPanel.add(cardPanel);
        cardsPanel.add(cardPanel);
        cardsPanel.revalidate();
        cardsPanel.repaint();

//         Ajouter un écouteur de clic pour la sélection de la carte
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelectedCard(cardPanel); // Sélectionner la carte sur clic
            }
        });

        cardsPanel.revalidate();
        cardsPanel.repaint();

    }

    public ArrayList<CardPanel> getCardLabels() {
        return cardPanels;
    }

    public void clearCards() {
//        cardLabels.clear();
        cardsPanel.removeAll();
        revalidate();
        repaint();
    }

    public void setSelectedCard(CardPanel cardLabel) {
        // Si une carte était déjà sélectionnée, la désélectionner
        if (selectedCard != null) {
            selectedCard.setBorder(null); // Retirer la bordure de l'ancienne sélection
        }

        // Sélectionner la nouvelle carte
        selectedCard = cardLabel;
        selectedCard.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2)); // Ajout d'un cadre pour marquer la sélection
    }

    public CardPanel getSelectedCard() {
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
