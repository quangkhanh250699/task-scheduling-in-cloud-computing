package vn.edu.hust.simulation.realtime;

import vn.edu.hust.simulation.algos.AbstractSchedulingAlgorithm;

public interface IControllerFactory {

    AbstractSchedulingController createController(AbstractSchedulingAlgorithm algo);
}
