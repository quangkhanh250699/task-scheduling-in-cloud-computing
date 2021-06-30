package vn.edu.hust.simulation.printer;

import org.cloudbus.cloudsim.vms.Vm;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractVmPrinter implements IPrinter<Vm>, CSVPrinter{

    protected String[] titles;

    @Override
    public void print(List<Vm> data, String dataPath) {
        List<String[]> trace = new ArrayList<>();
        trace.add(this.titles);
        data.forEach(
                vm -> {
                    List<String[]> vmUsage = this.convertVmToString(vm);
                    trace.addAll(vmUsage);
                }
        );

        try {
            this.csvWriterOneByOne(trace, dataPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract List<String[]> convertVmToString(Vm vm);
}
