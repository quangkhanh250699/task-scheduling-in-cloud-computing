package vn.edu.hust.simulation.algos.myalgo.basic;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.util.List;

public abstract class AbstractLongShortTermReviser {

    public void update(LongTermQueue longTerm, ShortTermQueue shortTerm, double clock) {
        List<SimpleCloudletQueue> oldQueues = this.getOldQueues(shortTerm, clock);
        oldQueues.forEach(
                simpleClouletQueue -> {
                    List<Cloudlet> longTermCloudlets = this.getLongTermCloudlets(simpleClouletQueue, clock);
                    simpleClouletQueue.removeCloudlets(longTermCloudlets);
                    longTerm.addCloudlets(longTermCloudlets);
                }
        );
    }

    protected abstract List<Cloudlet> getLongTermCloudlets(SimpleCloudletQueue queue, double clock);

    protected abstract List<SimpleCloudletQueue> getOldQueues(ShortTermQueue queue, double clock);
}
