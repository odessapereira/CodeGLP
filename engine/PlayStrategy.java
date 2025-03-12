package players;

import engine.GameEngine;
import game.GameTable;

public interface PlayStrategy {
    void play(GameEngine gameEngine, BotPlayer bot, GameTable gameTable);
}