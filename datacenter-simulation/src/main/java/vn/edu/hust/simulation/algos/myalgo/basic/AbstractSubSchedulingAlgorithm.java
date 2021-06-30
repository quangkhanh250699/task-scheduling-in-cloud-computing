package vn.edu.hust.simulation.algos.myalgo.basic;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;
import vn.edu.hust.simulation.algos.AbstractSchedulingAlgorithm;

import java.util.Map;

public abstract class AbstractSubSchedulingAlgorithm extends AbstractSchedulingAlgorithm {

    public abstract void updateVmInfo(Map<Cloudlet, Vm> futureRunning);
}
