package engine;

import data.cards.*;
import data.players.Player;
import engine.strategy.BotPlayer;

import java.util.ArrayList;
import java.util.List;

public class TurnManager {
    private List<Player> players;
    private int currentPlayerIndex;
    private boolean gameOver;

    public TurnManager(List<Player> players) {
        this.players = players;
        this.currentPlayerIndex = 0;
        this.gameOver = false;
    }

    public TurnManager (){
        this.currentPlayerIndex = 0;
        this.gameOver = false;
    }

    public boolean isValidMove(ArrayList<Card> selectedCards) {
        if (selectedCards.isEmpty()) {
            return false; // Aucune carte sélectionnée
        }

        // Vérifier si le joueur tente de poser un joker seul
        for (Card card : selectedCards) {
            if (card.isJoker(card) && selectedCards.size()==1) {
                return false; // Un joker ne peut pas être posé seul
            }
        }

        Combinaison serie = new Serie(selectedCards);
        Combinaison bomb = new Bomb(selectedCards);
        Combinaison doubleCards = new DoubleCard(selectedCards);

        if (serie.isValid() || bomb.isValid() || doubleCards.isValid()){
            return true;
        }

        // Si ce n'est pas une combinaison, les cartes doivent être posées individuellement
        return selectedCards.size() == 1; // Un joueur peut poser une seule carte non-joker
    }




    /**
     * Démarre le tour du joueur actuel.
     */
    public void startTurn() {
        if (gameOver) {
            System.out.println("Le jeu est terminé.");
            return;
        }

        Player currentPlayer = players.get(currentPlayerIndex);

        if (currentPlayer instanceof BotPlayer) {
            ((BotPlayer) currentPlayer).playTurn();
            endTurn();
        } else {
            System.out.println("En attente du joueur humain...");
        }
    }



    /**
     * Joue un tour pour le joueur donné.
     */
    public void playTurn(int playerId, ArrayList<Card> selectedCards) {
        Player player = players.get(playerId);

        if (isValidMove(selectedCards)) {
//            player.playTurn(selectedCards);
            endTurn();
        } else {
            System.out.println("Coup invalide. Essayez encore.");
        }
    }


    /**
     * Termine le tour actuel et passe au joueur suivant.
     */
    private void endTurn() {
        if (players.get(currentPlayerIndex).getHand().isEmpty()) {
            gameOver = true;
            System.out.println(players.get(currentPlayerIndex).getName() + " a gagné !");
            return;
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        startTurn();
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
