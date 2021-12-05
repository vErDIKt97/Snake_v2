package com.example.snake_v2;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Snake extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        SnakeGame snakeGame = new SnakeGame(stage,500);

    }

    public static void main(String[] args) {
        launch();
    }
}