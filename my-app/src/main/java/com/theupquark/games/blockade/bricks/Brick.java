package com.theupquark.games.blockade.bricks;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Brick extends Rectangle {

  private static final double REC_WIDTH = 80;
  private static final double REC_HEIGHT = 25;

  public Brick(double x, double y, Paint fill) {
    this.setWidth(REC_WIDTH);
    this.setHeight(REC_HEIGHT);

    this.setX(x);
    this.setY(y);

    this.setFill(fill);
    this.setStroke(Color.BLACK);
  }

  public static double getBrickWidth() {
    return REC_WIDTH;
  }

  public static double getBrickHeight() {
    return REC_HEIGHT;
  }

  /**
   * Return true when the brick should be removed from play.
   *
   * @return true always by default
   */
  public boolean removeBrick() {
    return true;
  }
}
