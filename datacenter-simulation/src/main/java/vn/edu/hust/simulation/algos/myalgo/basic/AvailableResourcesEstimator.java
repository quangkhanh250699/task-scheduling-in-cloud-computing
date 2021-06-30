package vn.edu.hust.simulation.algos.myalgo.basic;

import org.apache.commons.collections4.map.HashedMap;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AvailableResourcesEstimator extends AbstractAvailableResourcesEstimator{

    private AbstractStatusPredictor statusPredictor;

    public AvailableResourcesEstimator(AbstractStatusPredictor statusPredictor) {
        this.statusPredictor = statusPredictor;
    }

    @Override
    public Map<Vm, List<Double>> estimateAvailableResources(LongTermQueue longTerm, ShortTermQueue shortTerm, List<Vm> availableVms) {
        statusPredictor.setLongTermQueue(longTerm);
        statusPredictor.setShortTermQueue(shortTerm);
        statusPredictor.setVms(availableVms);
        statusPredictor.fit();

        Map<Vm, List<Double>> availableResources = new HashedMap<>();
        for (Vm vm : availableVms) {
            availableResources.put(vm, this.estimateAvailabelResources(vm));
        }

        return availableResources;
    }

    private List<Double> estimateAvailabelResources(Vm vm) {
        double cpu = 1;
        double ram = vm.getRam().getCapacity();
        double bandwidth = vm.getBw().getCapacity();
        double storage = vm.getStorage().getCapacity();

        List<Cloudlet> running = new ArrayList<>();
        vm.getCloudletScheduler().getCloudletExecList().forEach(
                c -> running.add(c.getCloudlet())
        );
        List<Double[]> probs = this.statusPredictor.estimateStatusProb(running);

        for (int i = 0; i < running.size(); ++i) {
            Cloudlet cloudlet = running.get(i);
            double runningProb = probs.get(i)[0];
            cpu -= cloudlet.getUtilizationOfCpu() * runningProb;
            ram -= cloudlet.getUtilizationOfRam() * runningProb;
            bandwidth -= cloudlet.getUtilizationOfBw() * runningProb;
        }

        cpu = Math.max(0, cpu);
        ram = Math.max(0, ram);

        List<Double> available = new ArrayList<>();
        available.add(cpu);
        available.add(ram);
        available.add(bandwidth);
        available.add(storage);

        return available;
    }
}
