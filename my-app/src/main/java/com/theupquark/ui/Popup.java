package com.theupquark.ui;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Class which will help create messages that could popup on the screen.
 *
 * A box with some text inside.
 */
public class Popup extends Pane {

  private String subTitle;
  private Text title;
  private int coordX;
  private int coordY;

  public Popup(String title, String subTitle, int x, int y) {
    this.subTitle = subTitle;

    Rectangle border = new Rectangle(300, 100, Color.WHITE);
    border.setFill(Color.WHITE);
    border.setStroke(Color.BLACK);
    border.setX(x);
    border.setY(y);

    title = new Title(x, y, title);
    this.getChildren().add(border);
  }

  public String getTitle() {
    return this.title;
  }

  public String getSubTitle() {
    return this.subTitle;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setSubTitle(String subTitle) {
    this.subTitle = subTitle;
  }
}
