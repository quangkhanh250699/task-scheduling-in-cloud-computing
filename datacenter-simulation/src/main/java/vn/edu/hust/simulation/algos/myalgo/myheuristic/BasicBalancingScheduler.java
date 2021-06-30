package vn.edu.hust.simulation.algos.myalgo.myheuristic;

import org.apache.commons.collections4.map.HashedMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BasicBalancingScheduler implements BalancingScheduler{

    private final int K = 3;

    private UnbalancingCalculator weightCalculator;

    public BasicBalancingScheduler(UnbalancingCalculator weightCalculator) {
        this.weightCalculator = weightCalculator;
    }

    @Override
    public Map<Integer, Integer> schedule(double[] tasks, int[] types, double[] works, double[] shortWork) {
        double[] tasksClone = tasks.clone();
        int[] typesClone = types.clone();
        double[] worksClone = works.clone();
        double[] shortWorkClone = shortWork.clone();

        return this.scheduleClone(tasksClone, typesClone, worksClone, shortWorkClone);
    }

    private Map<Integer, Integer> scheduleClone(double[] tasks, int[] types, double[] works, double[] shortWork) {

        int nTasks = tasks.length;
        int nMachines = works.length;
        Map<Integer, Integer> result = new HashedMap<>();
        for (int i = 0; i < nTasks; ++i) {
            if (types[i] == 0) {
                int j = this.longTaskToMachine(tasks[i], works);
                works[j] += tasks[i];
                result.put(i, j);
            } else {
                int j = this.shortTaskToMachine(tasks[i], works, shortWork);
                works[j] += tasks[i];
                shortWork[j] += tasks[i];
                result.put(i, j);
            }
        }

        return result;
    }

    private int shortTaskToMachine(double task, double[] works, double[] shortWorks) {
        int nMachines = works.length;
        double[] shortWeights = new double[nMachines];
        for (int i = 0; i < nMachines; ++i) {
            shortWorks[i] += task;
            shortWeights[i] = this.weightCalculator.calculateUnbalancedWeight(shortWorks);
            shortWorks[i] -= task;
        }
        int[] minKMachines = new int[this.K];
        boolean[] mark = new boolean[nMachines];

        for (int i = 0; i < nMachines; ++i)
            mark[i] = false;

        for (int i = 0; i < this.K; ++i) {
            double minW = 100000;
            int minInd = -1;
            for (int j = 0; j < nMachines; ++j) {
                if (shortWeights[j] < minW && !mark[j]) {
                    minW = shortWeights[j];
                    minInd = j;
                }
            }
            minKMachines[i] = minInd;
        }

        double minWeight = 10000;
        int minIndex = 0;

        for (int i = 0; i < minKMachines.length; ++i) {
            int j = minKMachines[i];
            works[j] += task;
            double weight = this.weightCalculator.calculateUnbalancedWeight(works);
            works[j] -= task;
            if (weight < minWeight) {
                minWeight = weight;
                minIndex = j;
            }
        }

        return minIndex;
    }

    private int longTaskToMachine(double task, double[] works) {
        int nMachines = works.length;
        double[] weights = new double[nMachines];
        double minWeight = 100000;
        int minInd = -1;
        for (int i = 0; i < nMachines; ++i) {
            works[i] += task;
            weights[i] = this.weightCalculator.calculateUnbalancedWeight(works);
            works[i] -= task;

            if (weights[i] < minWeight) {
                minInd = i;
                minWeight = weights[i];
            }
        }
        return minInd;
    }

    public static void main(String[] args) {
        UnbalancingCalculator calculator = new VarianceUnbalancedCalculator();
        BasicBalancingScheduler scheduler = new BasicBalancingScheduler(calculator);
        int[] types = {0, 1, 0, 1, 1, 0, 0, 1, 1, 0};
        double[] works = {20, 60, 50};
        double[] shortWork = {0, 0, 0};
        double[] tasks = {10, 20, 20, 20, 40, 50, 30, 20, 20, 40};

        Map<Integer, Integer> map = scheduler.schedule(tasks, types, works, shortWork);
        map.entrySet().forEach(
                integerIntegerEntry -> {
                    System.out.println(integerIntegerEntry.getKey().toString() + "-->" + integerIntegerEntry.getValue().toString());
                }
        );
    }
}
