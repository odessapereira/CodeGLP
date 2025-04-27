package gui;

import data.cards.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A panel representing the human player's card display in the game GUI. It manages the visual
 * representation of the player's cards, handles card selection, and displays the current combination.
 *
 * @author Nadjib-M Fariza-A Odessa-T-P
 */
public class PlayerPanel extends JPanel {

    private JPanel cardsPanel;
    private HashMap<CardPanel, Card> selectedCards;
    private ArrayList<Card> cardCombianison;
    private HashMap<CardPanel, Card> deck;
    private JLabel labelMessage;
    private TitledBorder border;

    /**
     * Constructs a PlayerPanel for the human player.
     */
    public PlayerPanel() {
        setLayout(new BorderLayout());
        cardsPanel = new JPanel();
        cardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cardsPanel.setPreferredSize(new Dimension(800, 150));
        border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.white, 2),
                "Cartes du JoueurHumain "
        );
        border.setTitleColor(Color.white);
        cardsPanel.setBorder(border);
        cardsPanel.setBackground(null);

        // Create the label for displaying the combination message
        labelMessage = new JLabel("combinaison : ");
        labelMessage.setFont(new Font("Arial", Font.PLAIN, 13));
        labelMessage.setForeground(Color.white);
        labelMessage.setBounds(10, 10, 200, 20);

        add(cardsPanel, BorderLayout.SOUTH);
        add(labelMessage, BorderLayout.EAST);
        selectedCards = new HashMap<>();
        cardCombianison = new ArrayList<>();
        deck = new HashMap<>();
    }

    /**
     * Adds a card panel to the player's display and associates it with a card.
     *
     * @param cardPanel The CardPanel to add.
     * @param card      The Card associated with the panel.
     */
    public void addCardPanel(CardPanel cardPanel, Card card) {
        cardsPanel.add(cardPanel);
        cardsPanel.revalidate();
        cardsPanel.repaint();
        deck.put(cardPanel, card);

        // Add a mouse listener for card selection
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelectedCard(cardPanel, deck.get(cardPanel));
            }
        });

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    /**
     * Changes the border color and thickness of the cards panel.
     *
     * @param color     The new border color.
     * @param thickness The new border thickness.
     */
    public void changeColorPanel(Color color, int thickness) {
        border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(color, thickness),
                "Cartes du joueurHumain"
        );
        cardsPanel.setBorder(border);
        border.setTitleColor(Color.white);
    }

    /**
     * Selects or deselects a card based on its current state and updates the combination message.
     *
     * @param cardLabel The CardPanel to select or deselect.
     * @param card      The Card associated with the panel.
     */
    public void setSelectedCard(CardPanel cardLabel, Card card) {
        if (selectedCards.containsKey(cardLabel)) {
            // Deselect the card if already selected
            cardLabel.setBorder(null);
            selectedCards.remove(cardLabel);
        } else {
            // Select the card and add it to the HashMap
            selectedCards.put(cardLabel, card);
            cardLabel.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
        }
        String combinaison = " ";
        ArrayList<Card> cards = new ArrayList<>(selectedCards.values());

        Serie serie = new Serie(cards);
        Bomb bomb = new Bomb(cards);
        DoubleCard doubleCards = new DoubleCard(cards);

        if (serie.isValid()) {
            combinaison = "une serie";
            labelMessage.setText("Combinaison : " + combinaison);
        }
        if (doubleCards.isValid()) {
            combinaison = "double";
            labelMessage.setText("Combinaison : " + combinaison);
        }
        if (bomb.isValid()) {
            combinaison = "Bombe ";
            labelMessage.setText("Combinaison : " + combinaison);
        } else {
            labelMessage.setText("Combinaison : " + combinaison);
        }
    }

    /**
     * Retrieves the currently selected cards.
     *
     * @return A HashMap mapping CardPanel instances to their corresponding Card objects.
     */
    public HashMap<CardPanel, Card> getSelectedCards() {
        return selectedCards;
    }

    /**
     * Clears the current selection of cards and resets the combination message.
     */
    public void clearSelection() {
        if (selectedCards != null) {
            for (CardPanel cardPanel : selectedCards.keySet()) {
                cardPanel.setBorder(null);
            }
            selectedCards.clear();
            labelMessage.setText("combinaison : ");
        }
    }
}