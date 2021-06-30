package vn.edu.hust.simulation.algos;

import org.apache.commons.collections4.map.HashedMap;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.List;
import java.util.Map;

public class SimpleAlgo extends SimpleAlgoAbstract{

    private int currentIndex;

    public SimpleAlgo() {
        super();
        this.currentIndex = -1;
    }

    @Override
    public void processing() {

        this.updateRunningCloudlets();

        List<Vm> vms = this.getVms();
        int vmNumber = vms.size();
        Map<Cloudlet, Vm> solutionMap = new HashedMap<>();
        for (int i = 0; i < this.comingCloudlets.size(); ++i) {
            currentIndex = (currentIndex + 1) % vmNumber;
            solutionMap.put(this.comingCloudlets.get(i), vms.get(currentIndex));
        }
        CloudletVmMappingSolution solution = new CloudletVmMappingSolution(solutionMap, 1);
        this.setSolution(solution);
    }

    @Override
    public void setVms(List<Vm> vms) {
        this.vms = vms;
    }
}
