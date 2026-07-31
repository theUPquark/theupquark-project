package com.theupquark.games.blockade.bricks;

import com.theupquark.games.common.Killable;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class Brick extends Killable {

  private static final double REC_WIDTH = 80;
  private static final double REC_HEIGHT = 25;

  // Track most recent collision (most likely with Ball)
  protected Bounds collision;

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
  public boolean removeBrick(Bounds collision) {
    this.collision = collision;
    if (removeCondition()) {
      die();
      return true;
    } 
    return false;
  }

  public boolean removeCondition() {
    return true;
  }

  @Override
  public boolean proceedToDie() {
    // Remove original brick shape immediately on death so debris shards take over
    return true;
  }

  // Spawns pieces that explode outward from the brick's position.
  public List<BrickDebris> spawnDebris() {
    List<BrickDebris> shards = new ArrayList<>();
    int cols = 4;
    int rows = 2;

    // Each piece size
    double pieceW = this.getWidth() / cols;
    double pieceH = this.getHeight() / rows;

    // Center of each piece
    // Recall that (x,y) coordinates are the upper-left corner
    double cx = this.getX() + this.getWidth() / 2.0;
    double cy = this.getY() + this.getHeight() / 2.0;

    for (int col = 0; col < cols; col++) {
      for (int row = 0; row < rows; row++) {
        double px = this.getX() + col * pieceW;
        double py = this.getY() + row * pieceH;
        double pcx = px + pieceW / 2.0;
        double pcy = py + pieceH / 2.0;

        // Radiate debris out from the center of the original Brick
        // When debris is left side of original Brick, move left. Ride side, move right.
        double dirX = pcx - cx;
        double dirY = pcy - cy;
        double speed = 2.0 + Math.random() * 4.0;
        double vx = (dirX == 0 ? (Math.random() - 0.5) : Math.signum(dirX)) * speed + (Math.random() - 0.5) * 2.0;
        double vy = (dirY == 0 ? -1.0 : Math.signum(dirY)) * speed + (Math.random() - 0.5) * 2.0 - 2.0;
        double spin = (Math.random() - 0.5) * 30.0;

        shards.add(new BrickDebris(px, py, pieceW, pieceH, this.getFill(), vx, vy, spin));
      }
    }
    return shards;
  }
}
