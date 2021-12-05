package com.example.snake_v2;

public class Point {
    int cor_x;
    int cor_y;
    protected final int dot_size = 10;
    String color;

    public Point(int size) {
        reSetCor_x(size);
        reSetCor_y(size);
        color = "red";
    }

    public int getCor_y() {
        return cor_y;
    }

    public void reSetCor_y(int size) {
        this.cor_y = (int) (Math.random() * ((size / dot_size) - 1)) * dot_size;
    }

    public int getCor_x() {
        return cor_x;
    }

    public void reSetCor_x(int size) {
        this.cor_x = (int) (Math.random() * ((size / dot_size) - 1)) * dot_size;
    }

    public void resetLocale (int size){
        reSetCor_x(size);
        reSetCor_y(size);
    }

}
