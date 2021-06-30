package vn.edu.hust.simulation.printer;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.util.Conversion;

public class SimpleCloudletPrinter extends AbstractCloudletPrinter{

    @Override
    protected String[] generateTitles() {
        return new String[]{
            "CloudletID",
            "Priority",
            "Status",
            "vmID",
            "vmSize",
            "vmMIPS",
            "cloudletSize",
            "waitingTimeSeconds",
            "cloudletLength",
            "cpuRequest",
            "memoryRequest",
            "cloudletCPUCores",
            "StartTime",
            "finishTime",
            "exeTime"
        };
    }

    @Override
    protected String[] convertToString(Cloudlet cloudlet) {
        String id = Long.toString(cloudlet.getId());
        String priority = Integer.toString(cloudlet.getPriority());
        String status = cloudlet.getStatus().toString();
        String vmId = Long.toString(cloudlet.getVm().getId());
        String vmSize = Long.toString(cloudlet.getVm().getStorage().getCapacity());
        String vmMIPS = Double.toString(cloudlet.getVm().getMips());
        String cloudletSize = Long.toString((long) Conversion.bytesToMegaBytes(cloudlet.getFileSize()));
        String waitingTimeSeconds = Double.toString(cloudlet.getWaitingTime());
        String cloudletLength = Long.toString(cloudlet.getLength());
        String cpuRequest = Double.toString(cloudlet.getUtilizationOfCpu());
        String memoryRequest = Double.toString(cloudlet.getUtilizationOfRam());
        String cloudletCPUCores = Long.toString(cloudlet.getNumberOfPes());
        String startTime = Double.toString(cloudlet.getExecStartTime());
        String finishTime = Double.toString(cloudlet.getFinishTime());
        String exeTime = Double.toString(cloudlet.getFinishTime() - cloudlet.getExecStartTime());

        return new String[]{id, priority, status, vmId, vmSize, vmMIPS, cloudletSize, waitingTimeSeconds,
                cloudletLength, cpuRequest, memoryRequest, cloudletCPUCores, startTime, finishTime, exeTime};
    }
}
