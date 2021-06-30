package vn.edu.hust.simulation.algos.myalgo.basic;

import org.apache.commons.collections4.map.HashedMap;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import vn.edu.hust.simulation.algos.CloudletVmMappingSolution;
import vn.edu.hust.simulation.algos.myalgo.basic.heap_structure.*;
import vn.edu.hust.simulation.reader.SimpleCloudletReader;
import vn.edu.hust.simulation.reader.SimpleVmReader;
import vn.edu.hust.simulation.realtime.RealtimeSimulation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BestFitResourcesAlgo extends AbstractSubSchedulingAlgorithm {

    private int MAX_SIZE = 15000;

    private List<Vm> vms;
    private MaxHeap<VmResourcesAvailable> heap;
    private Map<Long, Integer> vmIndices;

    private CloudletVmMappingSolution solution;

    public BestFitResourcesAlgo() {
        heap = new VmResourcesMaxHeap(MAX_SIZE, new VmResoucesComparator());
    }


    @Override
    public void setVms(List<Vm> availableVms) {
        this.vms = availableVms;
        this.vmIndices = new HashedMap<>();
        for (int i = 0; i < this.vms.size(); ++i)
            this.vmIndices.put(this.vms.get(i).getId(), i);
        this.updateVmInfo(null);
    }

    @Override
    public CloudletVmMappingSolution getSolution() {
        return this.solution;
    }

    @Override
    public void updateVmInfo(Map<Cloudlet, Vm> futureRunning) {
        this.heap.clear();
        Map<Vm, VmResourcesAvailable> reserverdResources = new HashedMap<>();
        if (futureRunning != null) {
            Map<Vm, List<Cloudlet>> mapper = futureRunning.entrySet().stream()
                    .collect(Collectors.groupingBy(
                            Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())
                    ));
            mapper.entrySet().forEach(
                    vmListEntry -> {
                        Vm vm = vmListEntry.getKey();
                        List<Cloudlet> futureCloudlets = vmListEntry.getValue();
                        double cpu = futureCloudlets.stream().mapToDouble(
                                cloudlet -> cloudlet.getUtilizationOfCpu()
                        ).sum();
                        double ram = futureCloudlets.stream().mapToDouble(
                                cloudlet -> cloudlet.getUtilizationOfRam()
                        ).sum();
                        double bandwidth = futureCloudlets.stream().mapToDouble(
                                cloudlet -> cloudlet.getUtilizationOfBw()
                        ).sum();
                        reserverdResources.put(vm, new VmResourcesAvailable(
                                vm.getId(),
                                vm.getTotalMipsCapacity(),
                                cpu,
                                ram,
                                bandwidth,
                                0.0
                        ));
                    }
            );
        }
        VmResourcesAvailable none = new VmResourcesAvailable(
                0, 0, 0, 0, 0, 0
        );
        this.vms.forEach(
                vm -> {
                    VmResourcesAvailable reserved = reserverdResources.getOrDefault(vm, none);
                    VmResourcesAvailable vmResources = new VmResourcesAvailable(
                            vm.getId(),
                            vm.getTotalMipsCapacity() - vm.getTotalCpuMipsUtilization(),
                            1 - vm.getCpuPercentUtilization() - reserved.getCpuPercentage(),
                            vm.getRam().getAvailableResource() - reserved.getRam(),
                            vm.getBw().getAvailableResource() - reserved.getBandwidth(),
                            vm.getStorage().getAvailableResource() - reserved.getStorage()
                    );
                    this.heap.add(vmResources);
                }
        );
    }

    @Override
    protected void processing() {

        this.setRunningStatus(true);

        this.comingCloudlets.sort(new CloudletComparator());
        Map<Cloudlet, Vm> result = new HashedMap<>();
        this.comingCloudlets.forEach(
                cloudlet -> {
                    VmResourcesAvailable info = this.heap.pop();
                    Vm bestVm = this.vms.get(this.vmIndices.get(info.getId()));
                    info.setCpuPercentage(info.getCpuPercentage() - cloudlet.getUtilizationOfCpu());
                    info.setRam(info.getRam() - cloudlet.getUtilizationOfRam());
                    info.setBandwidth(info.getBandwidth() - cloudlet.getUtilizationOfBw());
                    this.heap.add(info);
                    result.put(cloudlet, bestVm);
                }
        );
        this.solution = new CloudletVmMappingSolution(result, 1.0);

        this.setRunningStatus(false);
    }

    public static void main(String[] args) {
        SimpleCloudletReader reader = new SimpleCloudletReader(RealtimeSimulation::createCloudlet);
        List<Cloudlet> cloulets = reader.read("src/main/resources/test_data/cloudlets_v1.csv");
        SimpleVmReader reader1 = new SimpleVmReader(RealtimeSimulation::createVm);
        List<Vm> vms = reader1.read("src/main/resources/test_data/vms_v1.csv");

        BestFitResourcesAlgo algo = new BestFitResourcesAlgo();
        algo.setVms(vms.subList(0, 4));
        algo.setComingCloudlets(cloulets.subList(0, 10));
        algo.solve();

        CloudletVmMappingSolution solution = algo.getSolution();

        solution.getResult().entrySet().forEach(
                cloudletVmEntry -> {
                    System.out.printf("%d -> %d \n", cloudletVmEntry.getKey().getId(), cloudletVmEntry.getValue().getId());
                }
        );
    }
}
