package vn.edu.hust.simulation.algos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface SchedulingAlgorithm<S extends Solution> {

    Logger LOGGER = LoggerFactory.getLogger(SchedulingAlgorithm.class.getSimpleName());

    S getSolution();

    void solve();

    double getSolveTime();
}
