package vn.edu.hust.simulation.algos.myalgo.myheuristic;

import java.util.Arrays;
import java.util.List;

public class VarianceUnbalancedCalculator implements UnbalancingCalculator{

    @Override
    public double calculateUnbalancedWeight(double[] loads) {
        double mean = Arrays.stream(loads).sum() / loads.length;
        double weight = 0;
        for (int i = 0; i < loads.length; ++i) {
            weight += Math.pow(loads[i] - mean, 2);
        }
        weight = weight / loads.length;
        return weight;
    }
}
