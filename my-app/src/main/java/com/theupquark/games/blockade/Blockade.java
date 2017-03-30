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
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
  private int lives;
  private Timeline gameplay;
  private MediaPlayer soundCollision;

  public Blockade() {
    this.setStyle("-fx-background-color: black");
    this.setPrefWidth(900);
    this.setGrid(10, 7);

    ClassLoader classLoader = getClass().getClassLoader();
    //getResourceAsStream(--)?? 
    //soundCollision = new AudioClip("http://cs.au.dk/~dsound/DigitalAudio.dir/Greenfoot/Pong.dir/sounds_ping_pong_8bit/ping_pong_8bit_plop.wav");
    soundCollision = new MediaPlayer(new Media("http://cs.au.dk/~dsound/DigitalAudio.dir/Greenfoot/Pong.dir/sounds_ping_pong_8bit/ping_pong_8bit_plop.wav"));
    soundCollision.setCycleCount(1);
    lives = 3;
    activePaddle = new Paddle(200, 500);
    this.getChildren().add(activePaddle);

    activeBall = new Ball(200, activePaddle.getY() - 11);
    activeBall.setVelocityY(5);
    activeBall.setVelocityX(5);
    this.getChildren().add(activeBall);

    gameplay = new Timeline(new KeyFrame(
        Duration.millis(30), e-> this.startBall()));
    gameplay.setCycleCount(Timeline.INDEFINITE);

    this.setOnMouseMoved(event -> {
      activePaddle.setX(event.getX() - activePaddle.getWidth() / 2);

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
    activeBall.setCenterY(activeBall.getCenterY() + activeBall.getVelocityY());

    //Actions on intersect with activeBall
    List<Node> nodesToRemove = new ArrayList<>();
    for (Node node : this.getChildren()) {
      if (node instanceof Shape && !(node instanceof Ball)) {
        Bounds intersect = Shape.intersect( (Shape) node, activeBall).getBoundsInLocal();
        if (intersect.getWidth() != -1) {
          soundCollision.play();
          System.out.println("# sounds; " + soundCollision.getCurrentCount());
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
            //Paddle adjusts velocity based on position of interaction
            double position = activeBall.getCenterX() - ((Paddle) node).getX() - ((Paddle) node).getWidth() / 2;
            System.out.println("Relative ball position on paddle: " + position);
            activeBall.setVelocityX(Math.floor(position / 5));
            activeBall.setVelocityY(-activeBall.getVelocityY());
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
      this.failConditionResult();
    }

    activeBall.setCenterX(activeBall.getCenterX() + activeBall.getVelocityX());

    if (activeBall.getCenterX() < activeBall.getRadius()) {
      activeBall.setVelocityX(-activeBall.getVelocityX());
    } else if (activeBall.getCenterX() > this.getWidth() - activeBall.getRadius()) {
      activeBall.setVelocityX(-activeBall.getVelocityX());
    }
  }

  /**
   * Lose 'life' when ball hits the lower edge of the pane, 
   * and respawn ball with game 'paused'
   */
  public void failConditionResult() {
    betweenGames = true;
    gameplay.pause();

    activeBall.setCenterX(200);
    activeBall.setCenterY(activePaddle.getY() - 11);
  }
  /**
   * Set the grid of bricks.
   *
   * @param screen where the bricks will be placed
   * @param gridLength how many bricks along the X axis
   * @param gridDepth how many brick layers
   */
  private void setGrid(int gridLength, int gridDepth) {
    //startX should change to center grid
    System.out.println("blockade width: " + this.getWidth());
    double startX = (this.getPrefWidth() - gridLength * Brick.getBrickWidth()) / 2;
    double startY = 30;
    for (int i = 0; i < gridLength; i++) {
      for (int j = 0; j < gridDepth; j++) {
        this.getChildren().add(new Brick(startX + i * Brick.getBrickWidth(),
          startY + j * Brick.getBrickHeight(),
          Color.GREEN));
      }
    }
  }

}
