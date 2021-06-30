package vn.edu.hust.simulation.algos.myalgo.myheuristic;

import org.apache.commons.collections4.map.HashedMap;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletExecution;
import org.cloudbus.cloudsim.vms.Vm;
import vn.edu.hust.simulation.algos.AbstractSchedulingAlgorithm;
import vn.edu.hust.simulation.algos.CloudletVmMappingSolution;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LoadBalancingAlgo extends AbstractSchedulingAlgorithm {

    private List<Vm> vms;
    private final BalancingScheduler loadBalancer;
    private BayesianStatusPredictor statusPredictor;
    private CloudletVmMappingSolution solution;

    public LoadBalancingAlgo() {
        UnbalancingCalculator weightCalculator = new VarianceUnbalancedCalculator();
        this.loadBalancer = new BasicBalancingScheduler(weightCalculator);
    }

    @Override
    protected void processing() {

        double[] shortUsage = new double[this.vms.size()];
        double[] longUsage = new double[this.vms.size()];
        double[] usage = new double[this.vms.size()];
        double[] tasks = new double[this.comingCloudlets.size()];
        int[] types = new int[this.comingCloudlets.size()];

        this.statusPredictor.fit();

        for (int i = 0; i < this.vms.size(); ++i) {
            double[] vmUsage = this.getUsages(this.vms.get(i));
            longUsage[i] = vmUsage[0];
            shortUsage[i] = vmUsage[1];
            usage[i] = longUsage[i] + shortUsage[i];
        }

        for (int i = 0; i < this.comingCloudlets.size(); ++i) {
            tasks[i] = this.comingCloudlets.get(i).getUtilizationOfCpu();
            types[i] = (this.comingCloudlets.get(i).getLength() >= 10000) ? 0 : 1;
        }

        Map<Integer, Integer> taskToVm = this.loadBalancer.schedule(tasks, types, usage, shortUsage);

        Map<Cloudlet, Vm> result = new HashedMap<>();

        taskToVm.entrySet().forEach(
                integerIntegerEntry -> result.put(
                        this.comingCloudlets.get(integerIntegerEntry.getKey()),
                        this.vms.get(integerIntegerEntry.getValue())
                )
        );

        this.solution = new CloudletVmMappingSolution(result, 0.0);
    }

    private double[] getUsages(Vm vm) {
        List<Cloudlet> running = vm.getCloudletScheduler().getCloudletExecList().stream().map(
                CloudletExecution::getCloudlet
        ).collect(Collectors.toList());
        int numberCloudlets = running.size();
        double[] usage = {0.0, 0.0};
        for (Cloudlet cloudlet : running) {
            List<Double> prob = this.statusPredictor.estimateStatusProb(cloudlet);
            if (cloudlet.getLength() >= 10000)
                usage[0] += cloudlet.getUtilizationOfCpu() * prob.get(0);
            else
                usage[1] += cloudlet.getUtilizationOfCpu() * prob.get(0);
        }
        return usage;
    }

    @Override
    public void setVms(List<Vm> vms) {
        this.vms = vms;
        this.statusPredictor = new BayesianStatusPredictor(this.vms);
    }

    @Override
    public CloudletVmMappingSolution getSolution() {
        return this.solution;
    }
}
