package com.theupquark.games.blockade.bricks;

import javafx.scene.paint.Color;

public class RandomColorBrick extends Brick {

  /**
   * Same a Brick, but color is random. Might not be used
   * since color will be the easiest way to implement Bricks with special
   * properties.
   */
  public RandomColorBrick(double x, double y) {
    super(x, y, new Color(Math.random(), Math.random(), Math.random(), 1));
  }

}
