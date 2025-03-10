package engine;

import data.cards.Card;
import data.players.Hand;

import java.util.HashMap;
import java.util.Random;

public class CardsInteractions {
    private static CardsInteractions instance; // Instance unique du Singleton
    private HashMap<String, Card> cards;
    private Hand playerHand;

    // Constructeur privé pour empêcher l'instanciation externe
    private CardsInteractions() {
        cards = new HashMap<>();
        playerHand = new Hand();
        initializeCards(); // Initialisation unique des cartes
    }

    // Méthode pour récupérer l'instance unique
    public static synchronized CardsInteractions getInstance() {
        if (instance == null) {
            instance = new CardsInteractions();
        }
        return instance;
    }

    // Méthode pour initialiser la HashMap avec les cartes (appelée une seule fois)
    private void initializeCards() {
        String[] suits = {"c", "d", "h", "s"}; // c = cœur, d = carreau, h = trèfle, s = pique

        for (int number = 1; number < 15; number++) {
            for (String suit : suits) {
                String imagePath = "src/images/" + number + suit + ".gif"; // Chemin de l'image
                Card card = new Card(number, suit, imagePath);
                cards.put(imagePath, card);
            }
        }
    }

    public Hand getPlayerHand() {
        return playerHand;
    }

    public void AddCardHand(Card card){
        playerHand.addCard(card);
    }

    public void setPlayerHand(Hand playerHand) {
        this.playerHand = playerHand;
    }

    // Accesseur pour récupérer une carte à partir de son chemin d'image
    public Card getCard(String imagePath) {
        return cards.get(imagePath);
    }

    // Méthode pour ajouter une carte dans la HashMap
    public void addCard(Card card) {
        cards.put(card.getImagePath(), card);
    }

    // Méthode pour retirer une carte de la HashMap
    public void removeCard(String imagePath) {
        cards.remove(imagePath);
    }

    // Méthode pour récupérer une carte aléatoire et l'ajouter à la main du joueur
    public Card getRandomCard() {
        Random random = new Random();
        int cardValue = random.nextInt(13) + 1; // Valeur entre 1 et 13
        String[] suits = {"c", "d", "h", "s"};
        String cardSuit = suits[random.nextInt(suits.length)];
        String cardName = cardValue + cardSuit + ".gif";

        Card randomCard = cards.get("src/images/" + cardName);
        

        if (randomCard != null) {
            playerHand.addCard(randomCard); // Ajout de la carte à la main du joueur
        }

        return randomCard;
    }
}
