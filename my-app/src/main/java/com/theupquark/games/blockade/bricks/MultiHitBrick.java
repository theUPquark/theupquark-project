package com.theupquark.games.blockade.bricks;

import com.theupquark.games.blockade.balls.Ball;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class MultiHitBrick extends Brick {

  private int armor;

  public MultiHitBrick(double x, double y) {
    super(x, y);
    // some visual like lattis pattern
    this.setFill(Color.DARKGREEN);
    armor = 2;
  }

  @Override
  public boolean removeCondition(Ball instigator) {
    this.armor -= instigator.getDamage();
    if (armor <= 0) {
      return true;
    } else {
      return false;
    }
  }
}
