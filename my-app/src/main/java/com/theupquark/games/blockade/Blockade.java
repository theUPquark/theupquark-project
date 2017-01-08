package com.theupquark.games.blockade;

import com.theupquark.games.blockade.balls.Ball;
import com.theupquark.games.blockade.bricks.Brick;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Starting off similiar to the Atari game Breakout.
 */
public class Blockade extends Pane {

  private boolean betweenGames = true;

  public Blockade() {
    this.setStyle("-fx-background-color: black");
    this.setPrefSize(400, 100);
    this.setGrid(7, 5);

    Rectangle paddle = new Rectangle(200, 500, 75, 15);
    paddle.setFill(Color.BLUE);
    this.getChildren().add(paddle);

    Ball ball = new Ball(200, paddle.getY() - 11);
    this.getChildren().add(ball);

    this.setOnMouseMoved(event -> {
      paddle.setX(event.getX());

      if (betweenGames) {
        ball.setCenterX(paddle.getX() + paddle.getWidth() / 2);
      }
    });

    this.setOnMousePressed(event -> {
      betweenGames = false;
    });

 }

  /**
 * Set the grid of bricks.
 *
 * @param screen where the bricks will be placed
 * @param gridLength how many bricks along the X axis
 * @param gridDepth how many brick layers
 */
   private void setGrid(int gridLength, int gridDepth) {
     int startX = 10;
     int startY = 30;
 
     for (int i = 0; i < gridLength; i++) {
       for (int j = 0; j < gridDepth; j++) {
         this.getChildren().add(new Brick(startX + i * Brick.getBrickWidth(),
           startY + j * Brick.getBrickHeight(),
           Color.GREEN));
       }
     }
 
   }

}
