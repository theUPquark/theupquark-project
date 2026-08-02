package com.theupquark.games.blockade;

import com.theupquark.games.blockade.balls.Ball;
import com.theupquark.games.blockade.bricks.Brick;
import com.theupquark.games.blockade.bricks.BrickDebris;
import com.theupquark.games.blockade.bricks.LavaBrick;
import com.theupquark.games.blockade.bricks.RandomColorBrick;
import com.theupquark.games.blockade.event.Event;
import com.theupquark.games.blockade.event.HoldsEvents;
import com.theupquark.games.blockade.paddles.Paddle;
import com.theupquark.games.common.Killable;
import com.theupquark.ui.Popup;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
  private AtomicBoolean winCondition = new AtomicBoolean(false);
  
  private Timeline gameplay;
  private MediaPlayer soundCollision;

  private double boardWidth = 900;
  private Random random = new Random();

  record TrackedCollision(Node node, Bounds collision) {  }

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

    this.warmup();

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
   * Avoid slowdown during gameplay.
   */
  private void warmup() {
    new BrickDebris(0, 0, 1, 1, Color.TRANSPARENT, 0, 0, 0);

    ClassLoader classLoader = getClass().getClassLoader();
    soundCollision = new MediaPlayer(new Media(classLoader.getSystemResource("plop.wav").toExternalForm()));
    soundCollision.setOnEndOfMedia(() -> soundCollision.stop());
    this.restartMedia(soundCollision);
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
  private void startBall() {
    this.getChildren().remove(playerFeedback);
    activeBall.setCenterY(activeBall.getCenterY() + activeBall.getVelocityY());
    activeBall.setCenterX(activeBall.getCenterX() + activeBall.getVelocityX());

    //Actions on intersect with activeBall
    AtomicBoolean firstCollision = new AtomicBoolean(true);
    List<Node> newDebris = new ArrayList<>();

    List<Killable> forRemoval = this.getChildren().stream()
      .filter(Shape.class::isInstance)
      .filter(Predicate.not(Ball.class::isInstance))
      .map(node -> {
        Bounds intersect = Shape.intersect( (Shape) node, activeBall).getBoundsInLocal();
        return new TrackedCollision(node, intersect);
      })
      .map(trackedCollision -> {
        Node node = trackedCollision.node();
        Bounds intersect = trackedCollision.collision();
        if (intersect.getWidth() != -1) {
          if (node instanceof Brick brick && brick.collisionEnabled()) {
            System.out.println(node.getClass().getSimpleName() + ": " + intersect.getWidth() + ", " + intersect.getHeight());
            this.restartMedia(soundCollision);
            if (brick.removeBrick(intersect, activeBall)) {
              // TODO Method in Brick to get point value
              // TODO More points for multiple bricks hit. Resets when hit by paddle again.
              score++;
              newDebris.addAll(brick.spawnDebris());
            }
            // Adjust velocity for only the first collision.
            // TODO might need check for separate X/Y adjustments
            if (firstCollision.get() && !activeBall.isSlicing()) {
              firstCollision.set(false);
              if (intersect.getWidth() > intersect.getHeight()) {
                activeBall.setVelocityY(-activeBall.getVelocityY());
              } else if (intersect.getWidth() < intersect.getHeight()) {
                activeBall.setVelocityX(-activeBall.getVelocityX());
              } else {
                activeBall.setVelocityX(-activeBall.getVelocityX());
                activeBall.setVelocityY(-activeBall.getVelocityY());
              }
            }
            // TODO If we hit more than one brick in a loop, try flipping the velocity again, so it "slices" through them and keeps going.
          } else if (node instanceof Paddle paddle) {
            System.out.println(node.getClass().getSimpleName() + ": " + intersect.getWidth() + ", " + intersect.getHeight());
            this.restartMedia(soundCollision);
            paddle.reboundBall(this.activeBall);
            this.checkWinCondition();
          }
        }
        return node;
      })
      .filter(Killable.class::isInstance)
      .map(Killable.class::cast)
      .filter(Killable::isDead)
      .filter(Killable::proceedToDie)
      .collect(Collectors.toList());

    this.getChildren().removeAll(forRemoval);
    this.getChildren().addAll(newDebris);

    if (activeBall.getCenterY() < activeBall.getRadius()) {
      // Ball bounces off top
      this.restartMedia(soundCollision);
      activeBall.setVelocityY(-activeBall.getVelocityY());
      activeBall.trigger(Event.GamePhase.HitBorder);
      this.checkWinCondition();
    } else if (activeBall.getCenterY() > this.getHeight() - activeBall.getRadius()) {
      // Lose when ball hits bottom
      this.restartMedia(soundCollision);
      activeBall.setVelocityY(-activeBall.getVelocityY());
      activeBall.trigger(Event.GamePhase.HitBorder);
      this.failConditionResult();
    }

    // Ball bounces off sides
    if (activeBall.getCenterX() < activeBall.getRadius()) {
      this.restartMedia(soundCollision);
      activeBall.setVelocityX(-activeBall.getVelocityX());
      activeBall.trigger(Event.GamePhase.HitBorder);
    } else if (activeBall.getCenterX() > this.getWidth() - activeBall.getRadius()) {
      this.restartMedia(soundCollision);
      activeBall.setVelocityX(-activeBall.getVelocityX());
      activeBall.trigger(Event.GamePhase.HitBorder);
    }

    // Clean up spent events
    this.getChildren().stream()
      .filter(HoldsEvents.class::isInstance)
      .map(HoldsEvents.class::cast)
      .forEach(HoldsEvents::unregisterEvents);

    
    //update ui elements
    this.showScore.setText("Score: " + this.getScore());
    this.showLives.setText("Lives: " + this.getLives());

    if (this.winCondition.get()) {
      this.onWin();
    }
  }

  private void checkWinCondition() {
    if (this.getChildren().stream().noneMatch(n -> (n instanceof Brick brick) && !brick.isDead())) {
      this.winCondition.set(true);
    }
  }

  private void onWin() {
    // TODO Play a sound
    // TODO List of levels to progress through
    // TODO In the original game, the Ball was paused while the grid reset
    this.winCondition.set(false);
    setGrid(10, 7);
  }

  /**
   * Lose 'life' when ball hits the lower edge of the pane, 
   * and respawn ball with game 'paused'
   */
  private void failConditionResult() {
    betweenGames = true;
    gameplay.pause();
    lives--;
  
    //Tell the player they are bad
    this.playerFeedback.setSubTitle(this.getLives() + " remaining.");
    this.getChildren().add(this.playerFeedback);

    if (lives < 0) {
      //GAME OVER
      this.playerFeedback.setTitle("GAME OVER");

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
        this.getChildren().add(new RandomColorBrick(startX + i * Brick.getBrickWidth(),
          startY + j * Brick.getBrickHeight()));
      }
    }
    // TESTING LLAAVA
    this.getChildren().add(new LavaBrick(startX + Brick.getBrickWidth(),
          startY + gridDepth * Brick.getBrickHeight()));
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

  public void registerEvent(Event event) {
    switch (event.getPhase()) {
      // case Event.GamePhase.BrickCollision -> System.out.println("Register BrickCollision Event");
      default -> System.out.println("Unhandled event : " + event.toString());
    }
  }
}
