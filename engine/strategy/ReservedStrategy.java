package engine.strategy;

import data.game.GameTable;

public class ReservedStrategy implements Strategy {
    @Override
    public void playTurn(BotPlayer bot, GameTable gameTable) {
        // Logique : Jouer seulement si c'est avantageux
        // Sinon, piocher et attendre le bon moment
    }
}

