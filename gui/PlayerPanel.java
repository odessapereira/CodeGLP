package gui;

import data.cards.Serie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PlayerPanel extends JPanel {

    private JPanel cardsPanel;
    private ArrayList<CardPanel> cardPanels;
    private ArrayList<CardPanel> selectedCards;
    private Serie serie;



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
        selectedCards = new ArrayList<>();
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



    public void setSelectedCard(CardPanel cardLabel) {
        if (selectedCards.contains(cardLabel)) {
            // Si la carte est déjà sélectionnée, la désélectionner
            cardLabel.setBorder(null);
            selectedCards.remove(cardLabel);
        } else {
            // Sélectionner la nouvelle carte
            selectedCards.add(cardLabel);
            cardLabel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));

        }
    }


    public ArrayList<CardPanel> getSelectedCards() {
        return selectedCards; // Retourne la carte sélectionnée
    }


    public void clearSelection() {
        if (selectedCards != null) {
            for (CardPanel card : selectedCards) {
                card.setBorder(null); // Retire la bordure de chaque carte sélectionnée
            }
            selectedCards.clear(); // Vide la liste des cartes sélectionnées
        }
    }






}
