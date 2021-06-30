package vn.edu.hust.simulation.algos.myalgo.basic;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.util.List;
import java.util.stream.Collectors;

public class LongShorttermReviser extends AbstractLongShortTermReviser{

    private final int MAX_SHORT_TIME = 200;

    @Override
    protected List<Cloudlet> getLongTermCloudlets(SimpleCloudletQueue queue, double clock) {
        return queue.currentQueue().stream().filter(
                cloudlet -> (clock - cloudlet.getExecStartTime() > MAX_SHORT_TIME)
        ).collect(Collectors.toList());
    }

    @Override
    protected List<SimpleCloudletQueue> getOldQueues(ShortTermQueue queue, double clock) {
        List<SimpleCloudletQueue> oldQueue = queue.getQueues().stream().filter(
                q -> (clock - q.getStartRunningTime() > MAX_SHORT_TIME)
        ).collect(Collectors.toList());
        return oldQueue;
    }
}
