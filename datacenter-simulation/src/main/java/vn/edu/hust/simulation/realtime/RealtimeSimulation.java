package vn.edu.hust.simulation.realtime;

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.ResourceProvisioner;
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.schedulers.vm.VmScheduler;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.util.Conversion;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.builders.tables.TextTableColumn;
import vn.edu.hust.simulation.algos.AbstractSchedulingAlgorithm;
import vn.edu.hust.simulation.algos.myalgo.basic.MyAlgo;
import vn.edu.hust.simulation.printer.*;
import vn.edu.hust.simulation.reader.TaskInfo;
import vn.edu.hust.simulation.reader.VmInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class RealtimeSimulation {

    private static final int HOSTS = 100;
    private static final int VMS = 10;
    private static final int HOST_PES = 8;
    private static final long HOST_RAM = 4096*100; //in Megabytes
    private static final long HOST_BW = 10000; //in Megabits/s
    private static final long HOST_STORAGE = 10000000; //in Megabytes
    private static final double HOST_MIPS = 100000;

    private String CLOUDLET_DATA_PATH;
    private String VM_DATA_PATH;


    private int SIMULATED_TIME = 1100;

    private AlgoSelector selector;

    private CloudSim simulator;
    private AbstractSchedulingController controller;
    private AbstractSchedulingAlgorithm algo;
    private IControllerFactory controllerFactory;
    private Datacenter datacenter;
    private DatacenterBroker broker;
    private IPrinter<Cloudlet> cloudletPrinter;
    private IPrinter<Vm> vmPrinter;
    private AlgoPrinter algoPrinter;
    private RunningTracker<Cloudlet> cloudletTracker;
    private RunningTracker<Vm> cpuTracker;

    public static void main(String[] args) {
        new RealtimeSimulation();
    }

    public RealtimeSimulation() {
        this.setup();

        this.simulator.addOnClockTickListener(this.controller::onClockSticks);

        final double finishTime = this.simulator.start();

        printCloudlets(broker);

        showCpuUtilizationForAllVms(finishTime, this.controller.getAvailableVms());

        this.algoPrinter.print(this.selector.getALGO_SOLVE_TIME());
        this.cloudletTracker.printCSV(this.selector.getCLOUDLET_RUNNING_PATH());
        this.cpuTracker.printCSV(this.selector.getCPU_USAGE_PATH());

        if (this.algo instanceof MyAlgo) {
            VmTracker.getInstance().print(this.selector.get_COMPARISION_DATA_PATH());
            String dataPath = this.selector.DATA_PATH + "short_queue.csv";
            MyAlgo al = (MyAlgo) this.algo;
            al.printShortQueue(dataPath);
        }
    }

    private void setup() {
        VmTracker.Initial(10);

        this.simulator = new CloudSim();
        simulator.terminateAt(SIMULATED_TIME);

        selector = new AlgoSelector(this.simulator);
//        selector.loadBalancingAlgo();
        selector.loadWorstfit();
//        selector.loadMyAlgo();
//        selector.loadBestfit();
//        selector.loadRR();

        this.algoPrinter = new AlgoPrinter();

        this.VM_DATA_PATH = this.selector.getVM_DATA_PATH();
        this.CLOUDLET_DATA_PATH = this.selector.getCLOUDLET_DATA_PATH();

        this.cloudletPrinter = new SimpleCloudletPrinter();
        this.vmPrinter = new SimpleVmPrinter();


        this.datacenter = this.createDatacenter();
        this.broker = new DatacenterBrokerSimple(simulator, "MyBroker");
        this.cloudletTracker = new CloudletRunningTracker();
        this.cpuTracker = new CpuUsagePrinter();

        this.algo = selector.getAlgo();

        this.controllerFactory = new SimpleControllerFactory(simulator, broker, algoPrinter,
                cloudletTracker, this.cpuTracker);

        this.controller = this.controllerFactory.createController(this.algo);

        broker.submitVmList(controller.getAvailableVms());
    }

    private Datacenter createDatacenter() {
        List<Host> list = new ArrayList<>();
        list = this.createHosts();
        Datacenter datacenter =
            new DatacenterSimple(simulator, list, new VmAllocationPolicySimple());
        return datacenter;
    }

    private List<Host> createHosts() {
        List<Host> list = new ArrayList<>();

        for (int i = 0; i < HOSTS; ++i)
            list.add(createHost());

        return list;
    }

    public static Cloudlet createCloudlet(TaskInfo info) {
        long length = info.getLength();
        int id = info.getId();
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

        Cloudlet c =  new CloudletSimple(length, cpuCores)
            .setOutputSize(10)
            .setFileSize(10)
            .setUtilizationModelCpu(utilizationCpu)
            .setUtilizationModelRam(utilizationMemory)
            .setUtilizationModelBw(utilizationBandwidth);
        c.setId(id);
        return c;
    }

    public static Vm createVm (VmInfo info) {
        Vm vm = new VmSimple(
            info.getMips(), info.getCpuCores(), new CloudletSchedulerTimeShared()
        );
        vm.setId(info.getId());
        vm.setRam(info.getRam());
        vm.setSize(info.getStorage());
        vm.setBw(info.getBandwidth());
        return vm;
    }

    private Host createHost() {
        final List<Pe> peList = createPesList();
        final ResourceProvisioner ramProvisioner = new ResourceProvisionerSimple();
        final ResourceProvisioner bwProvisioner = new ResourceProvisionerSimple();
        final VmScheduler vmScheduler = new VmSchedulerTimeShared();
        final Host host = new HostSimple(HOST_RAM, HOST_BW, HOST_STORAGE, peList);
        host
            .setRamProvisioner(ramProvisioner)
            .setBwProvisioner(bwProvisioner)
            .setVmScheduler(vmScheduler);
        return host;
    }

    private List<Pe> createPesList() {
        List<Pe> list = new ArrayList<>(HOST_PES);
        for (int i = 0; i < HOST_PES; ++i) {
            list.add(
                new PeSimple(HOST_MIPS, new PeProvisionerSimple())
            );
        }
        return list;
    }

    private long getCloudletSizeInMB(final Cloudlet cloudlet) {
        return (long) Conversion.bytesToMegaBytes(cloudlet.getFileSize());
    }

    private long getVmSize(final Cloudlet cloudlet) {
        return cloudlet.getVm().getStorage().getCapacity();
    }

    private void printCloudlets(final DatacenterBroker broker) {
        final String username = broker.getName().replace("Broker_", "");
        final List<Cloudlet> list = broker.getCloudletFinishedList();
        list.sort(Comparator.comparingLong(Cloudlet::getId));
        new CloudletsTableBuilder(list)
            .addColumn(0, new TextTableColumn("Job", "ID"), Cloudlet::getJobId)
            .addColumn(7, new TextTableColumn("VM Size", "MB"), this::getVmSize)
            .addColumn(8, new TextTableColumn("Cloudlet Size", "MB"), this::getCloudletSizeInMB)
            .addColumn(10, new TextTableColumn("Waiting Time", "Seconds").setFormat("%.0f"), Cloudlet::getWaitingTime)
            .setTitle("Simulation results for Broker representing the username " + username)
            .build();
        this.cloudletPrinter.print(list, this.CLOUDLET_DATA_PATH);
    }

    private void showCpuUtilizationForAllVms(final double simulationFinishTime, List<Vm> vmlist) {
        System.out.printf("%nHosts CPU utilization history for the entire simulation period%n%n");
        int numberOfUsageHistoryEntries = 0;
        for (Vm vm : vmlist) {
            System.out.printf("VM %d%n", vm.getId());
            if (vm.getUtilizationHistory().getHistory().isEmpty()) {
                System.out.println("\tThere isn't any usage history");
                continue;
            }

            for (Map.Entry<Double, Double> entry : vm.getUtilizationHistory().getHistory().entrySet()) {
                final double time = entry.getKey();
                final double vmCpuUsage = entry.getValue() * 100;
                if (vmCpuUsage > 0) {
                    numberOfUsageHistoryEntries++;
                    System.out.printf("\tTime: %2.0f CPU Utilization: %6.2f%%%n", time, vmCpuUsage);
                }
            }
        }
        this.vmPrinter.print(vmlist, VM_DATA_PATH);
    }
}
