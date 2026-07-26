package com.theupquark.games.blockade.bricks;

import com.theupquark.games.common.Killable;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Brick extends Killable {

  private static final double REC_WIDTH = 80;
  private static final double REC_HEIGHT = 25;

  public Brick(double x, double y, Paint fill) {
    this(x, y);
    this.setFill(fill);
  }

  public Brick(double x, double y) {
    this.setWidth(REC_WIDTH);
    this.setHeight(REC_HEIGHT);

    this.setX(x);
    this.setY(y);

    this.setFill(Color.GREEN);
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
    if (removeCondition()) {
      die();
      return true;
    } 
    return false;
  }

  public boolean removeCondition() {
    return true;
  }

  // Fade Brick away
  private static int DEATH_STEPS_END = 20;
  private int deathSteps = 0;
  @Override
  public boolean proceedToDie() {
    this.setY(this.getY() + 5);
    this.setOpacity(this.getOpacity() - 0.05);
    if (++this.deathSteps > DEATH_STEPS_END) {
      return true;
    } else {
      return false;
    }
  }
}
