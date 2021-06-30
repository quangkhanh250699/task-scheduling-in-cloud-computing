package vn.edu.hust.simulation.realtime;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import vn.edu.hust.simulation.reader.TaskInfo;
import vn.edu.hust.simulation.reader.VmInfo;

public abstract class AbstractEntitiesCreator {

    public Cloudlet createCloudlet(TaskInfo info) {
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
