package com.theupquark.games.blockade.bricks;

import com.theupquark.games.blockade.Blockade;
import com.theupquark.games.blockade.balls.Ball;
import com.theupquark.games.blockade.event.Event;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

public class LavaBrick extends Brick {

  public LavaBrick(double x, double y) {
    super(x, y, Color.YELLOW);
    setStrokeWidth(6.0);
    setStroke(Color.GREY);
    setStrokeType(StrokeType.INSIDE);

    // TODO simulate lava flow better. Maybe some spotty texture that moves left -> right, and can loop.
    FillTransition lavaPulse = new FillTransition(Duration.seconds(1), this, Color.YELLOW, Color.GOLDENROD);
    lavaPulse.setCycleCount(Animation.INDEFINITE);
    lavaPulse.setAutoReverse(true);
    lavaPulse.play();
  }

  @Override
  public boolean removeCondition(Ball instigator) {
    // Need a trigger to disable. Trigger would have to link to overall game state. Maybe I want a reference to the Blockade game?
    // Maybe move bounce code to inside Ball, so it can also be adjusted/modified
    this.applyLavaProperties(instigator);
    instigator.registerEvent(powerDownBall(instigator));
    return true;
  }

  private void applyLavaProperties(Ball instigator) {
    // Increase ball damage when killed.
    instigator.setDamage(100);
    instigator.setFill(Color.GOLD);
    instigator.setSlice(true);
  }

  private Event powerDownBall(Ball instigator) {
    return Event
      .during(Event.GamePhase.HitBorder)
      .when((game) -> instigator.isAtBorder())
      .then((game) -> {
        instigator.returnToNormal();
        System.out.println("Called LavaBrick Event to power down ball");
      });
  }
}
