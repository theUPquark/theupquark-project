package com.theupquark.games.blockade.paddles;

import com.theupquark.games.blockade.balls.Ball;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle extends Rectangle {

  public Paddle(double x, double y) {
    super(x, y, 75, 15);
    this.setFill(Color.BLUE);
  }

  /**
   * Paddle adjusts velocity based on the position of the passed Ball
   *
   * @param ball Ball moving into Paddle
   */
  public void reboundBall(Ball ball) {
    double position = ball.getCenterX() - this.getX() - this.getWidth() / 2;
    double velocityChange = Math.floor(position / 5);
    ball.addVelocityX(velocityChange);
    ball.setVelocityY(-ball.getVelocityY());
    System.out.println("Relative ball position on paddle: " + position);
    System.out.println("Ball velocity change: " + velocityChange);
  }
}
