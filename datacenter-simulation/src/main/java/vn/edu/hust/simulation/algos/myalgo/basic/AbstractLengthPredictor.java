package vn.edu.hust.simulation.algos.myalgo.basic;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.util.List;

public abstract class AbstractLengthPredictor implements Regressor<Cloudlet>{

    public abstract List<Double> predict(List<Cloudlet> cloudlets);
}
