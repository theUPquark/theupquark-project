package com.theupquark.app;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class JavaFXWindow extends Application {

  public void start(Stage primaryStage) {
    
    Button btOK = new Button("OK");
    Scene scene = new Scene(this.getPane(), 600, 200);
    primaryStage.setTitle("JavaFXWindow");
    primaryStage.setScene(scene);
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
    BorderPane bPane = new BorderPane();

    //TOP
    Text title = new Text(100, 50, "Window Text");
    GridPane topPane = new GridPane();
    topPane.add(title, 0, 0);
    topPane.setAlignment(Pos.CENTER);
    bPane.setTop(topPane);

    //CENTER
    Pane screen = new Pane();
    screen.setStyle("-fx-background-color: black");
    screen.setPrefSize(400, 100);
    bPane.setCenter(screen);
    return bPane;
  }
} 
