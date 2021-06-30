package vn.edu.hust.simulation;

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
import vn.edu.hust.simulation.algos.CloudletVmMappingSolution;
import vn.edu.hust.simulation.algos.HeuristicAlgo;
import vn.edu.hust.simulation.printer.IPrinter;
import vn.edu.hust.simulation.printer.SimpleCloudletPrinter;
import vn.edu.hust.simulation.reader.SimpleCloudletReader;
import vn.edu.hust.simulation.reader.SimpleVmReader;
import vn.edu.hust.simulation.reader.TaskInfo;
import vn.edu.hust.simulation.reader.VmInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SingleHost {

    private static final int HOSTS = 1;
    private static final int VMS = 3;
    private static final int HOST_PES = 8;
    private static final long HOST_RAM = 4096*100; //in Megabytes
    private static final long HOST_BW = 10000; //in Megabits/s
    private static final long HOST_STORAGE = 10000000; //in Megabytes
    private static final double HOST_MIPS = 100000;

    private static final long CLOUDLET_LENGTH = 10000;

    private static final long VM_PES = 4;
    private static final int  VM_MIPS = 1000;
    private static final long VM_RAM = 4096; //in Megabytes
    private static final long VM_BW = 1000; //in Megabits/s
    private static final long VM_SIZE = 10000; //in Megabytes

//    private static final SimpleAlgo algo = new SimpleAlgo();
    private static final HeuristicAlgo algo = new HeuristicAlgo();

    private final Random random = new Random();

    CloudSim simulator;
    DatacenterBroker broker;
    IPrinter<Cloudlet> printer;

    public static void main(String[] args) {
        SingleHost example = new SingleHost();
        example.run();
    }

    public void run() {
        simulator = new CloudSim();
        printer = new SimpleCloudletPrinter();
        broker = new DatacenterBrokerSimple(simulator, "MyBroker");
        Datacenter datacenter = createDatacenter();
        Host host = datacenter.getHostList().get(0);
        List<Vm> vmList = this.createVms();
        List<Cloudlet> cloudletList = this.createCloudlets();
        for (int i = 0; i < cloudletList.size(); ++i)
            cloudletList.get(i).setId(i);

        algo.setVms(vmList);
        algo.setComingCloudlets(cloudletList);
        algo.solve();

        CloudletVmMappingSolution solution = algo.getSolution();
        CloudletToVmDispatcher.setCloudletsToVms(solution.getResult());

        broker.submitVmList(vmList);
        broker.submitCloudletList(cloudletList);

        simulator.start();

        this.printCloudlets(broker);
        this.printer.print(broker.getCloudletFinishedList(), "test.csv");
    }

    private Datacenter createDatacenter() {
        List<Host> list = new ArrayList<>();
        list.add(createHost());
        Datacenter datacenter =
            new DatacenterSimple(simulator, list, new VmAllocationPolicySimple());
        return datacenter;
    }

    private List<Cloudlet> createCloudlets() {
        /*
        The trace doesn't define the actual number of CPU cores (PEs) a Cloudlet will require,
        but just a percentage of the number of cores that is required.
        This way, we have to compute the actual number of cores.
        This is different from the CPU UtilizationModel, which is defined
        in the "task usage" trace files.
        */
        String dataPath = "src/main/resources/test_data/cloudlets_v1.csv";
        SimpleCloudletReader reader = new SimpleCloudletReader(this::createCloudlet);
        return reader.read(dataPath);
    }

    private List<Vm> createVms() {
        String dataPath = "src/main/resources/test_data/vms_v1.csv";
        SimpleVmReader reader = new SimpleVmReader(this::createVm);
        return reader.read(dataPath);
    }

    private Cloudlet createCloudlet(TaskInfo info) {
        long length = info.getLength();
        int cpuCores = info.getCpuCores();
        double cpu = info.getCpuRequest();
        double memory = info.getMemoryRequest();
        double diskspace = info.getDiskspaceRequest();
        double bandwidth = info.getBandwidthRequest();

        final UtilizationModelDynamic utilizationCpu =
            new UtilizationModelDynamic(UtilizationModel.Unit.PERCENTAGE, cpu, 1);
        final UtilizationModelDynamic utilizationMemory =
            new UtilizationModelDynamic(UtilizationModel.Unit.ABSOLUTE, memory, 1000);
        final UtilizationModelDynamic utilizationStorage =
            new UtilizationModelDynamic(UtilizationModel.Unit.ABSOLUTE, 0, diskspace);
        final UtilizationModelDynamic utilizationBandwidth =
            new UtilizationModelDynamic(UtilizationModel.Unit.ABSOLUTE, bandwidth, 100);

        return new CloudletSimple(length, cpuCores)
            .setOutputSize(10)
            .setFileSize(10)
            .setUtilizationModelCpu(utilizationCpu)
            .setUtilizationModelRam(utilizationMemory)
            .setUtilizationModelBw(utilizationBandwidth);
    }

    private Vm createVm (Host host) {
        Vm vm = new VmSimple(
            VM_MIPS, VM_PES, new CloudletSchedulerTimeShared()
        );
        vm.setRam(VM_RAM);
        vm.setBw(10);
        vm.setSize(VM_SIZE);
        vm.setHost(host);
        return vm;
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
    }
}
