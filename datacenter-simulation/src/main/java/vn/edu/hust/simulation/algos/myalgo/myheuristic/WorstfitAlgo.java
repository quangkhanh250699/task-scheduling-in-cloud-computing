package vn.edu.hust.simulation.algos.myalgo.myheuristic;

import org.apache.commons.collections4.map.HashedMap;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import vn.edu.hust.simulation.algos.AbstractSchedulingAlgorithm;
import vn.edu.hust.simulation.algos.CloudletVmMappingSolution;

import java.util.List;
import java.util.Map;

public class WorstfitAlgo extends AbstractSchedulingAlgorithm {

    private List<Vm> vms;
    private CloudletVmMappingSolution solution;

    private UnbalancingCalculator weightCalculator;

    public WorstfitAlgo() {
        this.weightCalculator = new VarianceUnbalancedCalculator();
    }


    @Override
    protected void processing() {
        double[] tasks = this.comingCloudlets.stream().mapToDouble(
                Cloudlet::getUtilizationOfCpu
        ).toArray();
        double[] machines = this.vms.stream().mapToDouble(
                vm -> vm.getCloudletScheduler().getCloudletExecList()
                        .stream().mapToDouble(cloudletExecution -> cloudletExecution.getCloudlet().getUtilizationOfCpu()).sum()
        ).toArray();

        Map<Integer, Integer> result = this.tasksToVms(tasks, machines);

        Map<Cloudlet, Vm> mapping = new HashedMap<>();

        result.forEach(
                (integer, integer2) -> mapping.put(
                        this.comingCloudlets.get(integer), this.vms.get(integer2)
                )
        );

        this.solution = new CloudletVmMappingSolution(mapping, 0);
    }

    public Map<Integer, Integer> tasksToVms(double[] tasks, double[] vms) {
        Map<Integer, Integer> result = new HashedMap<>();

        for (int i = 0; i < tasks.length; ++i) {
            int j = this.singleTaskToVm(tasks[i], vms);
            vms[j] += tasks[i];
            result.put(i, j);
        }
        return result;
    }

    private int singleTaskToVm(double task, double[] vms) {
        int minInd = -1;
        double minWeight = 100000;
        for (int i = 0; i < vms.length; ++i) {
            vms[i] += task;
            double weight = this.weightCalculator.calculateUnbalancedWeight(vms);
            vms[i] -= task;
            if (weight < minWeight) {
                minInd = i;
                minWeight = weight;
            }
        }
        return minInd;
    }

    @Override
    public void setVms(List<Vm> vms) {
        this.vms = vms;
    }

    @Override
    public CloudletVmMappingSolution getSolution() {
        return this.solution;
    }

    public static void main(String[] args) {
        double[] works = {20, 60, 50};
        double[] tasks = {10, 20, 20, 20, 40, 50, 30, 20, 20, 40};

        WorstfitAlgo scheduler = new WorstfitAlgo();

        Map<Integer, Integer> map = scheduler.tasksToVms(tasks, works);
        map.entrySet().forEach(
                integerIntegerEntry -> {
                    System.out.println(integerIntegerEntry.getKey().toString() + "-->" + integerIntegerEntry.getValue().toString());
                }
        );
    }
}
