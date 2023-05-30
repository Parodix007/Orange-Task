package org.sebastiansiarczynski.Bowling.Interfaces;

import org.sebastiansiarczynski.Bowling.score.exceptions.BowlingGameScoreChartException;

public interface BowlingGameScoreChart {

  int getScoreForPlayer(String playerId) throws BowlingGameScoreChartException;

  void updateScoreForPlayer(String playerId, int score) throws BowlingGameScoreChartException;

}
