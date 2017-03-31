package com.theupquark.app;

import com.theupquark.games.blockade.Blockade;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Starts JavaFX window formatted with a BorderPane 
 */
public class JavaFXWindow extends Application {

  private boolean betweenGames = true;

  public void start(Stage primaryStage) {
    
    Scene scene = new Scene(this.getPane(), 900, 600);
    primaryStage.setTitle("JavaFX Window");
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();


  }

  /**
   * Main
   */
  public static void main(String[] args) {
    Application.launch(args);
  } 

  /**
   * Sets BorderPane
   *
   * @returns borderPane
   */
  private BorderPane getPane() {
    Blockade blockade = new Blockade();

    BorderPane bPane = new BorderPane();

    //TOP
    Text title = new Text(100, 50, blockade.getTitle());
    GridPane topPane = new GridPane();
    topPane.add(title, 0, 0);
    topPane.setAlignment(Pos.CENTER);
    bPane.setTop(topPane);

    //CENTER
    bPane.setCenter(blockade);

    return bPane;
  }

} 
