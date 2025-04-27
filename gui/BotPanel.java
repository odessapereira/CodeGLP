package gui;

import data.cards.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A panel representing a bot's card display in the game GUI.
 * It manages the visual layout of the bot's cards and handles
 * dynamic updates like changing panel colors and removing played cards.
 *
 * Each bot has a unique ID to distinguish its position and layout.
 *
 * @author Nadjib-M Fariza-A Odessa-T-P
 */
public class BotPanel extends JPanel {

    private JPanel cardsPanel;
    private HashMap<CardPanel, Card> selectedCards;
    private ArrayList<Card> cardCombination;
    private HashMap<CardPanel, Card> deck;
    private int botId; // Identifiant unique du bot
    private TitledBorder border;


    /**
     * Constructs a BotPanel for a specific bot identified by its ID.
     *
     * @param botId The unique identifier for the bot.
     */
    public BotPanel(int botId) {
        this.botId = botId;
        setLayout(new BorderLayout());
        cardsPanel = new JPanel();
        adjustCardPanelLayout();

        cardsPanel.setBackground(null);
        border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.white, 2),
                "Cartes du bot " + botId
        );
        border.setTitleColor(Color.white);
        cardsPanel.setBorder(border);
        this.setBackground(GameGUI.BACKGROUND_COLOR);


        add(cardsPanel, BorderLayout.SOUTH);

        selectedCards = new HashMap<>();
        cardCombination = new ArrayList<>();
        deck = new HashMap<>();
    }

    /**
     * Adjusts the card panel layout and size depending on the bot's ID.
     * Bot 1: left side, Bot 2: top center, Bot 3: right side.
     */
    private void adjustCardPanelLayout() {
        if (botId == 1) {  // Bot à gauche
            cardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
            cardsPanel.setPreferredSize(new Dimension(200, 680)); // Ajuster la hauteur
        } else if (botId == 2) {  // Bot en haut
            cardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
            cardsPanel.setPreferredSize(new Dimension(400, 150));
        } else if (botId == 3) {  // Bot à droite
            cardsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
            cardsPanel.setPreferredSize(new Dimension(200, 680));
        }
    }

    /**
     * Adds a card panel to the bot's display and associates it with a card.
     *
     * @param cardPanel The CardPanel to add.
     * @param card      The Card associated with the panel.
     */
    public void addCardPanel(CardPanel cardPanel, Card card) {

        cardsPanel.add(cardPanel);
        cardsPanel.revalidate();
        cardsPanel.repaint();
        deck.put(cardPanel, card);
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
                "Cartes du bot " + botId
        );
        cardsPanel.setBorder(border);
        border.setTitleColor(Color.white);

    }


    /**
     * Removes the played cards from the bot's display panel.
     *
     * @param selectedCards A HashMap mapping CardPanel instances to the corresponding played Cards.
     */
    public void removePlayedCards(HashMap<CardPanel, Card> selectedCards) {
        for (int i=0; i < selectedCards.keySet().size(); i++) {
            cardsPanel.remove(0); // Supprime la carte visuellement
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }



}