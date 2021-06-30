package vn.edu.hust.simulation.algos.myalgo.basic;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.util.ArrayList;
import java.util.List;

public class FakeStatusPredictor extends AbstractStatusPredictor {

    private static FakeStatusPredictor instance;

    private FakeStatusPredictor() {

    }

    public static FakeStatusPredictor getInstance() {
        if (instance == null) {
            instance = new FakeStatusPredictor();
        }

        return instance;
    }

    @Override
    public List<Double[]> estimateStatusProb(List<Cloudlet> cloudlets) {
        List<Double[]> result = new ArrayList<>();
        cloudlets.forEach(
                t -> result.add(new Double[] {
                        1.0, 0.0
                })
        );

        return result;
    }

    @Override
    public List<Double> estimateStatusProb(Cloudlet cloudlet) {
        List<Double> prob = new ArrayList<>();
        prob.add(0.);
        prob.add(1.);
        return prob;
    }

    @Override
    public void fit() {

    }
}
