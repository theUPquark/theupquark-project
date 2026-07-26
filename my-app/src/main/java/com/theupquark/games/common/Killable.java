package com.theupquark.games.common;

public abstract class Killable extends DynamicCollision {
  private boolean dead = false;

  public void die() {
    this.dead = true;
    disableCollision();
  }

  public boolean isDead() {
    return this.dead;
  }

  public boolean proceedToDie() {
    return true;
  }
}
