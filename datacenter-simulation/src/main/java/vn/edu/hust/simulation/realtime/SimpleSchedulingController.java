package vn.edu.hust.simulation.realtime;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import vn.edu.hust.simulation.algos.AbstractSchedulingAlgorithm;
import vn.edu.hust.simulation.printer.AlgoPrinter;
import vn.edu.hust.simulation.printer.RunningTracker;
import vn.edu.hust.simulation.reader.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleSchedulingController extends AbstractSchedulingController{

    private String SIMULATED_CLOUDLETS_PATH = "src/main/resources/test_data/cloudlets_v6.csv";
    private String VM_PATH = "src/main/resources/test_data/vms_v3.csv";

//    private double commingRate = 0.3;
    private int currentIndex = -1;

    private SimpleCloudletReader cloudletReader;
    private SimpleVmReader vmReader;
    private List<Double> commingTime;
    private Random random = new Random();


    public SimpleSchedulingController(AbstractSchedulingAlgorithm algo, AlgoPrinter algoPrinter, DatacenterBroker broker,
                                      RunningTracker<Cloudlet> cloudletTracker, RunningTracker<Vm> cpuTracker,
                                      Simulation simulation, double waitingTime, int maxWaitingTasks) {
        super(algo, algoPrinter, broker, cloudletTracker, cpuTracker, simulation, waitingTime, maxWaitingTasks);
        this.cloudletReader = new SimpleCloudletReaderV3(this::createCloudlet);
        this.vmReader = new SimpleVmReader(this::createVm);
        this.updateSimulatedCloudlets();
        this.updateVms();
        this.enableHistoryTrace();
    }

    private void updateVms() {
        this.availableVms = this.vmReader.read(VM_PATH);
    }

    private void enableHistoryTrace() {
        this.availableVms.forEach(
            vm -> vm.getUtilizationHistory().enable()
        );
    }

    private void updateSimulatedCloudlets() {
        if (this.simulatedCloudlets == null)
            fetchSimulatedCloudlets();
    }

    private void fetchSimulatedCloudlets() {
        this.simulatedCloudlets = cloudletReader.read(SIMULATED_CLOUDLETS_PATH);
        this.commingTime = cloudletReader.readComingTime(SIMULATED_CLOUDLETS_PATH);
    }

    @Override
    protected List<Cloudlet> getComingCloudlets(double from, double to) {
        List<Cloudlet> newCloudlets = new ArrayList<>();

        while (currentIndex + 1 < this.simulatedCloudlets.size() &&
            this.commingTime.get(currentIndex + 1) < to) {
            currentIndex += 1;
            newCloudlets.add(this.simulatedCloudlets.get(currentIndex));
        }

        return newCloudlets;
    }

    @Override
    protected void makeSolution() {
        this.algo.setVms(availableVms);
        this.algo.setComingCloudlets(this.waitingQueue);
        this.waitingQueue.clear();
    }

    @Override
    public boolean checkForSubmittingTasks() {
        if (this.waitingQueue.size() >= this.maxWaitingTasks)
            return true;
        return (this.simulation.clock() - this.previousSchedulingTime > this.waitingTime)
                && this.waitingQueue.size() >= 1;
    }

    @Override
    protected void setupAfterScheduling() {
        this.previousSchedulingTime = this.simulation.clock();
    }

    private Cloudlet createCloudlet(TaskInfo info) {
        int id = info.getId();
        long length = info.getLength();
        int cpuCores = info.getCpuCores();
        double cpu = info.getCpuRequest();
        double memory = info.getMemoryRequest();
        double diskspace = info.getDiskspaceRequest();
        double bandwidth = info.getBandwidthRequest();

        final UtilizationModelDynamic utilizationCpu =
            new UtilizationModelDynamic(UtilizationModel.Unit.PERCENTAGE, cpu, cpu);
        final UtilizationModelDynamic utilizationMemory =
            new UtilizationModelDynamic(UtilizationModel.Unit.ABSOLUTE, memory, memory);
        final UtilizationModelDynamic utilizationStorage =
            new UtilizationModelDynamic(UtilizationModel.Unit.ABSOLUTE, diskspace, diskspace);
        final UtilizationModelDynamic utilizationBandwidth =
            new UtilizationModelDynamic(UtilizationModel.Unit.ABSOLUTE, bandwidth, bandwidth);

        Cloudlet cloudlet =  new CloudletSimple(length, cpuCores)
            .setOutputSize(10)
            .setFileSize(10)
            .setUtilizationModelCpu(utilizationCpu)
            .setUtilizationModelRam(utilizationMemory)
            .setUtilizationModelBw(utilizationBandwidth);
        cloudlet.setId(id);
        return cloudlet;
    }

    private Vm createVm (VmInfo info) {
        Vm vm = new VmSimple(
            info.getMips(), info.getCpuCores(), new CloudletSchedulerTimeShared()
        );
        vm.setId(info.getId());
        vm.setRam(info.getRam());
        vm.setSize(info.getStorage());
        vm.setBw(info.getBandwidth());
        return vm;
    }
}
