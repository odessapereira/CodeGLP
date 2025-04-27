package engine.strategy;

import data.cards.*;
import data.players.Hand;
import engine.CombinaisonProcess;
import engine.GameEngine;

import java.util.ArrayList;

/**
 * Strategy for a balanced bot that plays neither too aggressively nor too defensively.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public class NormalStrategy implements Strategy {
    private GameEngine gameEngine;

    public NormalStrategy(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public ArrayList<Card> playTurn(BotPlayer bot, ArrayList<Card> lastPlayedCombinaison) {
        Hand hand = bot.getHand();
        CombinaisonProcess cp = new CombinaisonProcess(hand);
        ArrayList<Card> selected = new ArrayList<>();

        // === CAS 1 : Premier tour ou relance ===
        if (lastPlayedCombinaison == null || lastPlayedCombinaison.isEmpty()) {
            return playFirstMove(bot, cp, hand);
        }

        // === CAS 2 : Répondre à une combinaison jouée ===
        return respondToCombination(bot, cp, hand, lastPlayedCombinaison);
    }

    private ArrayList<Card> playFirstMove(BotPlayer bot, CombinaisonProcess cp, Hand hand) {
        ArrayList<Card> selected = new ArrayList<>();

        ArrayList<Card> winningCombination = cp.findWinningCombination();
        if (winningCombination != null && winningCombination.size()!=0) {
            return winningCombination;
        }

        if (hand.size() > 8) {
            if (cp.hasSerie()) {
                selected.addAll(cp.getBestSerie().getCards());
            } else if (cp.hasBomb()) {
                selected.addAll(cp.getBestBomb().getCards());
            } else if (cp.hasDouble()) {
                selected.addAll(cp.getBestDouble().getCards());
            } else if (cp.hasSimple()) {
                selected.add(cp.getMiddleCard());
            }
        } else {
            if (cp.hasDouble()) {
                selected.addAll(cp.getLowestDouble().getCards());
            } else if (cp.hasSimple()) {
                selected.add(cp.getLowestCard());
            } else if (cp.hasSerie()) {
                selected.addAll(cp.getShortestLowestSerie().getCards());
            }
        }

        if (cp.hasBomb() && gameEngine.hasAdversaryWithFewCards() && hand.size() > 4) {
            Bomb bomb = cp.getWeakestBombWithout2();
            if (bomb != null) {
                return new ArrayList<>(bomb.getCards());
            }
        }

        if (cp.hasDoubleJoker()) {
            return cp.getDoubleJoker();
        }

        return selected;
    }

    private ArrayList<Card> respondToCombination(BotPlayer bot, CombinaisonProcess cp, Hand hand, ArrayList<Card> lastPlayedCombinaison) {
        ArrayList<Card> selected = new ArrayList<>();

        if (cp.isDoubleJoker(lastPlayedCombinaison)) {
            return cp.hasDoubleJoker() ? cp.getDoubleJoker() : new ArrayList<>();
        }

        Bomb lastBomb = new Bomb(lastPlayedCombinaison);
        DoubleCard lastDouble = new DoubleCard(lastPlayedCombinaison);
        Serie lastSerie = new Serie(lastPlayedCombinaison);

        boolean isSingle = lastPlayedCombinaison.size() == 1;
        boolean isSpecial = isSingle && lastPlayedCombinaison.get(0).getNumber() == 20;

        if (isSingle && !isSpecial) {
            Card higher = cp.getNextHigherCard(lastPlayedCombinaison.get(0));
            if (higher != null) {
                selected.add(higher);
            } else if (shouldPlaySpecialCard(bot, cp)) {
                selected.add(cp.getSpecialCard());
            } else {
                selected.addAll(playStrategicBomb(cp));
            }
        } else if (lastDouble.isValid()) {
            DoubleCard nextDouble = cp.getNextDouble(lastDouble);
            if (nextDouble != null) {
                selected.addAll(nextDouble.getCards());
            } else if (shouldPlaySpecialCard(bot, cp)) {
                selected.add(cp.getSpecialCard());
            } else {
                selected.addAll(playStrategicBomb(cp));
            }
        } else if (lastSerie.isValid()) {
            Serie nextSerie = cp.getNextSerie(lastSerie);
            if (nextSerie != null && nextSerie.getCards().size() <= lastSerie.getCards().size() + 1) {
                selected.addAll(nextSerie.getCards());
            } else if (shouldPlaySpecialCard(bot, cp)) {
                selected.add(cp.getSpecialCard());
            } else {
                selected.addAll(playStrategicBomb(cp));
            }
        } else if (isSpecial) {
            if (cp.hasSpecialCard()) {
                selected.add(cp.getSpecialCard());
            } else {
                selected.addAll(playStrategicBomb(cp));
            }
        } else if (lastBomb.isValid()) {
            Bomb nextBomb = cp.getNextBomb(lastBomb);
            if (nextBomb != null && hand.size() > nextBomb.getCards().size()) {
                selected.addAll(nextBomb.getCards());
            }
        } else if (shouldUseDoubleJoker(bot, cp)) {
            selected.addAll(cp.getDoubleJoker());
        }

        return selected;
    }

    /**
     * Sélectionne une bombe stratégique à jouer si disponible.
     *
     * @param cp L'instance de CombinaisonProcess pour analyser les combinaisons possibles.
     * @return Une liste de cartes représentant la bombe à jouer, ou une liste vide si aucune bombe appropriée n'est trouvée.
     */
    private ArrayList<Card> playStrategicBomb(CombinaisonProcess cp) {
        if (cp.hasBomb()) {
            Bomb bomb = cp.getWeakestBombWithout2();
            if (bomb != null && bomb.getCards().size() <= 4) {
                return new ArrayList<>(bomb.getCards());
            }
        }
        return new ArrayList<>();
    }

    /**
     * Détermine si le bot doit jouer une carte spéciale (carte "2").
     *
     * Conditions :
     * - Le bot a peu de cartes restantes.
     * - Un adversaire a peu de cartes restantes.
     * - Le bot possède plusieurs cartes spéciales.
     *
     * @param bot Le bot qui joue.
     * @param cp L'instance de CombinaisonProcess pour évaluer la main du bot.
     * @return true si une carte spéciale doit être jouée, false sinon.
     */
    private boolean shouldPlaySpecialCard(BotPlayer bot, CombinaisonProcess cp) {
        return cp.hasSpecialCard() && (
                bot.getHand().size() <= 6 ||
                        gameEngine.hasAdversaryWithFewCards() ||
                        cp.countSpecialCards() >= 2
        );
    }


    /**
     * Détermine si le bot doit utiliser un double joker.
     *
     * Conditions :
     * - Le bot a très peu de cartes restantes.
     * - Un adversaire a peu de cartes restantes.
     *
     * @param bot Le bot qui joue.
     * @param cp L'instance de CombinaisonProcess pour évaluer la main du bot.
     * @return true si le double joker doit être joué, false sinon.
     */
    private boolean shouldUseDoubleJoker(BotPlayer bot, CombinaisonProcess cp) {
        return cp.hasDoubleJoker() && (
                bot.getHand().size() <= 5 ||
                        gameEngine.hasAdversaryWithFewCards()
        );
    }
}
