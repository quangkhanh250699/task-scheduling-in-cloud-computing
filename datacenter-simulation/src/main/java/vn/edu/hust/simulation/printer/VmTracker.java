package vn.edu.hust.simulation.printer;

import org.cloudbus.cloudsim.vms.Vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VmTracker implements CSVPrinter {

    private static VmTracker instance;

    private int size;
    private List<Double> timestamps;
    private List<List<List<Double>>> estimate;
    private List<List<List<Double>>> real;
    private List<List<List<Double>>> hardEstimate;
    private List<List<Integer>> numberRunning;

    public static VmTracker getInstance() {
        if (instance == null)
            instance = new VmTracker(10);
        return instance;
    }

    public static void Initial(int size) {
        instance = new VmTracker(size);
    }

    private VmTracker(int size) {
        this.size = size;
        estimate = new ArrayList<>();
        for (int i = 0; i < size; ++i)
            estimate.add(new ArrayList<>());
        real = new ArrayList<>();
        for (int i = 0; i < size; ++i)
            real.add(new ArrayList<>());
        hardEstimate = new ArrayList<>();
        for (int i = 0; i < size; ++i)
            hardEstimate.add(new ArrayList<>());
        numberRunning = new ArrayList<>();
        for (int i = 0; i < size; ++i)
            numberRunning.add(new ArrayList<>());
        timestamps = new ArrayList<>();
    }

    public void addHardEstimate(Map<Vm, List<Double>> mapper) {
        mapper.forEach(((vm, resouces) -> {
            int id = Math.toIntExact(vm.getId());
            hardEstimate.get(id).add(resouces.subList(0, 2));
        }));
    }

    public void addEstimate(Map<Vm, List<Double>> mapper) {
        mapper.forEach((vm, resouces) -> {
            int id = Math.toIntExact(vm.getId());
            estimate.get(id).add(resouces.subList(0, 2));
        });
    }

    public void addReal(List<Vm> vms) {
        vms.forEach(vm -> {
            int id = Math.toIntExact(vm.getId());
            double usage = vm.getCloudletScheduler().getCloudletExecList().stream().mapToDouble(
                    cloudletExecution -> cloudletExecution.getCloudlet().getUtilizationOfCpu()
            ).sum();
//            double cpu = 1 - vm.getCpuPercentUtilization();
            double cpu = Math.max(0, 1 - usage / 2);
            double ram = vm.getRam().getAllocatedResource();
            List<Double> r = new ArrayList<>();
            r.add(cpu);
            r.add(ram);
            real.get(id).add(r);
        });
    }

    public void addNumberRunning(List<Vm> vms) {
        vms.forEach(vm -> {
            int id = Math.toIntExact(vm.getId());
            int numberRunningCloudlets = vm.getCloudletScheduler().getCloudletExecList().size();
            this.numberRunning.get(id).add(numberRunningCloudlets);
        });
    }

    public void addTimestamp(double time) {
        timestamps.add(time);
    }

    public void print(String dataPath) {
        try {
            this.csvWriterOneByOne(this.convertToString(), dataPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String[]> convertToString() {
        List<String[]> content = new ArrayList<>();
        String[] titles = new String[] {
                "timestamp", "vmId", "cpu_est", "ram_est", "cpu_real", "ram_real",
                "cpu_hard_est", "ram_hard_est", "numberRunningCloudlets"
        };
        content.add(titles);

        for (int i = 0; i < timestamps.size(); ++i) {
            double time = timestamps.get(i);

            for (int j = 0; j < size; ++j) {
                int vmId = j;

                double cpuEst = estimate.get(j).get(i).get(0);
                double ramEst = estimate.get(j).get(i).get(1);
                double cpuReal = real.get(j).get(i).get(0);
                double ramReal = real.get(j).get(i).get(1);
                double cpuHard = hardEstimate.get(j).get(i).get(0);
                double ramHard = hardEstimate.get(j).get(i).get(1);
                int numberRunningCloudlets = numberRunning.get(j).get(i);

                String[] info = new String[] {
                        Double.toString(time),
                        Integer.toString(vmId),
                        Double.toString(cpuEst),
                        Double.toString(ramEst),
                        Double.toString(cpuReal),
                        Double.toString(ramReal),
                        Double.toString(cpuHard),
                        Double.toString(ramHard),
                        Integer.toString(numberRunningCloudlets)
                };
                content.add(info);
            }
        }

        return content;
    }

    public static void main(String[] args) {
        List<Integer> a = new ArrayList<>();
        System.out.println(a.size());
    }
}
