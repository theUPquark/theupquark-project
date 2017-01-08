package com.theupquark.games.blockade;

import com.theupquark.games.blockade.balls.Ball;
import com.theupquark.games.blockade.bricks.Brick;
import com.theupquark.games.blockade.paddles.Paddle;

import java.util.List;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Starting off similiar to the Atari game Breakout.
 */
public class Blockade extends Pane {

  private boolean betweenGames = true;

  private Ball activeBall;
  private Paddle activePaddle;
  private List<Brick> activeBricks;

  public Blockade() {
    this.setStyle("-fx-background-color: black");
    this.setPrefSize(400, 100);
    this.setGrid(7, 5);

    activePaddle = new Paddle(200, 500);
    this.getChildren().add(activePaddle);

    activeBall = new Ball(200, activePaddle.getY() - 11);
    activeBall.setVelocityY(5);
    activeBall.setVelocityX(5);
    this.getChildren().add(activeBall);

    Timeline gameplay = new Timeline(new KeyFrame(
          Duration.millis(100), e-> this.startBall()));
    gameplay.setCycleCount(Timeline.INDEFINITE);

    this.setOnMouseMoved(event -> {
      activePaddle.setX(event.getX());

      if (betweenGames) {
        activeBall.setCenterX(activePaddle.getX() + activePaddle.getWidth() / 2);
      }
    });

    this.setOnMousePressed(event -> {
      if (betweenGames) {
        betweenGames = false;
        gameplay.play();
      }
    });

 }

  /**
   * Start the activeBall bouncing.
   * 
   */
  public void startBall() {
    activeBall.setCenterY(activeBall.getCenterY() - activeBall.getVelocityY());

    //Actions on intersect with activeBall
    List<Node> nodesToRemove = new ArrayList<>();
    for (Node node : this.getChildren()) {
      if (node instanceof Shape && !(node instanceof Ball)) {
        Bounds intersect = Shape.intersect( (Shape) node, activeBall).getBoundsInLocal();
        if (intersect.getWidth() != -1) {
          System.out.println(node.getClass() + ": " + intersect.getWidth() + ", " + intersect.getHeight());
          if (node instanceof Brick) {
            nodesToRemove.add(node);
            if (intersect.getWidth() > intersect.getHeight()) {
              activeBall.setVelocityY(-activeBall.getVelocityY());
            } else if (intersect.getWidth() < intersect.getHeight()) {
              activeBall.setVelocityX(-activeBall.getVelocityX());
            } else {
              activeBall.setVelocityX(-activeBall.getVelocityX());
              activeBall.setVelocityY(-activeBall.getVelocityY());
            }
          } else if (node instanceof Paddle) {
            //TODO Paddle should adjust velocity based on position of interaction
            if (intersect.getWidth() > intersect.getHeight()) {
              activeBall.setVelocityY(-activeBall.getVelocityY());
            } else if (intersect.getWidth() < intersect.getHeight()) {
              activeBall.setVelocityX(-activeBall.getVelocityX());
            } else {
              activeBall.setVelocityX(-activeBall.getVelocityX());
              activeBall.setVelocityY(-activeBall.getVelocityY());
            }
          }
        }

      }
    }
    this.getChildren().removeAll(nodesToRemove);

    //reverse velocity on pane borders
    if (activeBall.getCenterY() < activeBall.getRadius()) {
      activeBall.setVelocityY(-activeBall.getVelocityY());
    } else if (activeBall.getCenterY() > this.getHeight() - activeBall.getRadius()) {
      activeBall.setVelocityY(-activeBall.getVelocityY());
    }

    activeBall.setCenterX(activeBall.getCenterX() - activeBall.getVelocityX());

    if (activeBall.getCenterX() < activeBall.getRadius()) {
      activeBall.setVelocityX(-activeBall.getVelocityX());
    } else if (activeBall.getCenterX() > this.getWidth() - activeBall.getRadius()) {
      activeBall.setVelocityX(-activeBall.getVelocityX());
    }
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
