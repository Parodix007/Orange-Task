package org.sebastiansiarczynski.Bowling.Interfaces;

public interface BowlingGame {

  void roll(int pins);

  default int calculateScore() {
     return 0;
  };
}
