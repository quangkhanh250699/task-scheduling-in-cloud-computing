package vn.edu.hust.simulation.printer;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.util.ArrayList;
import java.util.List;

public class CloudletRunningTracker implements RunningTracker<Cloudlet>, CSVPrinter{

    List<String[]> runningInfo;
    String[] titles;

    public CloudletRunningTracker() {
        this.titles = new String[] {"timestamp", "cloudletId", "priority", "cpuRequest", "ramRequest", "status",
                "startTime", "vmID", "MIPS", "cpuUsage", "ramUsage", "totalRam", "numberRunningCloudlets", "mode"};
        this.runningInfo = new ArrayList<>();
        this.runningInfo.add(this.titles);
    }

    @Override
    public void addInfo(Cloudlet cloudlet, double timestamp, String mode) {
        String[] info = this.convertToString(cloudlet, timestamp, mode);
        this.runningInfo.add(info);
    }

    @Override
    public void printCSV(String dataPath) {
        try {
            this.csvWriterOneByOne(this.runningInfo, dataPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] convertToString(Cloudlet cloudlet, double timestamp, String mode) {
        String timeString = Double.toString(timestamp);
        String cloudletID = Long.toString(cloudlet.getId());
        String priority = Integer.toString(cloudlet.getPriority());
        String cpuRequest = Double.toString(cloudlet.getUtilizationOfCpu());
        String ramRequest = Double.toString(cloudlet.getUtilizationOfRam());
        String status = cloudlet.getStatus().toString();
        String startTime = Double.toString(cloudlet.getExecStartTime());
        String vmId = Long.toString(cloudlet.getVm().getId());
        String mips = Double.toString(cloudlet.getVm().getTotalMipsCapacity());
        String cpuUsage = Double.toString(cloudlet.getVm().getCpuPercentUtilization());
        String ramUsage = Double.toString(cloudlet.getVm().getRam().getAvailableResource());
        String totalRam = Double.toString(cloudlet.getVm().getRam().getCapacity());
        String numberRunningCloudlets = Integer.toString(cloudlet.getVm().getCloudletScheduler().getCloudletExecList().size());

        return new String[] {timeString, cloudletID, priority, cpuRequest, ramRequest, status, startTime,
                                vmId, mips, cpuUsage, ramUsage, totalRam, numberRunningCloudlets, mode};
    }
}
