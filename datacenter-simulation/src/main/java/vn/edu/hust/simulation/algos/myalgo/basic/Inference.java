package vn.edu.hust.simulation.algos.myalgo.basic;

import java.util.List;

public interface Inference<T> {

    List<Double> inference(T t);

    List<Integer> inference(List<T> ts);
}
