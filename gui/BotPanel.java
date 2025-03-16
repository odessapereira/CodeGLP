package gui;

import data.cards.*;
import engine.CardsInteractions;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class BotPanel extends JPanel {

    private JPanel cardsPanel;
    private HashMap<CardPanel, Card> selectedCards;
    private CardsInteractions ci;
    private ArrayList<Card> cardCombination;
    private HashMap<CardPanel, Card> deck;
    private JLabel labelMessage;
    private int botId; // Identifiant unique du bot

    public BotPanel(int botId) {
        this.botId = botId;
        setLayout(new BorderLayout());
        cardsPanel = new JPanel();
        adjustCardPanelLayout();

        cardsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        cardsPanel.setBackground(null);
        cardsPanel.setBorder(BorderFactory.createTitledBorder("Cartes du bot " + botId));

//        labelMessage = new JLabel("combinaison : ");
//        labelMessage.setFont(new Font("Arial", Font.PLAIN, 13));
//        labelMessage.setBounds(10, 10, 200, 20);

        add(cardsPanel, BorderLayout.SOUTH);
//        cardsPanel.add(labelMessage,BorderLayout.EAST);
        selectedCards = new HashMap<>();
        cardCombination = new ArrayList<>();
        deck = new HashMap<>();
    }

    private void adjustCardPanelLayout() {
        if (botId == 1) {  // Bot à gauche
            cardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
            cardsPanel.setPreferredSize(new Dimension(200, 680)); // Ajuster la hauteur
        } else if (botId == 2) {  // Bot à droite
            cardsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
            cardsPanel.setPreferredSize(new Dimension(200, 680));
        } else if (botId == 3) {  // Bot en haut
            cardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
            cardsPanel.setPreferredSize(new Dimension(400, 150));
        }
    }


    public void addCardPanel(CardPanel cardPanel, Card card) {
        /*if (botId == 1) {
            cardPanel.rotateCard(-90);  // Rotation pour affichage vertical
        } else if (botId == 2) {
            cardPanel.rotateCard(90);
        } else if (botId == 3) {
            cardPanel.rotateCard(0); // Pas besoin de rotation
        }*/

        cardsPanel.add(cardPanel);
        cardsPanel.revalidate();
        cardsPanel.repaint();
        deck.put(cardPanel, card);
    }





    public void selectCardsAutomatically() {
        selectedCards.clear();
        for (CardPanel cardPanel : deck.keySet()) {
            if (Math.random() > 0.5) { // Sélection aléatoire de cartes
                selectedCards.put(cardPanel, deck.get(cardPanel));
                cardPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
            }
        }
        updateCombinationMessage();
    }

    public JPanel getCardsPanel() {
        return cardsPanel;
    }

    public void setCardsPanel(JPanel cardsPanel) {
        this.cardsPanel = cardsPanel;
    }

    private void updateCombinationMessage() {
        String combinaison = " ";
        ArrayList<Card> cards = new ArrayList<>(selectedCards.values());

        Serie serie = new Serie(cards);
        Bomb bomb = new Bomb(cards);
        DoubleCard doubleCards = new DoubleCard(cards);

        if (serie.isValid()) {
            combinaison = "une série";
        } else if (doubleCards.isValid()) {
            combinaison = "double";
        } else if (bomb.isValid()) {
            combinaison = "Bombe";
        }
        labelMessage.setText("Bot " + botId + " - Combinaison : " + combinaison);
    }

    public HashMap<CardPanel, Card> getSelectedCards() {
        return selectedCards;
    }

    public void clearSelection() {
        for (CardPanel cardPanel : selectedCards.keySet()) {
            cardPanel.setBorder(null);
        }
        selectedCards.clear();
        labelMessage.setText("combinaison : ");
    }

    public ArrayList<Card> getCardCombination() {
        return cardCombination;
    }
}
