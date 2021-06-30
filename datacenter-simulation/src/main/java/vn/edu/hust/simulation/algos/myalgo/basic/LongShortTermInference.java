package vn.edu.hust.simulation.algos.myalgo.basic;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import vn.edu.hust.simulation.mlcenter.LongShortTermClassifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LongShortTermInference extends AbstractLongShortTermInference {

    private static LongShortTermInference instance;
    private static LongShortTermClassifier classifier;

    private Random rand;

    private LongShortTermInference() {
        this.rand = new Random();
        classifier = new LongShortTermClassifier();
        classifier.setup();
    }

    public static LongShortTermInference getInstance() {
        if (instance == null) {
            instance = new LongShortTermInference();
        }

        return instance;
    }

    @Override
    public List<Double> inference(Cloudlet cloudlet) {
        double prob = this.rand.nextDouble();
        List<Double> result = new ArrayList<>();
        result.add(prob);
        result.add(1 - prob);
        return result;
    }

    @Override
    public List<Integer> inference(List<Cloudlet> cloudlets) {
        List<Integer> result = classifier.predict(cloudlets);
        return result;
    }
}
