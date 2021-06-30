package vn.edu.hust.simulation.algos.myalgo.basic.heap_structure;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.util.Comparator;

public class CloudletComparator implements Comparator<Cloudlet> {

    @Override
    public int compare(Cloudlet cloudlet, Cloudlet t1) {
        double cpu1 = cloudlet.getUtilizationModelCpu().getUtilization();
        double cpu2 = t1.getUtilizationModelCpu().getUtilization();
        double ram1 = cloudlet.getUtilizationOfRam();
        double ram2 = t1.getUtilizationOfRam();

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
