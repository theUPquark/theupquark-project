package com.theupquark.games.blockade.balls;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class Ball extends Circle {

  private int velocityX;
  private int velocityY;

  public Ball(double centerX, double centerY) {
    super(centerX, centerY, 10, Color.WHITE);
    velocityX = 0;
    velocityY = 0;
  }

  public void setVelocityX(int velocityX) {
    this.velocityX = velocityX;
  }

  public int getVelocityX() {
    return this.velocityX;
  }

  public void setVelocityY(int velocityY) {
    this.velocityY = velocityY;
  }

  public int getVelocityY() {
    return this.velocityY;
  }
}
