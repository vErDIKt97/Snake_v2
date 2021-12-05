package com.example.snake_v2.nnet;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.nnet.learning.LMS;

import java.util.List;

/**
 * Back Propagation learning rule for Multi Layer Perceptron neural networks.
 *
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class BackPropagation extends LMS {

    /**
     * The class fingerprint that is set to indicate serialization
     * compatibility with a previous version of the class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates new instance of BackPropagation learning
     */
    public BackPropagation() {
        super();
    }


    /**
     * This method implements weight update procedure for the whole network
     * for the specified  output error vector.
     *
     * @param outputError output error vector
     */
    @Override
    public void calculateWeightChanges(double[] outputError) {
        calculateErrorAndUpdateOutputNeurons(outputError);
        calculateErrorAndUpdateHiddenNeurons();
    }


    /**
     * This method implements weights update procedure for the output neurons
     * Calculates delta/error and calls updateNeuronWeights to update neuron's weights
     * for each output neuron
     *
     * @param outputError error vector for output neurons
     */
    protected void calculateErrorAndUpdateOutputNeurons(double[] outputError) {
        int i = 0;

        // for all output neurons
        final List<Neuron> outputNeurons = neuralNetwork.getOutputNeurons();
        for (Neuron neuron : outputNeurons) {
            // if error is zero, just set zero error and continue to next neuron
            if (outputError[i] == 0) {
                neuron.setDelta(0);
                i++;
                continue;
            }

            // otherwise calculate and set error/delta for the current neuron
            final TransferFunction transferFunction = neuron.getTransferFunction();
            final double neuronInput = neuron.getNetInput();
            final double delta = outputError[i] * transferFunction.getDerivative(neuronInput); // delta = (y-d)*df(net)
            neuron.setDelta(delta);

            // and update weights of the current neuron
            calculateWeightChanges(neuron);
            i++;
        } // for
    }

    /**
     * This method implements weights adjustment for the hidden layers
     */
    protected void calculateErrorAndUpdateHiddenNeurons() {
        List<Layer> layers = neuralNetwork.getLayers();
        for (int layerIdx = layers.size() - 2; layerIdx > 0; layerIdx--) {
            for (Neuron neuron : layers.get(layerIdx).getNeurons()) {
                // calculate the neuron's error (delta)
                final double delta = calculateHiddenNeuronError(neuron);
                neuron.setDelta(delta);
                calculateWeightChanges(neuron);
            } // for
        } // for
    }

    /**
     * Calculates and returns the neuron's error (neuron's delta) for the given neuron param
     *
     * @param neuron neuron to calculate error for
     * @return neuron error (delta) for the specified neuron
     */
    protected double calculateHiddenNeuronError(Neuron neuron) {
        double deltaSum = 0d;
        for (Connection connection : neuron.getOutConnections()) {
            double delta = connection.getToNeuron().getDelta() * connection.getWeight().value;
            deltaSum += delta; // weighted delta sum from the next layer
        } // for

        TransferFunction transferFunction = neuron.getTransferFunction();
        double netInput = neuron.getNetInput();
        double f1 = transferFunction.getDerivative(netInput);   // does this use netInput or cached output in order to avoid double caluclation?
        double delta = f1 * deltaSum;
        return delta;
    }

    public void applyWeightChanges1(){
        List<Layer> layers = neuralNetwork.getLayers();
        for (int i = neuralNetwork.getLayersCount() - 1; i > 0; i--) {
            // iterate neurons at each layer
            for (Neuron neuron : layers.get(i)) {
                // iterate connections/weights for each neuron
                for (Connection connection : neuron.getInputConnections()) {
                    // for each connection weight apply accumulated weight change
                    Weight weight = connection.getWeight();
                    if (!isBatchMode()) {
                        weight.value += weight.weightChange;
                    } else {
                        weight.value += (weight.weightChange / getTrainingSet().size());
                    }

                    weight.weightChange = 0; // reset deltaWeight
                }
            }
        }
    }
}