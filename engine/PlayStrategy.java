package engine;

import players.BotPlayer;
import game.GameTable;

public interface PlayStrategy {
    void play(GameEngine gameEngine, BotPlayer bot, GameTable gameTable);
}
