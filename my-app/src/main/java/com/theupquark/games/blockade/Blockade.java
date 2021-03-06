package com.theupquark.games.blockade;

import com.theupquark.games.blockade.balls.Ball;
import com.theupquark.games.blockade.bricks.Brick;
import com.theupquark.games.blockade.paddles.Paddle;
import com.theupquark.ui.Popup;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

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

  private Popup playerFeedback;
  private Text showLives;
  private Text showScore;
  private int lives;
  private int score;
  
  private Timeline gameplay;
  private MediaPlayer soundCollision;

  private double boardWidth = 900;
  private Random random = new Random();

  public Blockade() {
    this.setStyle("-fx-background-color: black");
    this.setPrefWidth(boardWidth);
    this.setGrid(10, 7);
    this.lives = 3;
    this.score = 0;

    this.playerFeedback = new Popup("You aren't so good at this.", 
                            "Try better", 
                            100, 300, 
                            boardWidth/2 - 150, 400);
    this.showLives = new Text(10, 550, "Lives: " + this.getLives());
    this.showLives.setFill(Color.WHITE);
    this.showScore = new Text(750, 550, "Score: " + this.getScore());
    this.showScore.setFill(Color.WHITE);
    this.getChildren().add(showLives);
    this.getChildren().add(showScore);

    ClassLoader classLoader = getClass().getClassLoader();
    soundCollision = new MediaPlayer(new Media(classLoader.getSystemResource("plop.wav").toExternalForm()));
    soundCollision.setOnEndOfMedia(() -> soundCollision.stop());
    activePaddle = new Paddle(200, 500);
    this.getChildren().add(activePaddle);

    this.activeBall = new Ball(this.boardWidth / 2, activePaddle.getY() - 11, this.random);
    this.activeBall.resetVelocity();
    this.getChildren().add(activeBall);

    gameplay = new Timeline(new KeyFrame(
        Duration.millis(30), e-> this.startBall()));
    gameplay.setCycleCount(Timeline.INDEFINITE);

    this.setOnMouseMoved(event -> {
      activePaddle.setX(event.getX() - activePaddle.getWidth() / 2);
    });

    this.setOnMousePressed(event -> {
      if (betweenGames) {
        betweenGames = false;
        gameplay.play();
      }
    });

 }

  /**
   * Play Media. If already playing, restart it.
   * Used when a sound could occur rapidly, and missing an activation
   * would go noticed.
   *
   * @param media Media to play/restart
   */
  private void restartMedia(MediaPlayer media) {
    if (media.getStatus().equals(MediaPlayer.Status.PLAYING)) {
      media.seek(Duration.ZERO);
    } else {
      media.play();
    }
  }

  /**
   * Start the activeBall bouncing.
   *
   * Moves the activeBall location based on current velocity.
   * 
   * If any Brick collides with the area of the activeBall, reflect the 
   * velocity.
   *
   * Any Bricks that return true for removeBrick will be removed from rendering.
   * 
   */
  public void startBall() {
    this.getChildren().remove(playerFeedback);
    activeBall.setCenterY(activeBall.getCenterY() + activeBall.getVelocityY());
    activeBall.setCenterX(activeBall.getCenterX() + activeBall.getVelocityX());

    //Actions on intersect with activeBall
    List<Node> nodesHit = new ArrayList<>();
    boolean firstCollision = true;

    for (Node node : this.getChildren()) {
      if (node instanceof Shape && !(node instanceof Ball)) {
        Bounds intersect = Shape.intersect( (Shape) node, activeBall).getBoundsInLocal();
        if (intersect.getWidth() != -1) {
          System.out.println(node.getClass() + ": " + intersect.getWidth() + ", " + intersect.getHeight());
          this.restartMedia(soundCollision);
          if (node instanceof Brick) {
            if (((Brick) node).removeBrick()) {
              //Results of specific brick types being broken can be defined here.
              nodesHit.add(node);
              score++;
            }
            //Adjust velocity for only the first collision.
            //TODO might need check for separate X/Y adjustments
            if (firstCollision) {
              firstCollision = false;
              if (intersect.getWidth() > intersect.getHeight()) {
                activeBall.setVelocityY(-activeBall.getVelocityY());
              } else if (intersect.getWidth() < intersect.getHeight()) {
                activeBall.setVelocityX(-activeBall.getVelocityX());
              } else {
                activeBall.setVelocityX(-activeBall.getVelocityX());
                activeBall.setVelocityY(-activeBall.getVelocityY());
              }
            }
          } else if (node instanceof Paddle) {
            Paddle paddle = Paddle.class.cast(node);
            paddle.reboundBall(this.activeBall);
          }
        }

      }
    }
    this.getChildren().removeAll(nodesHit);

    //reverse velocity on pane borders
    if (activeBall.getCenterY() < activeBall.getRadius()) {
      this.restartMedia(soundCollision);
      activeBall.setVelocityY(-activeBall.getVelocityY());
    } else if (activeBall.getCenterY() > this.getHeight() - activeBall.getRadius()) {
      this.restartMedia(soundCollision);
      activeBall.setVelocityY(-activeBall.getVelocityY());
      this.failConditionResult();
    }

    if (activeBall.getCenterX() < activeBall.getRadius()) {
      this.restartMedia(soundCollision);
      activeBall.setVelocityX(-activeBall.getVelocityX());
    } else if (activeBall.getCenterX() > this.getWidth() - activeBall.getRadius()) {
      this.restartMedia(soundCollision);
      activeBall.setVelocityX(-activeBall.getVelocityX());
    }
    
    //update ui elements
    this.showScore.setText("Score: " + this.getScore());
    this.showLives.setText("Lives: " + this.getLives());
  }

  /**
   * Lose 'life' when ball hits the lower edge of the pane, 
   * and respawn ball with game 'paused'
   */
  public void failConditionResult() {
    betweenGames = true;
    gameplay.pause();
    lives--;
  
    //Tell the player they are bad
    this.playerFeedback.setSubTitle(this.getLives() + " remaining.");
    this.getChildren().add(this.playerFeedback);

    if (lives < 0) {
      //GAME OVER
      this.playerFeedback.setTitle("Wow, you lost..");

      //TODO Reset bricks, lives, and score
      List<Node> nodesHit = new ArrayList<>();
      for (Node node : this.getChildren()) {
            if (node instanceof Brick) {
              nodesHit.add(node);
            }
      }
      this.getChildren().removeAll(nodesHit);
      this.score = 0;
      this.lives = 3;
      setGrid(10, 7);
    }

    this.activeBall.resetVelocity();
    activeBall.setCenterX(this.boardWidth / 2);
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
          startY + j * Brick.getBrickHeight()));
      }
    }
  }

  /**
   * Test method for getting some information
   */
  public String getTitle() {
    return "Blockade";
  }

  /**
   * Get number of bricks destoryed
   */
  public int getScore() {
    return this.score;
  }

  /**
   * Get lives
   */
  public int getLives() {
    return this.lives;
  }
}
