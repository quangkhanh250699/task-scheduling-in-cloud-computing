package vn.edu.hust.simulation.algos.myalgo.myheuristic;

public class TaskBayesianNetwork {
    
    private final Double[] jobProbs;
    private final Double[][] taskProbs;

    public TaskBayesianNetwork() {
        this.jobProbs = new Double[] {0.85, 0.15};
        Double[] runningProbs = new Double[] {0.93, 0.07};
        Double[] finishedProbs = new Double[] {0.0, 1.0};
        this.taskProbs = new Double[2][2];
        taskProbs[0] = runningProbs;
        taskProbs[1] = finishedProbs;
    }

    public Double[] inference(int n, int k) {
        if (k >= n) {
            return new Double[] {0.0, 1.0};
        }
        if (k == 0) {
            return new Double[] {1.0, 0.0};
        }
        if (k == 1) {
            return new Double[] {0.2641, 0.7359};
        }
        if (k == 2) {
            return new Double[] {0.0251, 0.9749};
        }
        if (k == 3) {
            return new Double[]{0.0018, 0.9982};
        }
        return new Double[] {1.0, 0.0};
    }

    public static void main(String[] args) {
        TaskBayesianNetwork network = new TaskBayesianNetwork();
        System.out.println();
    }
}
