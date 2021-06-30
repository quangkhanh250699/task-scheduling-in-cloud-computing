package vn.edu.hust.simulation.algos.myalgo.basic;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.List;

public abstract class AbstractStatusPredictor {

    protected LongTermQueue longTermQueue;
    protected ShortTermQueue shortTermQueue;
    protected List<Vm> vms;

    public abstract List<Double> estimateStatusProb(Cloudlet cloudlet);

    public abstract List<Double[]> estimateStatusProb(List<Cloudlet> cloudlets);

    public abstract void fit();

    public void setVms(List<Vm> vms) {
        this.vms = vms;
    }

    public void setLongTermQueue(LongTermQueue longTermQueue) {
        this.longTermQueue = longTermQueue;
    }

    public void setShortTermQueue(ShortTermQueue shortTermQueue) {
        this.shortTermQueue = shortTermQueue;
    }
}
