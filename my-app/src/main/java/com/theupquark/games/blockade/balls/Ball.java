package com.theupquark.games.blockade.balls;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.theupquark.games.blockade.Blockade;
import com.theupquark.games.blockade.event.Event;
import com.theupquark.games.blockade.event.HoldsEvents;
import com.theupquark.games.common.GameAware;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class Ball extends Circle implements HoldsEvents, GameAware {

  private double velocityX;
  private double velocityY;
  private double velocityCap;

  private Random random;

  private int damage = 1;

  // When slice == true, the ball won't rebound off bricks, but keep going
  // TODO Refactor so that more logic about ball movement is in this class
  private boolean slice = false;

  private List<Event> events = new ArrayList<>();
  private Blockade game;

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

  public Blockade getGame() {
    return this.game;
  }

  public void setGame(Blockade game) {
    this.game = game;
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

  public void registerBorderBounceEvents() {
    Event sideBounce = Event
      .during(Event.GamePhase.HitBorder)
      .when(g -> {
        double width = (g != null && g.getWidth() > 0) ? g.getWidth() : (g != null ? g.getPrefWidth() : 900);
        return this.getCenterX() < this.getRadius() || this.getCenterX() > width - this.getRadius();
      })
      .then(g -> {
        // TODO Ball sound should probably be created, defined, and called from a Ball method.
        if (g != null) {
          g.restartSoundCollision();
        }
        this.setVelocityX(-this.getVelocityX());
      })
      .times(-1);
    this.registerEvent(sideBounce);

    Event topBounce = Event
      .during(Event.GamePhase.HitBorder)
      .when(g -> {
        return this.getCenterY() < this.getRadius();
      })
      .then(g -> {
        // TODO Ball sound should probably be created, defined, and called from a Ball method.
        if (g != null) {
          g.restartSoundCollision();
          g.checkWinCondition();
        }
        this.setVelocityY(-this.getVelocityY());
      })
      .times(-1);
    this.registerEvent(topBounce);

    Event bottomDeadzone = Event
      .during(Event.GamePhase.HitBorder)
      .when(g -> {
        double height = (g != null && g.getHeight() > 0) ? g.getHeight() : (g != null ? g.getPrefHeight() : 600);
        return this.getCenterY() > height - this.getRadius();
      })
      .then(g -> {
        if (g != null) {
          g.restartSoundCollision();
          g.failConditionResult();
        }
      })
      .times(-1);
    this.registerEvent(bottomDeadzone);
  }

  public boolean isAtBorder() {
    if (getGame() == null) return false;
    return this.getCenterX() <= this.getRadius()
        || this.getCenterX() >= getGame().getWidth() - this.getRadius()
        || this.getCenterY() <= this.getRadius()
        || this.getCenterY() >= getGame().getHeight() - this.getRadius();
  }

}
