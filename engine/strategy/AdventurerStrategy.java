package engine.strategy;

import data.cards.*;
import data.players.Hand;
import engine.CombinaisonProcess;
import engine.GameEngine;

import java.util.ArrayList;

/**
 * Strategy for an "Adventurer" bot.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public class AdventurerStrategy implements Strategy {

    private GameEngine gameEngine;

    /**
     * Constructor for AdventurerStrategy.
     *
     * @param gameEngine The game engine instance.
     */
    public AdventurerStrategy(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    /**
     * Selects the best move according to the Adventurer behavior:
     * - Prioritize strongest combinations and special cards.
     *
     * @param bot The bot player.
     * @param lastPlayedCombination The last combination played on the table.
     * @return List of cards selected to play.
     */
    @Override
    public ArrayList<Card> playTurn(BotPlayer bot, ArrayList<Card> lastPlayedCombination) {
        Hand hand = bot.getHand();
        ArrayList<Card> selected = new ArrayList<>();
        CombinaisonProcess cp = new CombinaisonProcess(hand);

        // First move of the round
        if (lastPlayedCombination == null || lastPlayedCombination.isEmpty()) {

            ArrayList<Card> winningCombination = cp.findWinningCombination();
            if (winningCombination != null && winningCombination.size()!=0) {
                selected.addAll(winningCombination);
                return selected;
            }

            if (cp.hasDoubleJoker()) return cp.getDoubleJoker();
            else if (cp.hasBomb() && cp.getBestBomb() != null) return new ArrayList<>(cp.getBestBomb().getCards());
            else if (cp.hasSpecialCard()) selected.add(cp.getSpecialCard());
            else if (cp.hasSerie() && cp.getBestSerie() != null) return new ArrayList<>(cp.getBestSerie().getCards());
            else if (cp.hasDouble() && cp.getBestDouble() != null) return new ArrayList<>(cp.getBestDouble().getCards());
            else if (!hand.getCards().isEmpty()) selected.add(cp.getHighestCard());

        } else { // Not the first move
            if (cp.hasDoubleJoker()) return cp.getDoubleJoker();

            boolean isSingle = lastPlayedCombination.size() == 1;
            boolean hasSpecial = cp.hasSpecialCard();

            if (!cp.isDoubleJoker(lastPlayedCombination)) {

                Bomb lastBomb = new Bomb(lastPlayedCombination);
                DoubleCard lastDouble = new DoubleCard(lastPlayedCombination);
                Serie lastSerie = new Serie(lastPlayedCombination);

                if (lastBomb.isValid()) {
                    Bomb better = cp.getNextBomb(lastBomb);
                    if (better != null && !better.getCards().isEmpty()) return new ArrayList<>(better.getCards());
                }
                else if (lastDouble.isValid()) {
                    DoubleCard better = cp.getNextDouble(lastDouble);
                    if (better != null && !better.getCards().isEmpty()) return new ArrayList<>(better.getCards());

                    Bomb bomb = cp.getBestBomb();
                    if (bomb != null && !bomb.getCards().isEmpty() && bomb.getCards().get(0).getNumber() != 20)
                        return new ArrayList<>(bomb.getCards());

                    if (hasSpecial) selected.add(cp.getSpecialCard());
                }
                else if (lastSerie.isValid()) {
                    Serie better = cp.getNextSerie(lastSerie);
                    if (better != null && !better.getCards().isEmpty()) return new ArrayList<>(better.getCards());

                    Bomb bomb = cp.getBestBomb();
                    if (bomb != null && !bomb.getCards().isEmpty() && bomb.getCards().get(0).getNumber() != 20)
                        return new ArrayList<>(bomb.getCards());
                }
                else if (isSingle) {
                    Card better = cp.getNextHigherCard(lastPlayedCombination.get(0));
                    if (better != null) selected.add(better);
                    else {
                        Bomb bomb = cp.getBestBomb();
                        if (bomb != null && !bomb.getCards().isEmpty() && bomb.getCards().get(0).getNumber() != 20)
                            return new ArrayList<>(bomb.getCards());

                        if (hasSpecial && !cp.hasDoubleJoker()) selected.add(cp.getSpecialCard());
                    }
                }
            }
        }

        // No valid move found -> pass
        return selected.isEmpty() ? new ArrayList<>() : selected;
    }
}
