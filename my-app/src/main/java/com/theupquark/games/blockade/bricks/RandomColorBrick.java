package com.theupquark.games.blockade.bricks;

import javafx.scene.paint.Color;

public class RandomColorBrick extends Brick {

  public RandomColorBrick(double x, double y) {
    super(x, y, new Color(Math.random(), Math.random(), Math.random(), 1));
  }

}
