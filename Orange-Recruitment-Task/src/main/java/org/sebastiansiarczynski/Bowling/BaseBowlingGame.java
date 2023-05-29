package org.sebastiansiarczynski.Bowling;

import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.sebastiansiarczynski.Bowling.Interfaces.BowlingGame;
import org.sebastiansiarczynski.Bowling.Interfaces.BowlingGameFrameHandler;
import org.sebastiansiarczynski.Bowling.Interfaces.BowlingGameScoreChart;
import org.sebastiansiarczynski.Bowling.Models.Player;

@RequiredArgsConstructor
public class BaseBowlingGame implements BowlingGame {

  private final Player player;
  private final BowlingGameScoreChart bowlingGameScoreChart;
  private final BowlingGameFrameHandler bowlingGameFrameHandler;

  @Override
  public void roll(int pins) {

    final int leftPins = ThreadLocalRandom.current().nextInt(pins + 1);
  }

  @Override
  public int calculateScore() {
    return bowlingGameScoreChart.getScoreForPlayer(player.getId());
  }

  private void checkIfStrike(final int leftPins) {
    if (leftPins > 0 && bowlingGameFrameHandler.getAttemptsForPlayer(player.getId()) > 0) {

    }
  }
}
