package org.sebastiansiarczynski.Bowling;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.sebastiansiarczynski.Bowling.Interfaces.BowlingGame;
import org.sebastiansiarczynski.Bowling.Interfaces.BowlingGameFrameHandler;
import org.sebastiansiarczynski.Bowling.Interfaces.BowlingGameScoreChart;
import org.sebastiansiarczynski.Bowling.Models.Player;
import org.sebastiansiarczynski.Bowling.exceptions.BowlingGameExceptions;
import org.sebastiansiarczynski.Bowling.frame.exceptions.BowlingGameFrameException;
import org.sebastiansiarczynski.Bowling.score.exceptions.BowlingGameScoreChartException;

class BaseBowlingGameTest {

  private Player player;
  @Mock
  private BowlingGameScoreChart bowlingGameScoreChart;
  @Mock
  private BowlingGameFrameHandler bowlingGameFrameHandler;
  @Mock
  private ThreadLocalRandom threadLocalRandom;

  private BowlingGame objectUnderTest;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    final String playerId = UUID.randomUUID().toString();

    player = new Player(playerId);

    objectUnderTest = new BaseBowlingGame(player, bowlingGameScoreChart, bowlingGameFrameHandler);
  }

  @AfterEach
  void tearDown() {
    Mockito.clearAllCaches();
  }

  @Test
  @DisplayName("Should properly perform a roll")
  void roll() throws BowlingGameFrameException {
    //given
    final int initPins = 10;
    final String frameId = UUID.randomUUID().toString();
    final int randomScore = 4;

    //when
    Mockito.when(bowlingGameFrameHandler.getAttemptsForPlayer(Mockito.anyString()))
        .thenReturn(2, 1, 0);
    Mockito.when(bowlingGameFrameHandler.updateFrameScoreForPlayer(Mockito.anyString(),
        Mockito.anyString(), Mockito.anyInt())).thenReturn(2, 1, 0);
    Mockito.when(bowlingGameFrameHandler.getFrameIdForPlayer(Mockito.anyString()))
        .thenReturn(frameId);

    Mockito.doNothing().when(bowlingGameFrameHandler)
        .checkIfNeedToSetBonusForSpare(Mockito.anyString(), Mockito.anyString());
    Mockito.doNothing().when(bowlingGameFrameHandler)
        .checkIfNeedToSetBonusForStrike(Mockito.anyString(), Mockito.anyString());

    try (MockedStatic<ThreadLocalRandom> threadLocalRandomMockedStatic =
        Mockito.mockStatic(ThreadLocalRandom.class)) {
      threadLocalRandomMockedStatic.when(ThreadLocalRandom::current).thenReturn(threadLocalRandom);
      Mockito.when(threadLocalRandom.nextInt(Mockito.anyInt())).thenReturn(randomScore);

      //then
      assertDoesNotThrow(() -> objectUnderTest.roll(initPins));

      Mockito.verify(bowlingGameFrameHandler, Mockito.times(3))
          .getAttemptsForPlayer(Mockito.anyString());
      Mockito.verify(bowlingGameFrameHandler, Mockito.times(2))
          .updateFrameScoreForPlayer(Mockito.anyString(),
              Mockito.anyString(), Mockito.anyInt());
      Mockito.verify(bowlingGameFrameHandler, Mockito.times(4))
          .getFrameIdForPlayer(Mockito.anyString());
    }
  }

  @Test
  @DisplayName("Should properly perform a roll while strikes a STRIKE")
  void rollStrike() throws BowlingGameFrameException {
//given
    final int initPins = 10;
    final String frameId = UUID.randomUUID().toString();
    final int randomScore = 10;

    //when
    Mockito.when(bowlingGameFrameHandler.updateFrameScoreForPlayer(Mockito.anyString(),
        Mockito.anyString(), Mockito.anyInt())).thenReturn(2, 1, 0);
    Mockito.when(bowlingGameFrameHandler.getFrameIdForPlayer(Mockito.anyString()))
        .thenReturn(frameId);

    Mockito.doNothing().when(bowlingGameFrameHandler)
        .checkIfNeedToSetBonusForSpare(Mockito.anyString(), Mockito.anyString());
    Mockito.doNothing().when(bowlingGameFrameHandler)
        .checkIfNeedToSetBonusForStrike(Mockito.anyString(), Mockito.anyString());
    Mockito.doNothing().when(bowlingGameFrameHandler)
        .markFrameAsStrike(Mockito.anyString(), Mockito.anyString());

    try (MockedStatic<ThreadLocalRandom> threadLocalRandomMockedStatic =
        Mockito.mockStatic(ThreadLocalRandom.class)) {
      threadLocalRandomMockedStatic.when(ThreadLocalRandom::current).thenReturn(threadLocalRandom);
      Mockito.when(threadLocalRandom.nextInt(Mockito.anyInt())).thenReturn(randomScore);

      //then
      assertDoesNotThrow(() -> objectUnderTest.roll(initPins));

      Mockito.verify(bowlingGameFrameHandler, Mockito.times(1))
          .updateFrameScoreForPlayer(Mockito.anyString(),
              Mockito.anyString(), Mockito.anyInt());
      Mockito.verify(bowlingGameFrameHandler, Mockito.times(4))
          .getFrameIdForPlayer(Mockito.anyString());
    }
  }

  @Test
  @DisplayName("Should throw when any error occurs while roll")
  void rollThrows() throws BowlingGameFrameException {
    //given
    final int initPins = 10;
    final int randomScore = 4;

    //when
    Mockito.when(bowlingGameFrameHandler.getAttemptsForPlayer(Mockito.anyString()))
        .thenThrow(BowlingGameFrameException.class);

    try (MockedStatic<ThreadLocalRandom> threadLocalRandomMockedStatic =
        Mockito.mockStatic(ThreadLocalRandom.class)) {
      threadLocalRandomMockedStatic.when(ThreadLocalRandom::current).thenReturn(threadLocalRandom);
      Mockito.when(threadLocalRandom.nextInt(Mockito.anyInt())).thenReturn(randomScore);

      //then
      assertThrows(BowlingGameExceptions.class, () -> objectUnderTest.roll(initPins));

      Mockito.verify(bowlingGameFrameHandler, Mockito.times(1))
          .getAttemptsForPlayer(Mockito.anyString());
    }
  }

  @Test
  @DisplayName("Should properly return a player score")
  void calculateScore() throws BowlingGameScoreChartException, BowlingGameExceptions {
    //given
    final int playerScore = 90;

    //when
    Mockito.when(bowlingGameScoreChart.getScoreForPlayer(Mockito.anyString()))
        .thenReturn(playerScore);

    //then
    assertEquals(objectUnderTest.calculateScore(), playerScore);

    Mockito.verify(bowlingGameScoreChart, Mockito.times(1)).getScoreForPlayer(Mockito.anyString());
  }

  @Test
  @DisplayName("Should throw when error while getting score for a player")
  void calculateScoreThrows() throws BowlingGameScoreChartException {
    //when
    Mockito.when(bowlingGameScoreChart.getScoreForPlayer(Mockito.anyString())).thenThrow(
        BowlingGameScoreChartException.class);

    //then
    assertThrows(BowlingGameExceptions.class, () -> objectUnderTest.calculateScore());

    Mockito.verify(bowlingGameScoreChart, Mockito.times(1)).getScoreForPlayer(Mockito.anyString());
  }
}