package engine.strategy;

import data.game.GameTable;

public interface Strategy {
    void playTurn(BotPlayer bot, GameTable gameTable);
}

