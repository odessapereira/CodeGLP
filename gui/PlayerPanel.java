package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PlayerPanel extends JPanel {
    private JPanel cardsPanel;
    private ArrayList<CardPanel> cardPanels;
    private ArrayList<CardPanel> selectedCards; // Liste pour les cartes sélectionnées

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
        selectedCards = new ArrayList<>(); // Initialiser la liste de cartes sélectionnées
    }

    public void addCardPanel(CardPanel cardPanel) {
        cardsPanel.add(cardPanel);
        cardPanels.add(cardPanel); // Ajout à la liste des cartes

        // Ajouter un écouteur de clic pour la sélection de la carte
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelectedCard(cardPanel); // Sélectionner ou désélectionner la carte sur clic
            }
        });

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    public void setSelectedCard(CardPanel cardPanel) {
        if (selectedCards.contains(cardPanel)) {
            // Si la carte est déjà sélectionnée, la désélectionner
            selectedCards.remove(cardPanel);
            cardPanel.setBorder(null); // Retirer la bordure
        } else {
            // Sinon, sélectionner la nouvelle carte
            selectedCards.add(cardPanel);
            cardPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2)); // Ajouter une bordure pour marquer la sélection
        }
    }

    public ArrayList<CardPanel> getSelectedCard() {
        return selectedCards; // Retourne la liste des cartes sélectionnées
    }

    public void clearSelection() {
        // Désélectionner toutes les cartes et retirer les bordures
        for (CardPanel cardPanel : selectedCards) {
            cardPanel.setBorder(null); // Retirer la bordure de chaque carte sélectionnée
        }
        selectedCards.clear(); // Vider la liste des cartes sélectionnées
    }
}
