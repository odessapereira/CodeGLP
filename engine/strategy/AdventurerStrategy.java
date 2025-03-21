package engine.strategy;

import data.cards.Card;
import data.game.GameTable;
import data.players.Hand;
import engine.CombinaisonProcess;
import engine.GameEngine;

import java.util.ArrayList;
import java.util.List;

public class AdventurerStrategy implements Strategy {
    private Hand hand;
    private GameEngine gameEngine = new GameEngine();
    private CombinaisonProcess combinaisonProcess = new CombinaisonProcess(hand);

    @Override
    public void playTurn(BotPlayer bot, GameTable gameTable ) {
        Hand hand = bot.getHand();  // Récupère la main du bot
        Card topCard = gameTable.getLastPlayedCard();  // Carte actuelle sur la table
        ArrayList <Card> selectedCards =new ArrayList<Card>();

        // 1. Jouer la plus grosse carte spéciale (2)
        if (hand.hasSpecialCard()) {
            selectedCards.add(hand.getSpecialCard());
            gameEngine.playSelectedCards(selectedCards,bot);
            return;
        }

        // 2. Jouer la plus grosse combinaison possible
        if (combinaisonProcess.hasBomb()) {
            for (Card card : combinaisonProcess.getBestBomb().getCards()){
                selectedCards.add(card);
            }
            gameEngine.playSelectedCards(selectedCards,bot);
            return;
        }
        if (combinaisonProcess.hasSerie()) {
            for (Card card : combinaisonProcess.getBestSerie().getCards()){
                selectedCards.add(card);
            }
            gameEngine.playSelectedCards(selectedCards,bot);
            return;
        }
        if (combinaisonProcess.hasDouble()) {
            for (Card card : combinaisonProcess.getBestDouble().getCards()){
                selectedCards.add(card);
            }
            gameEngine.playSelectedCards(selectedCards,bot);
            return;
        }

        // 3. Jouer la carte suivante si possible
        if (combinaisonProcess.hasNextCard(topCard)) {
            selectedCards.add(combinaisonProcess.getNextCard(topCard));
            gameEngine.playSelectedCards(selectedCards,bot);
            return;
        }

        // 4. Si aucune action possible, passer le tour
//        bot.passTurn();
    }



}
