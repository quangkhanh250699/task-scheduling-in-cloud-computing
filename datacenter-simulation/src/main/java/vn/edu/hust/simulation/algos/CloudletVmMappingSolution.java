package vn.edu.hust.simulation.algos;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.Map;

public class CloudletVmMappingSolution implements Solution<Map<Cloudlet, Vm>> {

    private double cost;
    private Map<Cloudlet, Vm> schedulingDecision;

    public CloudletVmMappingSolution(Map<Cloudlet, Vm> schedulingDecision, double cost) {
        this.schedulingDecision = schedulingDecision;
        this.cost = cost;
    }

    @Override
    public double getCost() {
        return this.cost;
    }

    @Override
    public Map<Cloudlet, Vm> getResult() {
        return this.schedulingDecision;
    }

    @Override
    public int compareTo(Solution<Map<Cloudlet, Vm>> mapSolution) {
        return this.getFitness() > mapSolution.getFitness() ? 1:0;
    }
}
