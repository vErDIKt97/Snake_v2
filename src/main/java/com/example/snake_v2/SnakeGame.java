package com.example.snake_v2;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.util.Arrays;

public class SnakeGame {
    private Canvas canvas;
    int size, score, maxScore, iteration, totalScore, delay = 10, up = 0, right = 1, down = 2, left = 3;
    private GraphicsContext gc;
    StackPane root = new StackPane();
    Stage primaryStage;
    boolean lost = false;

    Point head;
    FoodPoint foodPoint;


    public SnakeGame(Stage primaryStageIn, int sizeIn) {
        this.size = sizeIn;
        this.canvas = new Canvas(size, size);
        this.gc = canvas.getGraphicsContext2D();
        this.primaryStage = primaryStageIn;
        canvas.setFocusTraversable(true);
        root.getChildren().add(canvas);
        startGame();
        Scene scene = new Scene(root, size, size);
        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void draw() {
        gc.clearRect(0, 0, this.size, this.size);
        if (!lost) {
            gc.setFill(Paint.valueOf(this.foodPoint.color));
            gc.fillOval(this.foodPoint.cor_x, this.foodPoint.cor_y, this.foodPoint.dot_size, this.foodPoint.dot_size);
            gc.setFill(Paint.valueOf(head.color));
            gc.fillOval(this.head.cor_x, this.head.cor_y, this.head.dot_size, this.head.dot_size);
            gc.setFill(Paint.valueOf("black"));
            gc.fillText("score: " + score
                            + " maxScore: " + maxScore
                            + " iteration: " + iteration,
                    size / 2 - 50, size / 2 - 15);
            gc.fillText("TotalScore: " + totalScore, size / 2 - 50, size / 2 + 15);
        } else reset();
    }


    public void startGame() {
        foodPoint = new FoodPoint(size);
        reLocateFood();
        launchAgent();
        launchAgent();
        launchAgent();

    }

    public void launchAgent() {

        Thread game = new Thread(() -> {
            double reward;
            head = new Point(size);
            Agent agent = new Agent();
            while (true) {
                if (!lost) {
                    checkCollision();
                    checkFood();
                    agent.setInput(this.foodPoint.cor_x, this.foodPoint.cor_y);
                    agent.calculate(this.head.cor_x, this.head.cor_y, size);
                    reward = move(agent, agent.getDir());
                    agent.learn(this.head.cor_x, this.head.cor_y, reward, size);
                //    System.out.println(Arrays.toString(agent.getOutput()));
                    iteration++;
                }
                Platform.runLater(
                        () -> {
                draw();
                        });
                try {
                    Thread.sleep(delay);
                } catch (Exception ignored) {
                }
            }
        });
        game.start();
    }

    private double move(Agent agent, int dir) {
        if (dir == up) {
            this.head.cor_y -= this.head.dot_size;
            return 0.1;
        }
        if (dir == down) {
            this.head.cor_y += this.head.dot_size;
            return 0.1;
        }
        if (dir == right) {
            this.head.cor_x += this.head.dot_size;
            return 0.1;
        }
        if (dir == left) {
            this.head.cor_x -= this.head.dot_size;
            return 0.1;
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private void checkFood() {
        if (this.head.cor_x == this.foodPoint.cor_x && this.head.cor_y == this.foodPoint.cor_y) {
            score++;
            totalScore++;
            maxScore = totalScore;
            reLocateFood();
        }
    }

    private void checkCollision() {
        if (this.head.cor_x >= size) lost = true;
        if (this.head.cor_y >= size) lost = true;
        if (this.head.cor_x < 0) lost = true;
        if (this.head.cor_y < 0) lost = true;
    }

    private void reLocateFood() {
        this.foodPoint.resetLocale(this.size);
    }

    private void reset() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (maxScore < score)
            maxScore = score;
        score = 0;
        lost = false;
        head.resetLocale(this.size);
    }
}
