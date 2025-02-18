package engine;

import data.cards.Card;

import java.util.HashMap;
import java.util.Random;

public class CardsInteractions {
    private HashMap<String, Card> cards;

    public CardsInteractions() {
        cards = new HashMap<>();
        initializeCards();
    }

    // Méthode pour initialiser la HashMap avec les cartes
    private void initializeCards() {
        String[] suits = {"c", "d", "h", "s"}; // c = coeur, d = carreau, h = trèfle, s = pique

        for (int number=1;number<15;number++) {
            for (String suit : suits) {
                String imagePath = "src/images/" + number + suit + ".gif";  // Chemin d'image, ajuster selon ton répertoire
                Card card = new Card(number, suit, imagePath);
                cards.put(imagePath, card);
            }
        }
    }

    // Accesseur pour récupérer la carte à partir du chemin
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

    public Card getRandomCard (){
        Random random = new Random();
        // Générer un nombre aléatoire pour la valeur de la carte (entre 1 et 14)
        int cardValue = random.nextInt(14) + 1;

        // Générer un nombre aléatoire pour la couleur parmi {c, d, h, s}
        String[] suits = {"c", "d", "h", "s"};
        String cardSuit = suits[random.nextInt(suits.length)];

        // Construire le nom du fichier de l'image de la carte
        String cardName = cardValue + cardSuit + ".gif";

        Card randomCard = cards.get("src/images/"+cardName);

        return randomCard;
    }

}
