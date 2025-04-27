package engine.strategy;

import data.cards.*;
import data.players.Hand;
import engine.CombinaisonProcess;
import engine.GameEngine;
import java.util.ArrayList;


/**
 * Game strategy for a reserved bot.
 *
 * This bot focuses on preserving its strongest cards
 * for critical phases of the game and plays cautiously:
 * - It saves its bombs and special cards as much as possible.
 * - It plays weak or intermediate cards early in the game.
 * - It adjusts aggressiveness depending on the game progress and opponents.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public class ReservedStrategy implements Strategy {
    private GameEngine gameEngine;

    public ReservedStrategy(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }



    /**
     * Decides which cards the bot should play during its turn.
     *
     * @param bot The bot player.
     * @param lastPlayedCombinaison The last combination played on the table.
     * @return A list of cards representing the bot's move.
     */
    @Override
    public ArrayList<Card> playTurn(BotPlayer bot, ArrayList<Card> lastPlayedCombinaison) {
        Hand hand = bot.getHand();
        CombinaisonProcess cp = new CombinaisonProcess(hand);
        ArrayList<Card> selected = new ArrayList<>();


        // === CASE 1: Bot starts the round or resets the table ===
        if (lastPlayedCombinaison == null || lastPlayedCombinaison.isEmpty()) {
            // Priority: Play a winning combination if available and close to victory
            ArrayList<Card> winningCombination = cp.findWinningCombination();
            if (winningCombination != null && winningCombination.size()!=0) {
                selected.addAll(winningCombination);
                return selected;
            }
            // Early game: control or scout
            if (hand.size() > 8) {
                if (cp.hasSerie() && shouldPlayStrongSerie(cp)) {
                    Serie bestSerie = cp.getBestSerie();
                    selected.addAll(bestSerie.getCards());
                } else if (cp.hasDouble()) {
                    DoubleCard bestDouble = cp.getBestDouble();
                    selected.addAll(bestDouble.getCards());
                } else if (cp.hasSimple()) {
                    selected.add(cp.getMiddleCard());
                }
            }
            // Mid-game: balance between attack and defense
            else if (hand.size() <= 8 && hand.size() > 3) {
                if (cp.hasBomb() && !shouldPlayBomb(cp, lastPlayedCombinaison)) {
                    selected.addAll(cp.getBestBomb().getCards());
                } else if (cp.hasDouble()) {
                    selected.addAll(cp.getLowestDouble().getCards());
                } else if (cp.hasSerie()) {
                    selected.addAll(cp.getShortestLowestSerie().getCards());
                } else if (cp.hasSimple()) {
                    selected.add(cp.getLowestCard());
                }
            }
            // End-game: keep strong combinations for guaranteed win
            else {
                if (cp.hasSerie() && !shouldPlayBomb(cp, lastPlayedCombinaison)) {
                    selected.addAll(cp.getBestSerie().getCards());
                } else if (cp.hasDouble() && !shouldPlayBomb(cp, lastPlayedCombinaison)) {
                    selected.addAll(cp.getBestDouble().getCards());
                } else if (cp.hasSimple()) {
                    selected.add(cp.getLowestCard());
                } else if (cp.hasBomb() && gameEngine.hasAdversaryWithFewCards()) {
                    Bomb bomb = cp.getWeakestBombWithout2();
                    if (bomb != null) selected.addAll(bomb.getCards());
                } else if (cp.hasSpecialCard() && hand.size() < 2) {
                    selected.add(cp.getSpecialCard());
                } else if (cp.hasDoubleJoker()) {
                    selected.addAll(cp.getDoubleJoker());
                }
            }
        }

        // === CASE 2: Bot responds to an existing combination ===
        else {
            if (cp.isDoubleJoker(lastPlayedCombinaison)) {
                if (cp.hasDoubleJoker()) {
                    selected.addAll(cp.getDoubleJoker());
                } else {
                    return new ArrayList<>();
                }
            }

            Bomb lastBomb = new Bomb(lastPlayedCombinaison);
            DoubleCard lastDouble = new DoubleCard(lastPlayedCombinaison);
            Serie lastSerie = new Serie(lastPlayedCombinaison);
            boolean isSingle = lastPlayedCombinaison.size() == 1;
            boolean isSpecial = isSingle && lastPlayedCombinaison.get(0).getNumber() == 20;

            if (isSingle && !isSpecial) { // Respond to a single card
                Card higher = cp.getNextHigherCard(lastPlayedCombinaison.get(0));
                if (higher != null && !isHighValueCard(higher, cp)) {
                    selected.add(higher);
                } else if (shouldPlaySpecialCard(bot, cp)) {
                    selected.add(cp.getSpecialCard());
                } else {
                    selected.addAll(playStrategicBomb(cp, hand.size(), lastPlayedCombinaison.size()));
                }
            } else if (lastDouble.isValid()) { // Respond to a double
                DoubleCard nextDouble = cp.getNextDouble(lastDouble);
                if (nextDouble != null && !shouldPlayBomb(cp, lastPlayedCombinaison)) {
                    selected.addAll(nextDouble.getCards());
                } else if (shouldPlaySpecialCard(bot, cp)) {
                    selected.add(cp.getSpecialCard());
                } else {
                    selected.addAll(playStrategicBomb(cp, hand.size(), lastPlayedCombinaison.size()));
                }
            } else if (lastSerie.isValid()) { // Respond to a serie
                Serie nextSerie = cp.getNextSerie(lastSerie);
                if (nextSerie != null && nextSerie.getCards().size() <= lastSerie.getCards().size() + 1 && !shouldPlayBomb(cp, lastPlayedCombinaison)) {
                    selected.addAll(nextSerie.getCards());
                } else if (shouldPlaySpecialCard(bot, cp)) {
                    selected.add(cp.getSpecialCard());
                } else {
                    selected.addAll(playStrategicBomb(cp, hand.size(), lastPlayedCombinaison.size()));
                }
            } else if (isSpecial) { // Respond to a special card
                if (cp.hasSpecialCard()) {
                    selected.add(cp.getSpecialCard());
                } else {
                    selected.addAll(playStrategicBomb(cp, hand.size(), lastPlayedCombinaison.size()));
                }
            } else if (lastBomb.isValid()) { // Respond to a bomb
                Bomb nextBomb = cp.getNextBomb(lastBomb);
                if (nextBomb != null && hand.size() > nextBomb.getCards().size() + 2) {
                    selected.addAll(nextBomb.getCards());
                } else if (shouldUseDoubleJoker(bot, cp)) {
                    selected.addAll(cp.getDoubleJoker());
                }
            }
        }

        return selected;
    }

    /**
     * Checks if a card is considered a high-value card.
     *
     * @param card The card to evaluate.
     * @param cp The current CombinaisonProcess.
     * @return true if the card is strong (15 or higher), false otherwise.
     */
    private boolean isHighValueCard(Card card, CombinaisonProcess cp) {
        return card.getNumber() >= 15;
    }

    /**
     * Determines if a strong serie should be played.
     *
     * @param cp The current CombinaisonProcess.
     * @return true if a strong serie should be played, false otherwise.
     */
    private boolean shouldPlayStrongSerie(CombinaisonProcess cp) {
        Serie bestSerie = cp.getBestSerie();
        return bestSerie != null && bestSerie.getCards().size() >= 5 && gameEngine.hasAdversaryWithFewCards();
    }

    /**
     * Plays a bomb strategically if conditions allow.
     *
     * @param cp The current CombinaisonProcess.
     * @param handSize The bot's hand size.
     * @param lastCombinationSize The size of the last played combination.
     * @return A list of cards representing the bomb, or an empty list.
     */
    private ArrayList<Card> playStrategicBomb(CombinaisonProcess cp, int handSize, int lastCombinationSize) {
        if (cp.hasBomb() && handSize > lastCombinationSize + 2) {
            Bomb bomb = cp.getWeakestBombWithout2();
            if (bomb != null && bomb.getCards().size() <= 4) {
                return new ArrayList<>(bomb.getCards());
            }
        }
        return new ArrayList<>();
    }

    /**
     * Détermine s'il est préférable de jouer une bombe dans la situation actuelle.
     *
     * Conditions :
     * - Le bot possède une bombe.
     * - Un adversaire a peu de cartes restantes.
     * - La bombe a une valeur équivalente à la combinaison jouée (double ou série).
     *
     * @param cp L'instance de CombinaisonProcess pour analyser les combinaisons du bot.
     * @param lastPlayedCombinaison La dernière combinaison jouée à la table.
     * @return true si une bombe doit être jouée, false sinon.
     */
    public boolean shouldPlayBomb (CombinaisonProcess cp,ArrayList<Card> lastPlayedCombinaison){
        boolean hasBomb = cp.hasBomb();
        boolean hasDouble = cp.hasDouble();
        boolean hasSerie = cp.hasSerie();

        if(hasBomb && cp.getHand().size()<6){
            Bomb bomb = cp.getBestBomb();
            int bombValue = bomb.getValue();
            if (hasDouble){
                DoubleCard doubleCard = cp.getNextDouble(new DoubleCard(lastPlayedCombinaison));
                int doubleValue = doubleCard.getValue();
                if (doubleValue == bombValue){
                    return true; // on joue une bombe a l aplace d'un double
                }
            }
            if(hasSerie){
                Serie serie = cp.getNextSerie(new Serie(lastPlayedCombinaison));
                int serieValue = serie.getValue();
                if(bombValue== serieValue){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Détermine si une carte spéciale (2) doit être jouée par le bot.
     *
     * Conditions :
     * - Le bot possède une carte spéciale.
     * - Le bot possède également un double joker ou une bombe.
     * - Le bot a peu de cartes restantes ou un adversaire a peu de cartes restantes.
     *
     * @param bot Le bot en train de jouer.
     * @param cp L'instance de CombinaisonProcess pour évaluer la main du bot.
     * @return true si une carte spéciale doit être jouée, false sinon.
     */
    private boolean shouldPlaySpecialCard(BotPlayer bot, CombinaisonProcess cp) {
        if (cp.hasSpecialCard()) {
            Hand hand = bot.getHand();
            if(cp.hasDoubleJoker() || cp.hasBomb()){
                return true;
            }
            if((hand.size()<=5) || gameEngine.hasAdversaryWithFewCards()){
                return true;
            }
        }
        return false;
    }

    /**
     * Détermine si le double joker doit être utilisé par le bot.
     *
     * Conditions :
     * - Le bot possède un double joker.
     * - Le bot a très peu de cartes restantes ou un adversaire a peu de cartes restantes.
     *
     * @param bot Le bot en train de jouer.
     * @param cp L'instance de CombinaisonProcess pour analyser la main du bot.
     * @return true si le double joker doit être utilisé, false sinon.
     */
    private boolean shouldUseDoubleJoker(BotPlayer bot, CombinaisonProcess cp) {
        return cp.hasDoubleJoker() && (
                bot.getHand().size() <= 4 ||
                        gameEngine.hasAdversaryWithFewCards()
        );
    }
}
