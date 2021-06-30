package vn.edu.hust.simulation.algos.myalgo.basic.heap_structure;

import org.cloudbus.cloudsim.vms.Vm;

import java.util.Comparator;

public class VmComparator implements Comparator<Vm> {

    @Override
    public int compare(Vm vm, Vm t1) {
        double cpu1 = vm.getTotalMipsCapacity() - vm.getTotalCpuMipsUtilization();
        double cpu2 = t1.getTotalMipsCapacity() - t1.getTotalCpuMipsUtilization();
        double ram1 = vm.getRam().getAvailableResource();
        double ram2 = t1.getRam().getAvailableResource();

        if (cpu1 < cpu2)
            return 1;
        else if (cpu1 > cpu2)
            return -1;
        else if (ram1 < ram2)
            return 1;
        else if (ram1 > ram2)
            return -1;
        else return 0;
    }
}
