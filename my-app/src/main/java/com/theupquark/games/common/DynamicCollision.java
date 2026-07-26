package com.theupquark.games.common;

import javafx.scene.shape.Rectangle;

/**
 * 
 * Interactable
 * 
 * Interface to get some common logic.
 */
public abstract class DynamicCollision extends Rectangle {

  private boolean collisionEnabled = true;
  
  public boolean collisionEnabled() {
    return collisionEnabled;
  }

  void disableCollision() {
    this.collisionEnabled = false;
  }
}
