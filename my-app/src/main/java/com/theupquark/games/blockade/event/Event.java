package com.theupquark.games.blockade.event;

import java.util.function.BooleanSupplier;

import com.theupquark.games.blockade.Blockade;

import javafx.scene.shape.Shape;

/**
 * Events are interactions that can be registered into Blockade.
 * 
 * The goal is to define events in objects (ex Bricks, Ball, Paddle).
 * The same objects, with a reference to Blockade, can register those events when their methods are called.
 * 
 * Blockade will determine GamePhase, and decide when to ask objects to activate their registered Events.
*/
public class Event {

  public enum GamePhase {
    BrickCollision,
    HitBorder,
    Last
  }

  public interface GameCondition {
    boolean test(Blockade game);
  }
  public interface GameAction {
    void apply(Blockade game);
  }
  
  private Blockade game;
  private GamePhase phase = GamePhase.Last;
  private GameCondition condition = (game) -> true;
  private GameAction gameAction;
  private boolean unregister = false;
  private int charges = 1;

  private Event(Blockade game) {
    this.game = game;
  }

  public static Event in(Blockade game) {
    return new Event(game);
  }

  public Event during(GamePhase phase) {
    this.phase = phase;
    return this;
  }

  public Event when(GameCondition condition) {
    this.condition = condition;
    return this;
  }

  public Event then(GameAction gameAction) {
    this.gameAction = gameAction;
    return this;
  }

  // If charges == -1, Events will not unregister and will never be removed.
  public Event times(int charges) {
    this.charges = charges;
    return this;
  }

  public GamePhase getPhase() {
    return this.phase;
  }

  public boolean takeAction() {
    // Check game phase -- might not do this. It could just be where Blockade decides to place this Event.
    if (this.condition.test(this.game)) {
      this.gameAction.apply(game);
      if (this.charges > 0) {
        this.charges--;
        if (this.charges <= 0) {
          this.unregister = true;
        }
      }      
      return true;
    }
    return false;
  }

  public boolean unregister() {
    // TODO Maybe we could reference Blockade here and do whatever "unregister" would do, but might be better to have a cleanup in the main game loop (like how we remove blocks)
    return this.unregister;
  }

  @Override
  public String toString() {
    return new StringBuilder("Event during ").append(getPhase()).toString();
  }

}
