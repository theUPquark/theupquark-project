package com.theupquark.games.blockade.paddles;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle extends Rectangle {

  public Paddle(double x, double y) {
    super(x, y, 75, 15);
    this.setFill(Color.BLUE);
  }


}
