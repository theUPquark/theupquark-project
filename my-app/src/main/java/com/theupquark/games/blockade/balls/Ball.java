package com.theupquark.games.blockade.balls;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class Ball extends Circle {

  private double velocityX;
  private double velocityY;

  public Ball(double centerX, double centerY) {
    super(centerX, centerY, 10, Color.WHITE);
    velocityX = 0;
    velocityY = 0;
  }

  public void setVelocityX(double velocityX) {
    this.velocityX = velocityX;
  }

  public double getVelocityX() {
    return this.velocityX;
  }

  public void setVelocityY(double velocityY) {
    this.velocityY = velocityY;
  }

  public double getVelocityY() {
    return this.velocityY;
  }
}
