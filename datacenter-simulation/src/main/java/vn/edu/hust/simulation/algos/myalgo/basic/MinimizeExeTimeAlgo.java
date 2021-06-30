package vn.edu.hust.simulation.algos.myalgo.basic;

import org.apache.commons.collections4.map.HashedMap;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import vn.edu.hust.simulation.algos.CloudletVmMappingSolution;
import vn.edu.hust.simulation.algos.myalgo.basic.heap_structure.VmResourcesAvailable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MinimizeExeTimeAlgo extends AbstractSubSchedulingAlgorithm {

    private List<Vm> vms;
    private Map<Cloudlet, Double> cloudletLength;
    private Map<Vm, List<Double>> availableResources;
    private Map<Long, Integer> vmIndices;
    private List<VmResourcesAvailable> vmResourcesAvailables;
    private AbstractLengthPredictor lengthPredictor;

    private CloudletVmMappingSolution solution;

    public MinimizeExeTimeAlgo() {
        vmResourcesAvailables = new ArrayList<>();
        lengthPredictor = LengthPredictor.getInstance();
    }

    public void setVms(List<Vm> vms) {
        this.vms = vms;
        vmIndices = new HashedMap<>();
        for (int i = 0; i < vms.size(); ++i)
            vmIndices.put(vms.get(i).getId(), i);
    }

    @Override
    public void setComingCloudlets(List<Cloudlet> comingCloudlets) {
        super.setComingCloudlets(comingCloudlets);
        this.cloudletLength = new HashedMap<>();

        if (comingCloudlets == null)
            return;

        List<Double> length = this.lengthPredictor.predict(comingCloudlets);
        for (int i = 0; i < comingCloudlets.size(); ++i) {
            this.cloudletLength.put(
                    comingCloudlets.get(i), length.get(i)
            );
        }

//        comingCloudlets.forEach(
//            cloudlet -> this.cloudletLength.put(
//                    cloudlet, this.lengthPredictor.predict(cloudlet)
//            )
//        );
    }

    public void setAvailableResources(Map<Vm, List<Double>> availableResources) {
        this.availableResources = availableResources;
        updateVmInfo(null);
    }

    @Override
    public void updateVmInfo(Map<Cloudlet, Vm> futureRunning) {
        this.vmResourcesAvailables.clear();
        this.vms.forEach(
                vm -> {
                    List<Double> resources = this.availableResources.get(vm);
                    VmResourcesAvailable info = new VmResourcesAvailable(
                            vm.getId(),
                            vm.getTotalMipsCapacity(),
                            resources.get(0),
                            resources.get(1),
                            resources.get(2),
                            resources.get(3)
                    );
                    this.vmResourcesAvailables.add(info);
                }
        );
    }

    @Override
    public CloudletVmMappingSolution getSolution() {
        return this.solution;
    }

    @Override
    public void processing() {

        this.setRunningStatus(true);

        Map<Cloudlet, Vm> result = new HashedMap<>();

        List<Map.Entry<Cloudlet, Double>> cloudlets = cloudletLength.entrySet().stream().sorted(new Comparator<Map.Entry<Cloudlet, Double>>() {
            @Override
            public int compare(Map.Entry<Cloudlet, Double> cloudletDoubleEntry, Map.Entry<Cloudlet, Double> t1) {
                if (cloudletDoubleEntry.getValue() < t1.getValue())
                    return 1;
                else if (cloudletDoubleEntry.getValue().equals(t1.getValue()))
                    return 0;
                else
                    return -1;
            }
        }).collect(Collectors.toList());

        cloudlets.forEach(
                cloudletDoubleEntry -> {
                    Cloudlet cloudlet = cloudletDoubleEntry.getKey();
                    double length = cloudletDoubleEntry.getValue();
                    double cpuRequest = cloudlet.getUtilizationOfCpu();
                    int cpuCores = Math.toIntExact(cloudlet.getNumberOfPes());
                    double ramRequest = cloudlet.getUtilizationOfRam();
                    double bandwidthRequest = cloudlet.getUtilizationOfBw();
                    long maxId = 0;
                    double minTime = 10000000.0;
                    int resourceInd = 0;
                    for (int i = 0; i < vmResourcesAvailables.size(); ++i) {
                        VmResourcesAvailable resources = vmResourcesAvailables.get(i);
                        double exeTime = length / (resources.getMips() * cpuRequest * cpuCores);
                        if (cpuRequest > resources.getCpuPercentage() || ramRequest > resources.getRam())
                            exeTime += 10000;
                        if (exeTime < minTime) {
                            minTime = exeTime;
                            maxId = resources.getId();
                            resourceInd = i;
                        }
                    }
                    result.put(cloudlet, this.vms.get(this.vmIndices.get(maxId)));
                    VmResourcesAvailable r = vmResourcesAvailables.get(resourceInd);
                    r.setCpuPercentage(r.getCpuPercentage() - cpuRequest);
                    r.setRam(r.getRam() - ramRequest);
                    r.setBandwidth(r.getBandwidth() - bandwidthRequest);
                }
        );

        this.solution = new CloudletVmMappingSolution(result, 1.0);

        this.setRunningStatus(false);
    }

    @Override
    public double getSolveTime() {
        return 0;
    }
}
