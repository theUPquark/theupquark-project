package com.theupquark.games.blockade.balls;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class Ball extends Circle {

  private double velocityX;
  private double velocityY;
  private double velocityCap;

  public Ball(double centerX, double centerY) {
    super(centerX, centerY, 10, Color.WHITE);
    velocityX = 0;
    velocityY = 0;

    velocityCap = 20;
  }

  public void setVelocityX(double velocityX) {
    if (velocityX > velocityCap) {
      this.velocityX = this.velocityCap;
    } else if (velocityX < -velocityCap) {
      this.velocityX = -this.velocityCap;
    } else {
      this.velocityX = velocityX;
    }
  }

  public double getVelocityX() {
    return this.velocityX;
  }

  public void setVelocityY(double velocityY) {
    if (velocityY > velocityCap) {
      this.velocityY = this.velocityCap;
    } else if (velocityY < -velocityCap) {
      this.velocityY = -this.velocityCap;
    } else {
      this.velocityY = velocityY;
    }
  }

  public double getVelocityY() {
    return this.velocityY;
  }

  public void setVelocityCap(double velocityCap) {
    this.velocityCap = velocityCap;
  }

  public double getVelocityCap() {
    return this.velocityCap;
  }

  public void addVelocityX(double adjustment) {
    this.setVelocityX(this.getVelocityX() + adjustment);
    System.out.println("Velocity X: " + this.velocityX);
  }
  
  public void addVelocityY(double adjustment) {
    this.setVelocityY(this.getVelocityY() + adjustment);
  }
}
