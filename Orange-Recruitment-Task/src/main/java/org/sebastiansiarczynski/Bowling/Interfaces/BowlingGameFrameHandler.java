package org.sebastiansiarczynski.Bowling.Interfaces;

import org.sebastiansiarczynski.Bowling.frame.exceptions.BowlingGameFrameException;

public interface BowlingGameFrameHandler {

  int getAttemptsForPlayer(String playerId) throws BowlingGameFrameException;

  int updateFrameScoreForPlayer(String playerId, String frameId, int score)
      throws BowlingGameFrameException;

  String getFrameIdForPlayer(String playerId) throws BowlingGameFrameException;

  void markFrameAsStrike(String playerId, String frameId) throws BowlingGameFrameException;

  void checkIfNeedToSetBonusForStrike(String playerId, String frameId)
      throws BowlingGameFrameException;

  void checkIfNeedToSetBonusForSpare(String playerId, String frameId)
      throws BowlingGameFrameException;

}
