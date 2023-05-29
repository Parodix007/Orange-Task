package org.sebastiansiarczynski.Bowling.Interfaces;

public interface BowlingGameScoreChart {

  int getScoreForPlayer(String playerId);

  void updateScoreForPlayer(String playerId, int score);

}
