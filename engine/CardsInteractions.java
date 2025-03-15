package engine;

import data.cards.Card;
import data.players.Hand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        for (int number = 1; number < 14; number++) {
            for (String suit : suits) {
                String imagePath = "src/images/" + number + suit + ".gif"; // Chemin de l'image
                Card card = new Card(number, suit, imagePath);
                cards.put(imagePath, card);
            }
        }
        cards.put("src/images/joker1.jpeg",new Card(0,"joker1","src/images/joker1.jpeg"));
        cards.put("src/images/joker2.jpeg",new Card(0,"joker2","src/images/joker2.jpeg"));

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

        // Vérifie si la HashMap est vide
        if (cards.isEmpty()) {
            return null; // ou lever une exception selon le besoin
        }

        // Créer une liste de clés (les chemins des images)
        List<String> keys = new ArrayList<>(cards.keySet());

        // Sélectionner un index aléatoire
        Random rand = new Random();
        int randomIndex = rand.nextInt(keys.size());

        // Récupérer la clé (chemin de l'image) correspondant à l'index aléatoire
        String randomKey = keys.get(randomIndex);

        // Récupérer la carte correspondante à cette clé
        Card randomCard = cards.get(randomKey);



        // Supprimer la carte de la HashMap
        cards.remove(randomKey);


        return randomCard;
    }



}
