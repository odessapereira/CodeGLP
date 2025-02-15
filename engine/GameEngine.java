package engine;

import data.cards.Card;
import data.game.DiscardPile;
import data.game.DrawPile;
import data.game.GameTable;
import data.players.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GameEngine {

        private List<Player> players;
        private DrawPile drawPile;
        private DiscardPile discardPile;
        private GameTable gameTable;
        private int currentTurn;
        private boolean isGameOver;

        public GameEngine(List<Player> players) {
            this.players = players;
            this.drawPile = new DrawPile();
            this.discardPile = new DiscardPile();
            this.gameTable = new GameTable();
            this.currentTurn = 0;
            this.isGameOver = false;
        }
        /**
         * Initializes and starts the game.
         */
        public void startGame() {
            initializeGame();
            dealCards();
        }

        /**
         * Initializes the game state, setting up the deck, players, and starting conditions.
         */
        private void initializeGame() {
            drawPile.shuffle();
            currentTurn = 0;
            isGameOver = false;
        }

    /**
     * Distributes cards to all players at the beginning of the game.
     */
    private void dealCards() {
        for (Player player : players) {
            for (int i = 0; i < 5; i++) {
                Card drawnCard = drawPile.drawCard();
                if (drawnCard != null) {
                    player.getHand().add(drawnCard);
                } else {
                    System.out.println("Draw pile is empty! Unable to deal more cards.");
                    return;
                }
            }
        }
    }



    /**
     * Allows a player to draw a card from the deck.
     */
    public void drawCard(int playerId) {
        Card drawnCard = drawPile.drawCard();
        if (drawnCard != null) {
            players.get(playerId).getHand().add(drawnCard);
        } else {
            System.out.println("Draw pile is empty! Unable to draw more cards.");
        }
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
