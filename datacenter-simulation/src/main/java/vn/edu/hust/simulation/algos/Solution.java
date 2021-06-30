package vn.edu.hust.simulation.algos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Solution<T> extends Comparable<Solution<T>> {
    Logger LOGGER = LoggerFactory.getLogger(Solution.class.getSimpleName());

    default double getFitness() {return 1.0D / this.getCost();}

    double getCost();

    T getResult();
}
