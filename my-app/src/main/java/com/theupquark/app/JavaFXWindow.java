package com.theupquark.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class JavaFXWindow extends Application {

  public void start(Stage primaryStage) {
    
    Button btOK = new Button("OK");
    Scene scene = new Scene(btOK);
    primaryStage.setTitle("JavaFXWindow");
    primaryStage.setScene(scene);
    primaryStage.show();


  }

  /**
   * Not used
   */
  public static void main(String[] args) {
    Application.launch(args);
  } 
} 
