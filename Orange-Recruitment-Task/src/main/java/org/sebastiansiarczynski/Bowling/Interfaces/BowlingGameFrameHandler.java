package org.sebastiansiarczynski.Bowling.Interfaces;

public interface BowlingGameFrameHandler {

  int getAttemptsForPlayer(String playerId);

  int updateFrameScoreForPlayer(String playerId, int score);

  int getFrameIdForPlayer(String playerId);

  void markFrameAsStrike(String playerId, String frameId);

  void markFrameAsSpare(String playerId, String frameId);

  void checkIfNeedToSetBonusForStrike(String playerId, String frameId);

  void checkIfNeedToSetBonusForSpare(String playerId, String frameId);

}
