package vn.edu.hust.simulation.algos.myalgo.basic.heap_structure;

import java.util.Comparator;

public class VmResoucesComparator implements Comparator<VmResourcesAvailable> {

    @Override
    public int compare(VmResourcesAvailable vmResourcesAvailable, VmResourcesAvailable t1) {
        double cpu1 = vmResourcesAvailable.getCpuPercentage();
        double cpu2 = t1.getCpuPercentage();
        double ram1 = vmResourcesAvailable.getRam();
        double ram2 = t1.getRam();

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
