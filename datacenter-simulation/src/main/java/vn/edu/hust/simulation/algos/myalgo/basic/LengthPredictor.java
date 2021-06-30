package vn.edu.hust.simulation.algos.myalgo.basic;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import vn.edu.hust.simulation.mlcenter.CloudletLengthRegressor;

import java.util.List;

public class LengthPredictor extends AbstractLengthPredictor {

    private static LengthPredictor instance;
    private static CloudletLengthRegressor regressor;

    private LengthPredictor() {
        regressor = new CloudletLengthRegressor();
    }

    public static LengthPredictor getInstance() {
        if (instance == null) {
            instance = new LengthPredictor();
        }

        return instance;
    }

    @Override
    public double predict(Cloudlet cloudlet) {
        return 1000;
    }

    @Override
    public List<Double> predict(List<Cloudlet> cloudlets) {
        return regressor.predict(cloudlets);
    }
}
