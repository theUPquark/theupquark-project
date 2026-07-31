package com.theupquark.games.blockade.bricks;

import com.theupquark.games.common.Killable;
import javafx.scene.paint.Paint;

/**
 * Mimic an exploding Brick.
 * 
 * It's important that this is not derived from Brick.class, so they won't conflict with
 * the win condition of no Bricks being present.
 */
public class BrickDebris extends Killable {

  private double velocityX;
  private double velocityY;
  private double spin;
  private static final double GRAVITY = 0.4;
  private static final int MAX_STEPS = 25;
  private int steps = 0;

  public BrickDebris(double x, double y, double width, double height, Paint fill, double velocityX, double velocityY, double spin) {
    this.setX(x);
    this.setY(y);
    this.setWidth(width);
    this.setHeight(height);
    this.setFill(fill);
    this.velocityX = velocityX;
    this.velocityY = velocityY;
    this.spin = spin;
    
    // Debris does not collide with game entities. RIP.
    this.die();
  }

  @Override
  public boolean proceedToDie() {
    this.setX(this.getX() + velocityX);
    this.setY(this.getY() + velocityY);
    this.velocityY += GRAVITY;
    this.setRotate(this.getRotate() + spin);
    this.setOpacity(Math.max(0, this.getOpacity() - 0.04));
    return ++this.steps > MAX_STEPS;
  }
}
