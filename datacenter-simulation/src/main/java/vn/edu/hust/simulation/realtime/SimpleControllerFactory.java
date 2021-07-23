package vn.edu.hust.simulation.realtime;

import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.vms.Vm;
import vn.edu.hust.simulation.algos.AbstractSchedulingAlgorithm;
import vn.edu.hust.simulation.printer.AlgoPrinter;
import vn.edu.hust.simulation.printer.RunningTracker;

import java.security.PrivateKey;

public class SimpleControllerFactory implements IControllerFactory{

    private double MAX_WAITING_TIME = 10;
    private int MAX_WATING_TASKS = 100;

    private Simulation simulation;
    private DatacenterBroker broker;
    private AlgoPrinter algoPrinter;
    private RunningTracker<Cloudlet> cloudletTracker;
    private RunningTracker<Vm> cpuTracker;

    public SimpleControllerFactory(Simulation simulation, DatacenterBroker broker,
                                   AlgoPrinter algoPrinter,
                                   RunningTracker<Cloudlet> cloudletTracker,
                                   RunningTracker<Vm> cpuTracker) {
        this.simulation = simulation;
        this.broker = broker;
        this.algoPrinter = algoPrinter;
        this.cloudletTracker = cloudletTracker;
        this.cpuTracker = cpuTracker;
    }

    @Override
    public AbstractSchedulingController createController(AbstractSchedulingAlgorithm algo) {
        return new SimpleSchedulingController(algo, algoPrinter, this.broker, this.cloudletTracker,
                this.cpuTracker, this.simulation, MAX_WAITING_TIME, MAX_WATING_TASKS);
    }
}
