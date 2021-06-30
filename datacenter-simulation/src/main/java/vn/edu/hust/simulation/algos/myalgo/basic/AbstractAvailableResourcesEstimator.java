package vn.edu.hust.simulation.algos.myalgo.basic;

import org.cloudbus.cloudsim.vms.Vm;

import java.util.List;
import java.util.Map;

public abstract class AbstractAvailableResourcesEstimator {

    /**
     *
     * @param longTerm queue of long-term cloudlets
     * @param shortTerm queue of short-term cloudlet queues
     * @param availableVms virtual machines
     * @return [cpuPercentage, ram, bandwidth, storage]
     */
    public abstract Map<Vm, List<Double>> estimateAvailableResources(
            LongTermQueue longTerm, ShortTermQueue shortTerm, List<Vm> availableVms);
}
