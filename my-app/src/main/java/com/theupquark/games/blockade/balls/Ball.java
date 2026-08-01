package com.theupquark.games.blockade.balls;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.theupquark.games.blockade.event.Event;
import com.theupquark.games.blockade.event.HoldsEvents;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class Ball extends Circle implements HoldsEvents {

  private double velocityX;
  private double velocityY;
  private double velocityCap;

  private Random random;

  private int damage = 1;

  // When slice == true, the ball won't rebound off bricks, but keep going
  // TODO Refactor so that more logic about ball movement is in this class
  private boolean slice = false;

  private List<Event> events = new ArrayList<>();

  public Ball(double centerX, double centerY, Random random) {
    super(centerX, centerY, 10, Color.WHITE);
    this.random = random;
    velocityX = 0;
    velocityY = 0;

    velocityCap = 20;
  }

  @Override
  public List<Event> getEvents() {
    return this.events;
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

  public void resetVelocity() {
    this.setVelocityY(-5);
    this.setVelocityX(Math.floor(this.random.nextDouble() * 10 - 5));
  }

  public int getDamage() {
    return this.damage;
  }

  public void setDamage(int damage) {
    this.damage = damage;
  }

  public void setSlice(boolean slice) {
    this.slice = slice;
  }

  public boolean isSlicing() {
    return this.slice;
  }

  public void returnToNormal() {
    this.damage = 1;
    this.setFill(Color.WHITE);
    this.slice = false;
  }

}
