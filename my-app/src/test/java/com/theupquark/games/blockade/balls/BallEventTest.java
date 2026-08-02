package com.theupquark.games.blockade.balls;

import java.util.Random;

import com.theupquark.games.blockade.event.Event;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BallEventTest {

  @Test
  public void testLeftSideBounceEvent() {
    Ball ball = new Ball(100, 100, new Random());
    ball.registerBorderBounceEvents();

    ball.setCenterX(5.0); // Inside left border (radius = 10)
    ball.setVelocityX(-4.0);

    ball.trigger(null, Event.GamePhase.HitBorder);

    assertEquals(4.0, ball.getVelocityX(), 0.001, "Velocity X should be inverted after hitting left border");
  }

  @Test
  public void testRightSideBounceEvent() {
    Ball ball = new Ball(100, 100, new Random());
    ball.registerBorderBounceEvents();

    ball.setCenterX(895.0); // Inside right border (radius = 10, width = 900 default)
    ball.setVelocityX(6.0);

    ball.trigger(null, Event.GamePhase.HitBorder);

    assertEquals(-6.0, ball.getVelocityX(), 0.001, "Velocity X should be inverted after hitting right border");
  }

  @Test
  public void testNoSideBounceInCenter() {
    Ball ball = new Ball(450, 300, new Random());
    ball.registerBorderBounceEvents();

    ball.setVelocityX(5.0);
    ball.trigger(null, Event.GamePhase.HitBorder);

    assertEquals(5.0, ball.getVelocityX(), 0.001, "Velocity X should remain unchanged when ball is in center");
  }
}
