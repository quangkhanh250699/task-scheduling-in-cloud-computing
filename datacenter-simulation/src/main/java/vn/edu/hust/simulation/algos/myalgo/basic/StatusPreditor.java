package vn.edu.hust.simulation.algos.myalgo.basic;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import vn.edu.hust.simulation.mlcenter.CloudletStatusClassifier;

import java.util.ArrayList;
import java.util.List;

public class StatusPreditor extends AbstractStatusPredictor {

    private static StatusPreditor instance;

    private static CloudletStatusClassifier statusClassifier;

    private StatusPreditor() {
        statusClassifier = new CloudletStatusClassifier();
    }

    public static StatusPreditor getInstance() {
        if (instance == null) {
            instance = new StatusPreditor();
        }

        return instance;
    }

    @Override
    public List<Double[]> estimateStatusProb(List<Cloudlet> cloudlets) {
        List<Double> probs = statusClassifier.predict(cloudlets);
        List<Double[]> result = new ArrayList<>();
        probs.forEach(
                t -> result.add(new Double[] {
                        t, 1.0 - t
//                        Math.max(t - 0.1, 0.), 1.0 - Math.max(t - 0.1, 0.)
//                        0.95, 0.05
                })
        );
//        for (int i = 0; i < cloudlets.size(); ++i) {
//            if (this.longTermQueue.contain(cloudlets.get(i))) {
//                result.get(i)[0] = 1.0;
//                result.get(i)[1] = 1.0 - result.get(i)[0];
//            }
//        }

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
