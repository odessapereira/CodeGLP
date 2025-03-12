package players;

import gui.GameGUI;
import engine.GameEngine;
import game.GameTable;
import cards.Card;
import java.util.List; 
/*
 * un joueur offensif va jouer ses meilleures combinaisons directement, 
 * va poser des doubles quand ils commencent un tour, 
 * va poser un 2 directement si il en a 
 */
public class OffensiveStrategy implements PlayStrategy {
    @Override
    public void play(GameEngine gameEngine, BotPlayer bot, GameTable gameTable) {
        System.out.println(bot.getName() + " joue de manière offensive !");
        
        List<Card> hand = bot.getHand().getCards();
        if (hand.isEmpty()) {
            System.out.println(bot.getName() + " n'a plus de cartes !");
            return;
        }

        // play the best attack card on the gametable 
        Card bestAttackCard = findBestAttackCard(hand);

        if (bestAttackCard != null) {
            System.out.println(bot.getName() + " joue : " + bestAttackCard);
            //faudrait vérifier que la carte est jouable 
            GameGUI.playCard(bestAttackCard, gameTable);//je trouve pas la methode
        }
    }

    // Look for the best attack card in the bot hand 
    private Card findBestAttackCard(List<Card> hand) {
        if (hand.isEmpty()) return null;

        Card bestCard = hand.get(0);

        for (int i = 1; i < hand.size(); i++) {
            if (hand.get(i).getAttackValue() > bestCard.getAttackValue()) {
                bestCard = hand.get(i);
            }
        }

        return bestCard;
    }
}


