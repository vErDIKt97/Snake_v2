package com.example.snake_v2;

import com.example.snake_v2.nnet.BackPropagation;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.TransferFunctionType;

import java.util.Arrays;

public class Agent {
    public NeuralNetwork neuralNetwork = NeuralNetworkFactory.createMLPerceptron("2 125 125 125 125 125 4", TransferFunctionType.STEP);
    BackPropagation rule = new BackPropagation();
    private double[] input = new double[2];
    private double[] output = new double[4];
    private double[] predicted = new double[4];
    private int dir;
    private double reward;
    private final static double[] upList = new double[]{1, 0, 0, 0};
    private final static double[] rightList = new double[]{0, 1, 0, 0};
    private final static double[] downList = new double[]{0, 0, 1, 0};
    private final static double[] leftList = new double[]{0, 0, 0, 1};

    public Agent() {
        rule.setLearningRate(0.2);
        neuralNetwork.setLearningRule(rule);
    }

    public void learn(double x_head, double y_head, double reward, double size) {
        this.setPredicted(x_head, y_head, reward, size);
        double[] patternError = this.rule.getErrorFunction().addPatternError(this.output, this.predicted);
        this.rule.calculateWeightChanges(patternError);
        if (!this.rule.isBatchMode()) {
            this.rule.applyWeightChanges1();
        }
    }

    public double[] getInput() {
        return input;
    }

    public void setInput(double x_f, double y_f) {
        this.input[0] = x_f;
        this.input[1] = y_f;
    }

    public double[] getOutput() {
        return this.neuralNetwork.getOutput();
    }

    public void setOutput(double[] output) {
        this.output = output;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public void calculate(double x_head, double y_head, double size) {
        this.neuralNetwork.calculate();
        this.output = neuralNetwork.getOutput();

    }

    private void setPredicted(double x_head, double y_head, double reward, double size) {
        for (int i = 0; i < this.predicted.length; i++) {
            this.predicted[i] = getPredDir(x_head, y_head, size)[i];
        }
        // this.predicted[4]=this.output[4]+(1-reward);
    }

    public double[] getPredDir(double x_head, double y_head, double size) {
        boolean toggle1, toggle2, toggle3, toggle4;
        double[] result;
        toggle1 = toggle2 = toggle3 = toggle4 = true;
        if (x_head < this.input[0]) {
            if ((size - y_head) < (size - this.input[1])) {
                if ((this.input[0] - x_head) != ((size - this.input[1]) - (size - y_head))) {
                    if (toggle1) {
                        result = rightList;
                    } else {
                        result = upList;
                    }
                    toggle1 = !toggle1;
                } else result = upList;
            } else if ((size - y_head) > (size - this.input[1])) {
                if ((this.input[0] - x_head) != ((size - y_head) - (size - this.input[1]))) {
                    if (toggle2)
                        result = downList;
                    else
                        result = rightList;
                    toggle2 = !toggle2;
                } else result = rightList;
            } else result = rightList;
        } else {
            if (x_head > this.input[0]) {
                if ((size - y_head) < (size - this.input[1])) {
                    if ((x_head - this.input[0]) != ((size - this.input[1]) - (size - y_head))) {
                        if (toggle3)
                            result = upList;
                        else
                            result = leftList;
                        toggle3 = !toggle3;
                    } else result = upList;
                } else if ((size - y_head) > (size - this.input[1])) {
                    if ((x_head - this.input[0]) != ((size - y_head) - (size - this.input[1]))) {
                        if (toggle4)
                            result = downList;
                        else
                            result = leftList;
                        toggle4 = !toggle4;
                    } else result = upList;
                } else result = leftList;
            } else if ((size - y_head) < (size - this.input[1])) result = upList;
            else result = downList;
        }
        return result;
    }


    public int getDir() {
        if (Arrays.equals(this.output,upList))return 0;
        if (Arrays.equals(this.output,rightList) )return 1;
        if (Arrays.equals(this.output,downList) )return 2;
        if (Arrays.equals(this.output, leftList))return 3;
        return 5;

    }
}
