package vn.edu.hust.simulation.realtime;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudsimplus.listeners.EventInfo;
import vn.edu.hust.simulation.CloudletToVmDispatcher;
import vn.edu.hust.simulation.algos.AbstractSchedulingAlgorithm;
import vn.edu.hust.simulation.printer.AlgoPrinter;
import vn.edu.hust.simulation.printer.RunningTracker;
import vn.edu.hust.simulation.printer.VmTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractSchedulingController {

    private final int NUM_THREADS = 20;

    private int lastCpuCheck = -1;

    protected AbstractSchedulingAlgorithm algo;
    protected AlgoPrinter algoPrinter;
    protected DatacenterBroker broker;
    protected List<Cloudlet> waitingQueue;
    protected List<Vm> availableVms;
    protected Simulation simulation;
    protected AbstractMappedCloudletStore cloudletStore;
    protected VmTracker tracker;
    protected RunningTracker<Vm> cpuTracker;

    protected List<Cloudlet> simulatedCloudlets;

    protected double waitingTime;
    protected int maxWaitingTasks;
    protected double previousSchedulingTime;
    protected RunningTracker<Cloudlet> cloudletTracker;

//    protected ExecutorService executor;

    public AbstractSchedulingController(AbstractSchedulingAlgorithm algo, AlgoPrinter algoPrinter, DatacenterBroker broker,
                                        RunningTracker<Cloudlet> cloudletTracker, RunningTracker<Vm> cpuTracker,
                                        Simulation simulation, double waitingTime, int maxWaitingTasks) {
        this.algo = algo;
        this.algoPrinter = algoPrinter;
        this.broker = broker;
        this.simulation = simulation;
        this.waitingTime = waitingTime;
        this.maxWaitingTasks = maxWaitingTasks;
        this.waitingQueue = new ArrayList<>();
        this.cloudletStore = new MappedCloudletStore();
        this.cloudletTracker = cloudletTracker;
        this.tracker = VmTracker.getInstance();
        this.cpuTracker = cpuTracker;
    }

    public List<Cloudlet> getWaitingQueue() {
        return waitingQueue;
    }

    public List<Vm> getAvailableVms() {
        return availableVms;
    }

    public List<Cloudlet> getSimulatedCloudlets() {
        return simulatedCloudlets;
    }

    public double getWaitingTime() {
        return waitingTime;
    }

    public int getMaxWaitingTasks() {
        return maxWaitingTasks;
    }

    public void onClockSticks(final EventInfo evt) {
        if (this.simulatedCloudlets.isEmpty())
            return;

        double time = this.simulation.clock();
        double from = time - 1;
        double to = time;

        List<Cloudlet> comingCloulets = this.getComingCloudlets(from, to);
        this.waitingQueue.addAll(comingCloulets);

        this.addCpuUsageInfo(time);

        if (this.checkForSubmittingTasks())
            this.scheduling();

        this.submitCloudlet();
    }

    private void addCpuUsageInfo(double time) {
        int currentCheck = (int) time;
        if (currentCheck != this.lastCpuCheck) {
            this.availableVms.forEach(
                    vm -> this.cpuTracker.addInfo(vm, time, "")
            );
            this.lastCpuCheck = currentCheck;
        }
    }

    private void addRunningInfo(double now, String mode) {
        for (Vm vm : this.availableVms) {
            vm.getCloudletScheduler().getCloudletExecList().forEach(
                    cloudletExecution -> {
                        Cloudlet cloudlet = cloudletExecution.getCloudlet();
                        if (cloudlet.getStatus() != Cloudlet.Status.SUCCESS)
                            this.cloudletTracker.addInfo(cloudlet, now, mode);
                    }
            );
        }
    }

    private void scheduling() {
        this.makeSolution();
        while (this.algo.isRunning()) {};
        this.algo.solve();
        double now = this.simulation.clock();
        this.algoPrinter.addElements(algo.getComingCloudlets().size(), algo.getSolveTime());
        this.cloudletStore.addMappedCloudlet(algo.getComingCloudlets(), now + algo.getSolveTime());
        Map<Cloudlet, Vm> map = this.algo.getSolution().getResult();
        CloudletToVmDispatcher.setCloudletsToVms(map);
        this.setupAfterScheduling();
        this.addRunningInfo(now, "scheduling");
    }

    private void submitCloudlet() {
        double now = this.simulation.clock();
        List<Cloudlet> cloudlets = this.cloudletStore.getSubmittingCloudlet(now);
        if (cloudlets != null) {
            this.addRunningInfo(now, "submitting");
            this.tracker.addTimestamp(now);
            this.tracker.addReal(this.availableVms);
            this.broker.submitCloudletList(cloudlets);
            System.out.printf("Submitting %d tasks at %f \n", cloudlets.size(), now);
        }
    }

    protected abstract void makeSolution();

    protected abstract List<Cloudlet> getComingCloudlets(double from, double to);

    public abstract boolean checkForSubmittingTasks();

    protected abstract void setupAfterScheduling();
}
