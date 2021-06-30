package vn.edu.hust.simulation.printer;

import org.cloudbus.cloudsim.vms.Vm;

import java.util.ArrayList;
import java.util.List;

public class CpuUsagePrinter  implements RunningTracker<Vm>, CSVPrinter {

    private final String[] titles;
    private List<String[]> info;

    public CpuUsagePrinter() {
        this.titles = new String[] {"id", "timestamp", "cpu_usage"};
        this.info = new ArrayList<>();
    }

    @Override
    public void addInfo(Vm vm, double timestamp, String mode) {
        String cpuUsage = Double.toString(this.getCpuUsage(vm));
        String timeString = Double.toString(timestamp);
        String id = Long.toString(vm.getId());

        this.info.add(
                new String[] {id, timeString, cpuUsage}
        );
    }

    private double getCpuUsage(Vm vm) {
        return vm.getCloudletScheduler().getCloudletExecList().stream().mapToDouble(
                cloudletExecution -> cloudletExecution.getCloudlet().getUtilizationOfCpu()
        ).sum();
    }

    @Override
    public void printCSV(String dataPath) {
        try {
            List<String[]> data = new ArrayList<>();
            data.add(this.titles);
            data.addAll(this.info);
            this.csvWriterOneByOne(data, dataPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
