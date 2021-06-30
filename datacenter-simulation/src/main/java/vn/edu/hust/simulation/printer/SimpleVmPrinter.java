package vn.edu.hust.simulation.printer;

import org.cloudbus.cloudsim.vms.Vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleVmPrinter extends AbstractVmPrinter{

    public SimpleVmPrinter() {
        this.titles = new String[] {"id", "timestamp", "cpu_usage"};
    }

    @Override
    protected List<String[]> convertVmToString(Vm vm) {
        List<String[]> usageTrace = new ArrayList<>();

        if (vm.getUtilizationHistory().getHistory().isEmpty()) {
            System.out.println("\tThere isn't any usage history");
            return usageTrace;
        }

        Set<Map.Entry<Double, Double>> trace = vm.getUtilizationHistory().getHistory().entrySet();
        String id = Long.toString(vm.getId());

        trace.stream().forEach(
                entry -> {
                    double time = entry.getKey();
                    double usage = entry.getValue();
                    usageTrace.add(
                            new String[] {id, Double.toString(time), Double.toString(usage)}
                    );
                }
        );

        return usageTrace;
    }
}
