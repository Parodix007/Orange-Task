package org.sebastiansiarczynski.Bowling.Interfaces;

import org.sebastiansiarczynski.Bowling.exceptions.BowlingGameExceptions;

public interface BowlingGame {

  void roll(int pins) throws BowlingGameExceptions;

  default int calculateScore() throws BowlingGameExceptions {
    return 0;
  }
}
