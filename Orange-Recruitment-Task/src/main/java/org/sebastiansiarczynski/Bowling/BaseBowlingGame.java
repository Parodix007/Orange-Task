package org.sebastiansiarczynski.Bowling;

import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sebastiansiarczynski.Bowling.Interfaces.BowlingGame;
import org.sebastiansiarczynski.Bowling.Interfaces.BowlingGameFrameHandler;
import org.sebastiansiarczynski.Bowling.Interfaces.BowlingGameScoreChart;
import org.sebastiansiarczynski.Bowling.Models.Player;
import org.sebastiansiarczynski.Bowling.exceptions.BowlingGameExceptions;

@RequiredArgsConstructor
@Slf4j
public class BaseBowlingGame implements BowlingGame {

  private static final int MAX_SCORE_ONE_ATTEMPT = 10;
  private final Player player;
  private final BowlingGameScoreChart bowlingGameScoreChart;
  private final BowlingGameFrameHandler bowlingGameFrameHandler;

  @Override
  public void roll(int pins) throws BowlingGameExceptions {
    try {
      final int score = ThreadLocalRandom.current().nextInt(pins + 1);

      log.info("Score for a roll {}", score);

      final int leftPins = pins - score;

      log.info("Left pins after a roll {}", leftPins);

      if (score == MAX_SCORE_ONE_ATTEMPT) {
        log.info("PlayerId {} scored a strike", player.getId());

        updateForStrike();
        checkBonus();

        return;
      }

      final int attemptsForPlayer = bowlingGameFrameHandler.getAttemptsForPlayer(player.getId());

      if (attemptsForPlayer > 0) {
        log.info("Left attempts {} for a playerId {}", attemptsForPlayer, player.getId());

        bowlingGameFrameHandler.updateFrameScoreForPlayer(player.getId(),
            bowlingGameFrameHandler.getFrameIdForPlayer(player.getId()), score);

        roll(leftPins);

        return;
      }

      checkBonus();
    } catch (final Exception error) {
      log.error("Error while handling a roll for a playerId {}", player.getId(), error);

      throw new BowlingGameExceptions("Error while roll", error);
    }
  }

  @Override
  public int calculateScore() throws BowlingGameExceptions {
    try {
      log.info("Getting a score for a playerId {}", player.getId());

      return bowlingGameScoreChart.getScoreForPlayer(player.getId());
    } catch (final Exception error) {
      log.error("Error while getting a score for a playerId {}", player.getId(), error);

      throw new BowlingGameExceptions("Error while getting a score for a player", error);
    }
  }

  private void updateForStrike() throws BowlingGameExceptions {
    try {
      log.info("Updating score {} for a strike for a playerId {}", MAX_SCORE_ONE_ATTEMPT,
          player.getId());

      bowlingGameFrameHandler.markFrameAsStrike(player.getId(),
          bowlingGameFrameHandler.getFrameIdForPlayer(player.getId()));

      bowlingGameFrameHandler.updateFrameScoreForPlayer(player.getId(),
          bowlingGameFrameHandler.getFrameIdForPlayer(player.getId()), MAX_SCORE_ONE_ATTEMPT);
    } catch (final Exception error) {
      log.error("Error while updating for a strike for a playerId {}", player.getId());

      throw new BowlingGameExceptions("Error while updating for a strike", error);
    }
  }

  private void checkBonus() throws BowlingGameExceptions {
    try {
      log.info("Checking bonus for a roll for a playerId {}", player.getId());

      bowlingGameFrameHandler.checkIfNeedToSetBonusForSpare(player.getId(),
          bowlingGameFrameHandler.getFrameIdForPlayer(player.getId()));

      bowlingGameFrameHandler.checkIfNeedToSetBonusForStrike(player.getId(),
          bowlingGameFrameHandler.getFrameIdForPlayer(player.getId()));
    } catch (final Exception error) {
      log.error("Error while updating for a roll for a playerId {}", player.getId());

      throw new BowlingGameExceptions("Error while updating for a roll", error);
    }
  }
}
