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

  private Text subTitle;
  private Text title;

  private double height;
  private double width;

  /**
   * @param title main title
   * @param subTitle displayed text under title
   * @param height of rectangle
   * @param width of rectangle
   * @param x position within pane
   * @param y position within pane
   */
  public Popup(String title, 
               String subTitle,
               double height,
               double width,
               double x, 
               double y) {

    Rectangle border = new Rectangle(width, height, Color.WHITE);
    border.setFill(Color.WHITE);
    border.setStroke(Color.BLACK);
    border.setX(x);
    border.setY(y);

    this.title = new Text(x + title.length() * 2.2, y + height/2, title);
    this.getChildren().add(border);
    this.getChildren().add(this.title);
  }

  public String getTitle() {
    return this.title.getText();
  }

  public String getSubTitle() {
    return this.subTitle.getText();
  }

  public void setTitle(String title) {
    this.title.setText(title);
  }

  public void setSubTitle(String subTitle) {
    this.subTitle.setText(subTitle);
  }
}
