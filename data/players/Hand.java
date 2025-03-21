package data.players;

import data.cards.Bomb;
import data.cards.Card;
import data.cards.Combinaison;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private ArrayList<Card> cards; // List of cards in the player's hand

    public Hand() {
        this.cards = new ArrayList<>();
    }

    // Add a card to the hand
    public void addCard(Card card) {
        cards.add(card);
    }

    // Remove a card from the hand
    public boolean removeCard(Card card) {
        return cards.remove(card);
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    // Check if the hand is empty
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    // Get the number of remaining cards
    public int getCardCount() {
        return cards.size();
    }


    // Get the list of cards in hand
    public ArrayList<Card> getCards() {
        return new ArrayList<>(cards); // Returns a copy to avoid external modifications
    }

    // les cartes de 2 sont de valeur 20, retourne Vrai si il trouve cette carte sinon faux
    public boolean hasSpecialCard() {
        for (Card card : this.getCards()) {
            if (card.getNumber() == 20) {
                return true;
            }
        }
        return false;
    }

    //permet de retourner la carte speciale (carte numero 2)
    public Card getSpecialCard() {
        for (Card card : this.getCards()) {
            if (card.getNumber() == 20) { // Vérifie si c'est une carte spéciale
                return card; // Retourne la première carte spéciale trouvée
            }
        }
        return null; // Aucune carte spéciale trouvée
    }






    // Display the cards in hand
    public void displayHand() {
        System.out.println("Cards in hand:");
        for (int i = 0; i < cards.size(); i++) {
            System.out.println((i + 1) + ". " + cards.get(i));
        }
    }
}
