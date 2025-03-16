package gui;

import data.cards.*;
import engine.CardsInteractions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerPanel extends JPanel {

    private JPanel cardsPanel;
    private HashMap<CardPanel,Card> selectedCards;
    private CardsInteractions ci;
    private ArrayList <Card> cardCombianison;
    private HashMap<CardPanel,Card> deck;
    private JLabel labelMessage;





    public PlayerPanel(String playerName) {
        setLayout(new BorderLayout());
        cardsPanel = new JPanel();
        cardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cardsPanel.setPreferredSize(new Dimension(800, 150));
        cardsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        cardsPanel.setBackground(null);
        cardsPanel.setBorder(BorderFactory.createTitledBorder("Cartes du joueur"));

        // Création du JLabel pour afficher le message
        labelMessage = new JLabel("combinaison : ");
        labelMessage.setFont(new Font("Arial", Font.PLAIN, 13));
        labelMessage.setBounds(10, 10, 200, 20);

        add(cardsPanel, BorderLayout.SOUTH);// Ajout du panel des cartes
        add(labelMessage, BorderLayout.EAST);
        selectedCards = new HashMap<>();


        cardCombianison = new ArrayList<>();
        deck = new HashMap<>();

    }

    public void addCardPanel(CardPanel cardPanel, Card card ) {
        cardsPanel.add(cardPanel);
        cardsPanel.add(cardPanel);
        cardsPanel.revalidate();
        cardsPanel.repaint();
        deck.put(cardPanel,card);



//         Ajouter un écouteur de clic pour la sélection de la carte
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelectedCard(cardPanel,deck.get(cardPanel)); // Sélectionner la carte sur clic
            }
        });

        cardsPanel.revalidate();
        cardsPanel.repaint();

    }



    public void setSelectedCard(CardPanel cardLabel, Card card) {
        if (selectedCards.containsKey(cardLabel)) {
            // Si la carte est déjà sélectionnée, la désélectionner
            cardLabel.setBorder(null);
            selectedCards.remove(cardLabel);

        } else {
            // Sélectionner la nouvelle carte et l'ajouter à la HashMap
            selectedCards.put(cardLabel, card);
            cardLabel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));

        }
        String combinaison = " ";
        ArrayList<Card> cards = new ArrayList<>(selectedCards.values());// Récupère uniquement les cartes

        Serie serie = new Serie(cards);
        Bomb bomb = new Bomb(cards);
        DoubleCard doubleCards = new DoubleCard(cards);

        if (serie.isValid()){
            combinaison ="une serie";
            labelMessage.setText("Combinaison : "+combinaison);
        }if (doubleCards.isValid()){
            combinaison="double";
            labelMessage.setText("Combinaison : "+combinaison);
        }if (bomb.isValid()){
            combinaison="Bombe ";
            labelMessage.setText("Combinaison : "+combinaison);
        }
        else {
            labelMessage.setText("Combinaison : "+combinaison);
        }


    }


    public HashMap<CardPanel,Card> getSelectedCards() {
        return selectedCards; // Retourne la carte sélectionnée
    }


    public void clearSelection() {
        if (selectedCards != null) {
            for (CardPanel cardPanel : selectedCards.keySet()) {
                cardPanel.setBorder(null); // Retire la bordure de chaque carte sélectionnée
            }
            selectedCards.clear(); // Vide la HashMap
            labelMessage.setText("combinaison : ");
        }
    }

    public ArrayList<Card> getCardCombianison() {
        return cardCombianison;
    }


}
