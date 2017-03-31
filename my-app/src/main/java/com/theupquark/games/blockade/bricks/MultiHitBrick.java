package com.theupquark.games.blockade.bricks;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class MultiHitBrick extends Brick {

  private int armor;

  public MultiHitBrick(double x, double y, Paint fill) {
    super(x, y, fill);
    armor = 2;
  }

  @Override
  public boolean removeBrick() {
    if (--armor <= 0) {
      return true;
    } else {
      return false;
    }
  }
}
