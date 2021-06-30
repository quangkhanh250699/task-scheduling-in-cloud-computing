package vn.edu.hust.simulation.algos.myalgo.myheuristic;

import java.util.List;
import java.util.Map;

public interface BalancingScheduler {

    Map<Integer, Integer> schedule(double[] tasks, int[] types, double[] works, double[] shortWork);
}
