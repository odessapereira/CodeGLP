package engine;

import data.players.Player;

import java.util.List;
import java.util.Optional;

public class TurnManager {
    private List<Player> players;
    private int currentPlayerIndex;
    private boolean gameOver;

    public TurnManager(List<Player> players) {
        this.players = players;
        this.currentPlayerIndex = 0;
        this.gameOver = false;
    }

    /**
     * Démarre un tour pour le joueur actuel.
     */
    public void startTurn() {
        if (gameOver) {
            System.out.println("Le jeu est terminé. Aucun tour ne peut être commencé.");
            return;
        }

        Player currentPlayer = players.get(currentPlayerIndex);
        System.out.println("C'est au tour de " + currentPlayer.getName() + ".");
        currentPlayer.playTurn();

        // Vérifier si le joueur a gagné après son tour
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getHand().isEmpty()) {
                gameOver = true;
            }
        }
        // Passe au joueur suivant
        nextTurn();
    }



    /**
     * Handles the logic for a player's turn.
     */
    public void playTurn(int playerId) {
        Player player = players.get(playerId);
        player.playTurn();
        nextTurn();
    }

    /**
     * Passe au joueur suivant et démarre son tour.
     */
    private void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        startTurn();
    }


    /**
     * Retourne l'index du joueur dont c'est actuellement le tour.
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Permet de savoir si la partie est terminée.
     * @return true si le jeu est terminé, false sinon.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Accède à la liste des joueurs du jeu.
     */
    public List<Player> getPlayers() {
        return players;
    }
}
