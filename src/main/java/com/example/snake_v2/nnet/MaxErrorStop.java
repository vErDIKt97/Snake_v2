package com.example.snake_v2.nnet;

import java.io.Serializable;
import org.neuroph.core.learning.stop.StopCondition;

/**
 * Stops learning rule if total network error is below some specified value
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class MaxErrorStop implements StopCondition, Serializable {

    private final SupervisedLearning learningRule;

    public MaxErrorStop(SupervisedLearning learningRule) {
        this.learningRule = learningRule;
    }

    @Override
    public boolean isReached() {
        if (learningRule.getTotalNetworkError() < learningRule.getMaxError()) {
            return true;
        }

        return false;
    }

}