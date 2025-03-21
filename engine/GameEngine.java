package engine;

import data.cards.Card;
import data.game.DiscardPile;
import data.game.DrawPile;
import data.game.GameTable;
import data.players.Hand;
import data.players.HumanPlayer;
import data.players.Player;
import engine.strategy.BotPlayer;
import gui.CardPanel;
import gui.GameGUI;
import gui.PlayerPanel;
import gui.BotPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEngine {

    private List<Player> players;
    private DrawPile drawPile;
    private DiscardPile discardPile;
    private GameTable gameTable;
    private int currentTurn;
    private boolean isGameOver;
    private GameGUI gameGUI;
    private HashMap<CardPanel, Card> deck;
    private CardsInteractions ci;
    private TurnManager turnManager;
    private Hand hand;
    private JPanel discardPileContainer;





    public GameEngine(List<Player> players, GameGUI gameGUI, HashMap<CardPanel, Card> deck) {
        this.players = players;
        this.drawPile = new DrawPile();
        this.discardPile = new DiscardPile();
        this.gameTable = new GameTable();
        this.currentTurn = 0;
        this.isGameOver = false;
        this.gameGUI = gameGUI;
        this.deck = deck;
        this.ci=CardsInteractions.getInstance();
        this.turnManager=new TurnManager(players);
        hand = new Hand();
        discardPileContainer = gameGUI.getDiscardPileContainer();

    }

    public GameEngine (){}

    /**
     * Méthode qui initialise le jeu en distribuant 5 cartes pour chaque joueur et chaque bot
     */
    public void initializeGame() {
        int nbCartesParJoueur = 5; // Nombre de cartes à distribuer à chaque joueur et bot

        // Distribuer les cartes au joueur humain
        distributeCardsToPlayer(gameGUI.getPlayerArea(), nbCartesParJoueur, players.get(0));

        // Distribuer les cartes aux bots
        distributeCardsToBot(gameGUI.getLeftBotPanel(), nbCartesParJoueur, 1, players.get(1));
        distributeCardsToBot(gameGUI.getRightBotPanel(), nbCartesParJoueur, 2, players.get(2));
        distributeCardsToBot(gameGUI.getTopBotPanel(), nbCartesParJoueur, 3, players.get(3));
    }

    public void distributeCardsToPlayer(PlayerPanel playerPanel, int nbCartes, Player player) {
        for (int i = 0; i < nbCartes; i++) {
            Card card = ci.getRandomCard();  // Récupère une carte
            CardPanel cardPanel = new CardPanel(card.getImagePath(), false);  // Crée un CardPanel avec l'image de la carte

            hand.addCard(card);

            if (card != null) {
                playerPanel.addCardPanel(cardPanel, card);
                playerPanel.revalidate();
                playerPanel.repaint();
            } else {
                System.out.println("Le draw pile est vide !");
            }
        }
        player.setHand(hand);
    }

    public void distributeCardsToBot(BotPanel botPanel, int nbCartes, int botId, Player player ) {
        for (int i = 0; i < nbCartes; i++) {
            Card hiddenCard =ci.getRandomCard() ;  // Récupère une carte du draw pile
            CardPanel cardPanel ;  // Crée un CardPanel avec l'image de la carte cachée



            // Choisir l'affichage en fonction du bot
            if (botId == 3) {
                // Bot 3 : cartes cachées

                cardPanel = new CardPanel("src/images/hiddenCard.jpeg", false);
            } else {
                // Bots 1 et 2 : affichage normal des cartes
                cardPanel = new CardPanel("src/images/hiddenCardPivot.jpg", true);
            }

            hand.addCard(hiddenCard);


            if (hiddenCard != null) {
                botPanel.addCardPanel(cardPanel, hiddenCard);  // Ajouter la carte cachée au panneau du bot
                botPanel.revalidate();
                botPanel.repaint();
            } else {
                System.out.println("Le draw pile est vide !");
            }
        }
        //ajouter hand pour chaque bot
        player.setHand(hand);
        hand=new Hand();


    }

    public void playSelectedCards(List<Card> selectedCards, BotPlayer bot) {
        gameGUI.clearDiscardPile(); // Vide la pile de défausse

        for (Card card : selectedCards) {
            CardPanel cardPanel = bot.getCardPanel(card); // Récupère l'affichage correspondant
            discardPileContainer.add(cardPanel, BorderLayout.CENTER); // Ajoute la carte à la défausse
            bot.getHand().removeCard(card); // Retirer la carte de la main
        }

        discardPileContainer.revalidate();
        discardPileContainer.repaint();
    }




    /**
     * Returns the current state of the game, including players' hands and cards on the table.
     */
    public Map<String, Object> getGameState() {
        Map<String, Object> state = new HashMap<>();
        state.put("currentTurn", currentTurn);
        state.put("players", players);
        state.put("drawPile", drawPile);
        state.put("discardPile", discardPile);
        state.put("gameTable", gameTable);
        return state;
    }
}
