package engine;

import data.cards.Bomb;
import data.cards.Card;
import data.cards.DoubleCard;
import data.cards.Serie;
import data.players.Hand;

import java.util.ArrayList;

public class CombinaisonProcess {
    private Hand hand;

    public CombinaisonProcess (Hand hand){
        this.hand= hand;
    }

    public CombinaisonProcess (){}

    public boolean hasBomb() {
        // Parcours de toutes les combinaisons possibles dans la main
        for (int i = 0; i < hand.getCards().size(); i++) {
            for (int j = i + 1; j < hand.getCards().size(); j++) {
                for (int k = j + 1; k < hand.getCards().size(); k++) {
                    ArrayList<Card> combo = new ArrayList<>();
                    combo.add(hand.getCards().get(i));
                    combo.add(hand.getCards().get(j));
                    combo.add(hand.getCards().get(k));

                    // Créer une bombe avec la combinaison de cartes
                    Bomb bomb = new Bomb(combo);

                    // Vérifier si la bombe est valide
                    if (bomb.isValid()) {
                        return true;  // Une bombe valide a été trouvée
                    }
                }
            }
        }

        // Si aucune bombe valide n'a été trouvée, retourner false
        return false;
    }

    public Bomb getBestBomb() {
        Bomb bestBomb = null;

        // Parcours de toutes les combinaisons possibles de 3 cartes dans la main
        for (int i = 0; i < hand.getCards().size(); i++) {
            for (int j = i + 1; j < hand.getCards().size(); j++) {
                for (int k = j + 1; k < hand.getCards().size(); k++) {
                    ArrayList<Card> combo = new ArrayList<>();
                    combo.add(hand.getCards().get(i));
                    combo.add(hand.getCards().get(j));
                    combo.add(hand.getCards().get(k));

                    // Créer une bombe avec la combinaison de cartes
                    Bomb bomb = new Bomb(combo);

                    // Vérifier si la bombe est valide
                    if (bomb.isValid()) {
                        // Si une bombe valide est trouvée, la considérer comme la meilleure bombe
                        if (bestBomb == null || bomb.getCards().size() > bestBomb.getCards().size()) {
                            bestBomb = bomb;
                        }
                    }
                }
            }
        }

        // Retourner la meilleure bombe trouvée (ou null si aucune bombe valide n'a été trouvée)
        return bestBomb;
    }


    public boolean hasSerie() {
        // Parcours de toutes les combinaisons possibles de cartes pour voir si une série valide existe
        for (int i = 0; i < hand.getCards().size(); i++) {
            for (int j = i + 1; j < hand.getCards().size(); j++) {
                for (int k = j + 1; k < hand.getCards().size(); k++) {
                    ArrayList<Card> combo = new ArrayList<>();
                    combo.add(hand.getCards().get(i));
                    combo.add(hand.getCards().get(j));
                    combo.add(hand.getCards().get(k));

                    // Créer une série avec la combinaison de cartes
                    Serie serie = new Serie(combo);

                    // Vérifier si la série est valide
                    if (serie.isValid()) {
                        return true; // Une série valide a été trouvée
                    }
                }
            }
        }

        return false; // Aucune série valide n'a été trouvée
    }

    public Serie getBestSerie() {
        Serie bestSerie = null;

        // Parcours de toutes les combinaisons possibles de 3 cartes dans la main
        for (int i = 0; i < hand.getCards().size(); i++) {
            for (int j = i + 1; j < hand.getCards().size(); j++) {
                for (int k = j + 1; k < hand.getCards().size(); k++) {
                    ArrayList<Card> combo = new ArrayList<>();
                    combo.add(hand.getCards().get(i));
                    combo.add(hand.getCards().get(j));
                    combo.add(hand.getCards().get(k));

                    // Créer une série avec la combinaison de cartes
                    Serie serie = new Serie(combo);

                    // Vérifier si la série est valide
                    if (serie.isValid()) {
                        // Si une série valide est trouvée, la considérer comme la meilleure série
                        if (bestSerie == null || serie.getCards().size() > bestSerie.getCards().size()) {
                            bestSerie = serie;
                        }
                    }
                }
            }
        }

        // Retourner la meilleure série trouvée (ou null si aucune série valide n'a été trouvée)
        return bestSerie;
    }

    public boolean hasDouble() {
        // Parcours de toutes les combinaisons possibles de cartes pour voir si un double valide existe
        for (int i = 0; i < hand.getCards().size(); i++) {
            for (int j = i + 1; j < hand.getCards().size(); j++) {
                ArrayList<Card> combo = new ArrayList<>();
                combo.add(hand.getCards().get(i));
                combo.add(hand.getCards().get(j));

                // Créer un double avec la combinaison de cartes
                DoubleCard aDouble = new DoubleCard(combo);

                // Vérifier si le double est valide
                if (aDouble.isValid()) {
                    return true; // Un double valide a été trouvé
                }
            }
        }

        return false; // Aucun double valide n'a été trouvé
    }

    public DoubleCard getBestDouble() {
        DoubleCard bestDouble = null;

        // Parcours de toutes les combinaisons possibles de cartes pour voir si un double valide existe
        for (int i = 0; i < hand.getCards().size(); i++) {
            for (int j = i + 1; j < hand.getCards().size(); j++) {
                ArrayList<Card> combo = new ArrayList<>();
                combo.add(hand.getCards().get(i));
                combo.add(hand.getCards().get(j));

                // Créer un double avec la combinaison de cartes
                DoubleCard aDouble = new DoubleCard(combo);

                // Vérifier si le double est valide
                if (aDouble.isValid()) {
                    // Si le double est valide, le comparer avec le meilleur trouvé jusqu'à maintenant
                    if (bestDouble == null || aDouble.getCards().get(0).getNumber() > bestDouble.getCards().get(0).getNumber()) {
                        bestDouble = aDouble;
                    }
                }
            }
        }

        return bestDouble; // Retourner le meilleur double trouvé, ou null si aucun double n'est valide
    }

    public boolean hasNextCard(Card topCard) {
        // Parcourt toutes les cartes de la main
        for (Card card : hand.getCards()) {
            // Vérifier si la carte peut être jouée, c'est-à-dire si elle a une valeur qui suit la topCard
            if (card.getNumber() == topCard.getNumber() + 1) {
                return true; // Si on trouve une carte avec la valeur suivante, retourne vrai
            }
        }
        return false; // Si aucune carte n'a une valeur qui suit celle de topCard, retourne faux
    }

    public Card getNextCard(Card topCard) {
        // Parcourt toutes les cartes de la main
        for (Card card : hand.getCards()) {
            // Si une carte a une valeur qui suit celle de topCard, retourne cette carte
            if (card.getNumber() == topCard.getNumber() + 1) {
                return card; // Carte suivante trouvée
            }
        }
        return null; // Aucune carte suivante trouvée
    }






}
