package vn.edu.hust.simulation.algos;

import org.apache.commons.collections4.map.HashedMap;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class SimpleAlgoAbstract extends AbstractSchedulingAlgorithm {

    protected Map<Cloudlet, Vm> runningCloudlets;
    protected List<Vm> vms;
    protected Map<Vm, List<Cloudlet>> vmToCloudlet;
    protected CloudletVmMappingSolution solution;

    public SimpleAlgoAbstract() {
        this.comingCloudlets = new ArrayList<>();
        this.runningCloudlets = new HashedMap<>();
        this.vms = new ArrayList<>();
        this.vmToCloudlet = new HashedMap<>();
    }

    public void updateRunningCloudlets() {
        this.runningCloudlets = this.runningCloudlets.entrySet().stream()
            .filter(cloudlet -> cloudlet.getKey().getStatus() == Cloudlet.Status.INEXEC)
            .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
    }

    private void updateRunningCloudlets(Map<Cloudlet, Vm> map) {
        this.runningCloudlets.putAll(map);
    }

    private void updateVmMapping() {
        this.updateRunningCloudlets(this.solution.getResult());
        this.vmToCloudlet = this.runningCloudlets.entrySet().stream()
            .collect(Collectors.groupingBy(
                Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
    }

    public List<Vm> getVms() {
        return this.vms;
    }

    protected void setSolution(CloudletVmMappingSolution solution) {
        this.solution = solution;
    }

    @Override
    public CloudletVmMappingSolution getSolution() {
        return this.solution;
    }
}
