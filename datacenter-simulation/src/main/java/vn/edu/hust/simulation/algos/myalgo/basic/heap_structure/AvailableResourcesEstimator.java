package vn.edu.hust.simulation.algos.myalgo.basic.heap_structure;

import org.apache.commons.collections4.map.HashedMap;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import vn.edu.hust.simulation.algos.myalgo.basic.AbstractAvailableResourcesEstimator;
import vn.edu.hust.simulation.algos.myalgo.basic.LongTermQueue;
import vn.edu.hust.simulation.algos.myalgo.basic.ShortTermQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AvailableResourcesEstimator extends AbstractAvailableResourcesEstimator {

    @Override
    public Map<Vm, List<Double>> estimateAvailableResources(LongTermQueue longTerm, ShortTermQueue shortTerm, List<Vm> availableVms) {
        Map<Vm, List<Double>> availableResources = new HashedMap<>();

        for (Vm vm: availableVms) {
            List<Cloudlet> cloudlets = vm.getCloudletScheduler().getCloudletList();
            cloudlets = cloudlets.stream().filter(
                    cloudlet -> (cloudlet.getStatus() != Cloudlet.Status.SUCCESS)
            ).collect(Collectors.toList());
            double cpuPercentage = cloudlets.stream().mapToDouble(
                    cloudlet -> cloudlet.getUtilizationOfCpu()
            ).sum();
            double ram = cloudlets.stream().mapToDouble(
                    cloudlet -> cloudlet.getUtilizationOfRam()
            ).sum();
            double bandwidth = cloudlets.stream().mapToDouble(
                    cloudlet -> cloudlet.getUtilizationOfBw()
            ).sum();
            List<Double> available = new ArrayList<>();
            available.add(1 - cpuPercentage);
            available.add(vm.getRam().getCapacity() - ram);
            available.add(vm.getBw().getCapacity() - bandwidth);
            availableResources.put(vm, available);
        }
        return availableResources;
    }
}
