package engine;

import data.cards.*;
import data.game.GameTable;
import data.players.Player;
import engine.strategy.BotPlayer;

import java.util.*;

/**
 * Manages the turns of players in the game, including validation of moves and game state transitions.
 *
 * Author: Nadjib-M Fariza-A Odessa-T-P
 */
public class TurnManager {
    private List<Player> players;
    private int currentPlayerIndex;
    private boolean gameOver;
    private GameTable gameTable;
    private GameEngine gameEngine;
    private int nbTour;

    /**
     * Constructor for TurnManager.
     *
     * Initializes the turn manager with the list of players, game table, and game engine.
     *
     * @param players The list of players in the game.
     * @param gameTable The game table containing game state.
     * @param gameEngine The game engine managing overall game logic.
     */
    public TurnManager(List<Player> players, GameTable gameTable, GameEngine gameEngine) {
        this.players = players;
        this.currentPlayerIndex = 0;
        this.gameOver = false;
        this.gameTable = gameTable;
        this.gameEngine = gameEngine;
        this.nbTour = 0;
    }

    /**
     * Default constructor for TurnManager.
     *
     * Creates an empty turn manager without initializing players, game table, or game engine.
     */
    public TurnManager() {}

    /**
     * Validates whether the selected cards constitute a valid move.
     *
     * Checks the selected cards against the last played combination and game rules.
     *
     * @param selectedCards The cards selected by the player for the move.
     * @return True if the move is valid, false otherwise.
     */
    public boolean isValidMove(ArrayList<Card> selectedCards) {
        // Check if the selected cards are null or empty
        if (selectedCards == null || selectedCards.isEmpty()) {
            return false;
        }

        // Count jokers
        int nbJoker = 0;
        for (Card card : selectedCards) {
            if (card.isJoker()) {
                nbJoker++;
            }
        }

        // Count cards with number 20
        int nbCarte2 = 0;
        for (Card card : selectedCards) {
            if (card.getNumber() == 20) {
                nbCarte2++;
            }
        }

        // Check if a single joker is played (not allowed)
        if (selectedCards.size() == 1 && nbJoker == 1) {
            return false;
        }

        // Check if a double joker is played (always valid)
        if (selectedCards.size() == 2 && nbJoker == 2) {
            return true;
        }

        // Check if multiple cards with number 20 are played (not allowed)
        if (selectedCards.size() > 1 && nbCarte2 > 1) {
            return false;
        }

        // Retrieve the last played combination
        ArrayList<Card> lastCombinaison = gameTable.getDiscardPile().getLastCombinaison();

        // If it's the first turn (no cards played yet)
        if (lastCombinaison == null || lastCombinaison.isEmpty()) {
            // First turn: any valid combination can be played
            return selectedCards.size() == 1 ||
                    new Serie(selectedCards).isValid() ||
                    new Bomb(selectedCards).isValid() ||
                    new DoubleCard(selectedCards).isValid();
        }

        // Analyze the last played combination
        Bomb lastBomb = new Bomb(lastCombinaison);
        Serie lastSerie = new Serie(lastCombinaison);
        DoubleCard lastDouble = new DoubleCard(lastCombinaison);
        boolean isLastSingleCard = lastCombinaison.size() == 1;

        if (lastBomb.isValid()) {
            Bomb currentBomb = new Bomb(selectedCards);
            if (currentBomb.isValid()) {
                int lastValue = lastBomb.getValue();
                int currentValue = currentBomb.getValue();

                // Stronger if value is greater, or same value but more cards
                return (currentValue > lastValue)
                        || (currentValue == lastValue && selectedCards.size() > lastCombinaison.size());
            }
            return false;
        }

        if (selectedCards.size() == 1 && selectedCards.get(0).getNumber() == 20 && !lastBomb.isValid()) {
            return true;
        }

        // If the last combination was a double
        else if (lastDouble.isValid()) {
            // Option 1: Play a double that follows
            DoubleCard currentDouble = new DoubleCard(selectedCards);
            if (currentDouble.isValid()) {
                int lastValue = lastDouble.getValue();
                int currentValue = currentDouble.getValue();
                return currentValue == lastValue + 1;
            }

            // Option 2: Play a bomb (unless it contains a 2)
            Bomb currentBomb = new Bomb(selectedCards);
            if (currentBomb.isValid() && selectedCards.get(0).getNumber() != 20) {
                return true;
            }

            return false;
        }

        // If the last combination was a series
        else if (lastSerie.isValid() && lastSerie.getCards().size() >= 3) {
            // Sort the cards of the previous series to ensure order
            List<Card> lastCards = new ArrayList<>(lastSerie.getCards());
            Collections.sort(lastCards, Comparator.comparing(Card::getNumber));

            // Identify the joker and reconstruct possible sequences
            List<Integer> nonJokerValues = new ArrayList<>();
            int jokerIndex = -1;
            for (int i = 0; i < lastCards.size(); i++) {
                Card card = lastCards.get(i);
                if (card.isJoker()) {
                    jokerIndex = i;
                } else {
                    nonJokerValues.add(card.getNumber());
                }
            }

            // Generate possible sequences with the joker
            List<List<Integer>> possibleSequences = new ArrayList<>();
            if (jokerIndex != -1) {
                int size = lastCards.size();
                if (jokerIndex == 0) { // Joker at the start
                    // Case 1: Joker before
                    int startValueBefore = nonJokerValues.get(0) - 1;
                    List<Integer> sequenceBefore = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        sequenceBefore.add(startValueBefore + i);
                    }
                    possibleSequences.add(sequenceBefore);

                    // Case 2: Joker after
                    int startValueAfter = nonJokerValues.get(0);
                    List<Integer> sequenceAfter = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        sequenceAfter.add(startValueAfter + i);
                    }
                    possibleSequences.add(sequenceAfter);
                } else if (jokerIndex == size - 1) { // Joker at the end
                    // Case 1: Joker after
                    int startValueAfter = nonJokerValues.get(0);
                    List<Integer> sequenceAfter = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        sequenceAfter.add(startValueAfter + i);
                    }
                    possibleSequences.add(sequenceAfter);

                    // Case 2: Joker before
                    int startValueBefore = nonJokerValues.get(0) - (size - 1);
                    List<Integer> sequenceBefore = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        sequenceBefore.add(startValueBefore + i);
                    }
                    possibleSequences.add(sequenceBefore);
                }
            } else {
                possibleSequences.add(new ArrayList<>(nonJokerValues));
            }

            // Check the series selected by the player
            Serie currentSerie = new Serie(selectedCards);
            if (currentSerie.isValid() && selectedCards.size() >= 3) { // Minimum 3 cards, can be more
                int firstCardValue = selectedCards.get(0).getNumber();
                // Check if the first card matches an acceptable value
                for (List<Integer> sequence : possibleSequences) {
                    for (int i = 1; i < sequence.size(); i++) { // Except the first card
                        if (firstCardValue == sequence.get(i)) {
                            return true; // The series starts with a valid card and has at least 3 cards
                        }
                    }
                }

                return false;
            }
            // Option 2: Play a bomb (unless it contains a 2)
            Bomb currentBomb = new Bomb(selectedCards);
            if (currentBomb.isValid() && selectedCards.get(0).getNumber() != 2) {
                return true;
            }
        }

        // If the last combination was a single card
        else if (isLastSingleCard) {
            // Option 1: Play a single card that follows
            if (selectedCards.size() == 1) {
                int lastValue = lastCombinaison.get(0).getNumber();
                int currentValue = selectedCards.get(0).getNumber();
                return currentValue == lastValue + 1;
            }

            // Option 2: Play a bomb (unless it contains a 2)
            Bomb currentBomb = new Bomb(selectedCards);
            if (currentBomb.isValid() && selectedCards.get(0).getNumber() != 20) {
                return true;
            }

            return false;
        }

        return false;
    }

    /**
     * Starts the turn of the current player.
     *
     * Executes the turn logic for either a bot or human player and updates the game state.
     */
    public void startTurn() {
        Player currentPlayer = players.get(currentPlayerIndex);

        if (currentPlayer instanceof BotPlayer) {
            BotPlayer botplayer = (BotPlayer) currentPlayer;
            gameEngine.playBotTurn(botplayer);
            nbTour++;
            if (botplayer.getHand().isEmpty()) {
                gameOver = true;
                gameEngine.setWinner(currentPlayer.getName());
            }
        } else {
            gameEngine.playHumanTurn();
            System.out.println("Waiting for the human player...");
            nbTour++;
        }
    }

    /**
     * Ends the current turn and advances to the next player.
     *
     * Checks if the current player has won and updates the player index.
     */
    public void nextTurn() {
        startTurn();
        if (players.get(currentPlayerIndex).getHand().isEmpty()) {
            gameOver = true;
            System.out.println(players.get(currentPlayerIndex).getName() + " has won!");
        }
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    /**
     * Retrieves the index of the current player.
     *
     * @return The index of the current player.
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Generates a random player index.
     *
     * @return A random index between 0 and 3.
     */
    public int getRandomPlayerIndex() {
        Random rand = new Random();
        return rand.nextInt(4); // Generates a random integer between 0 (inclusive) and 4 (exclusive)
    }

    /**
     * Sets the index of the current player.
     *
     * @param currentPlayerIndex The index to set for the current player.
     */
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    /**
     * Checks if the game is over.
     *
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Retrieves the list of players in the game.
     *
     * @return The list of players.
     */
    public List<Player> getPlayers() {
        return players;
    }


    /**
     * Retrieves the number of turns played.
     *
     * @return The number of turns.
     */
    public int getNbTour() {
        return nbTour;
    }
}